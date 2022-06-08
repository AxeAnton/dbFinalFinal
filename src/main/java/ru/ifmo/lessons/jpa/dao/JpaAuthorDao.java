package ru.ifmo.lessons.jpa.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ru.ifmo.lessons.dao.Author;
import ru.ifmo.lessons.dao.Dao;

import java.util.ArrayList;
import java.util.List;

//31. Ур.3. - имплементирует Dao интерфейс из старого пакета, этот класс JpaAuthorDao будет работать с сущьностью Author из текущего пакета, ТД первичного ключа Integerю.
public class JpaAuthorDao implements Dao<Author, Integer> {

//32. Ур.3. - и делаем имплементацию (необходимые переопределения) всех методов.

    //37. Ур.3. - вот что еще забыли, т.к библиотеке JpaAuthorDao работает с EntityManager, то необходимо добавить свойство EntityManager и передать его в конструктор (если я правильно понял, то если мы, где-то в Application, добавим свойство Authre оно так же попадет сюда).
    private EntityManager manager;

    public JpaAuthorDao(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public Author add(Author author) {
        return null;
    }

    @Override
    public void update(Author author) {
    }

    @Override
    public void deleteByPK(Integer integer) {
    }

    @Override
    public void delete(Author author) {
    }

    @Override
    public Author getByPK(Integer integer) {
        return null;
    }

    //33. Ур.3. - Есть много вариантов написать запрос для DAO библиотеки.
    @Override
    public List<Author> getAll() {

/*
    //34. Ур.3. - 1. ПЕРВЫЙ вариант это ИМЕННОВАННЫЕ - name native query - как в чистом SQL синтаксисе. Т.к. запрос именованный то сам запрос будет размещаться над сущностью, т.к Dao класс работает с автором, следовательно, над классом Author, идем в Author п.35
    //36. Ур.3. - Создаем экземпляр запроса, он типизированный, <указываем, что мы хотим получить в результате> т.е. объекты какого типа мы должны собирать из таблички (Author), п.37, а потом продолжаем здесь же, обращаемся к manager и вызываем метод (именнованный запрос) createNamedQuery, в аргумент передаем имя запроса - "getAllAuthors.native" и второй арг., это ТД который хотим получить - Author.class.
    TypedQuery<Author> query = manager.createNamedQuery("getAllAuthors.native", Author.class);
    //38. Ур.3. - т. хотим получить СПИСОК авторов то доб авляем их в .getResultList(). И возвращаем автора.
    List<Author> authors =query.getResultList();
    return Author
*/

/*
    //  43. Ур.3. - 2. ВТОРОЙ вариант это ИМЕННОВАННЫЕ - name JPQL query запрос будет выполняться так же, как и в п.36.
    TypedQuery<Author> query = manager.createNamedQuery("getAllAuthors.jpql", Author.class);
    List<Author> authors =query.getResultList();
*/

/*
    // 44. Ур.3. - 3. ТРЕТЬИЙ вариант это НЕ ИМЕННОВАННЙ запрос - name JPQL query. Используется если предыдущие два чем-то не устраивают или инфо о запросе приходит динамически или со стороны.
    String jpql = "SELECT a FROM Author a";
    TypedQuery<Author> query = manager.createQuery(jpql, Author.class);
    List<Author> authors = query.getResultList();
    return Author
*/

        // 52. Ур.3. - Тренировка, необходимо создать JPQL запрос ДЛЯ author, база 3-й вариант п.44.
        String jpqlNames = "SELECT a.name FROM Author a";
        TypedQuery<String> queryNames = manager.createQuery(jpqlNames, String.class);
        List<String> names = queryNames.getResultList();
        System.out.println("NAMES" + names);


        // 53. Ур.3. - 4. ЧЕТВЕРТЫЙ вариант, по правилам ООП, называется - Criteria API.
        // 54. Ур.3. - сriteriaBuilder строит/формирует SQL запрос.  Есть еще CriteriaQuery, это аналог строки запроса. Запрос выглядит как SELECT FROM tb_authors
        // 55. Ур.3. - criteriaBuilder плдучается через manager, затем criteriaQuery через criteriaBuilder + метод createQuery, в арг. ссылка на класс(объекты которого мы хотим получить, т.е это ТД (string, integer и тд.))
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Author> criteriaQuery = criteriaBuilder.createQuery(Author.class); //тип данных результата
        // 56. Ур.3. - Root (класс который ассоциирован с конкретной табличкой) это таблица из которой мы берем данные.
        Root<Author> root = criteriaQuery.from(Author.class); // 57. Ур.3. - Строка аналогична вот этому куску запроса FROM tb_authors
        criteriaQuery.select(root); // 58. Ур.3. - Строка аналогична  вот этому куску запроса SELECT
        // 58. Ур.3. -извлечение данных будет аналогично п. 52
        TypedQuery<Author> query = manager.createQuery(criteriaQuery);
        List<Author> authors = query.getResultList();
        return authors;
    }

    // 45. Ур.3. - напишем метод который возвращает Author (не список), а принимает имя и возраст. ИМЕННОВАННЙ запрос - name JPQL query.
    public Author getByNameAndAge(String name, int age) {
/*
       // 46. Ур.3. - Запрос начинает формироваться так же, как и запросе п. 43.
       // 47. Ур.3. - Теперь есть задача, но перед началом решения необходимо понять, что такое ":name" в запросе - "SELECT a FROM author a WHERE a.name = :name AND a.age = :name "),  а это параметр (наличие двоеточия указывает на это, а имя может быть любым) - не понял откуда я о параметре должен был. а задача состоит в следующем, передать вместо параметра :name и :name из аргумента п. 45.
       TypedQuery<Author> query = manager.createNamedQuery("getByNameAndAge.jpql", Author.class);
       // 48. Ур.3. - обращаемся к запросу, ставим точку, вызываем метод setParameter, передаем в арг. имя параметра (то что после двоеточия) и значение параметра то что из арг. п.45.
       query.setParameter("name", name);
       query.setParameter("age", age);

        // 49. Ур.3. -  Получение параметров, обращаемся к запросу author, затем вызываем метод getSingleResult и получим результат не в составе списка, а один.
        Author author = query.getSingleResult();

         return author;
*/
        // 50. Ур.3. -  Запрос может быть  ИМЕННОВАННЙ нативнный (native) запрос, он будет формироваться точно так же п. 45-49. Дальше не понятная фраза, если строчка запроса будет не в аннотации то тоже все то же самое, просто добавлятся две строчки п 48.

        // 51. Ур.3. - Вариант на получение записи, где параметры не передаются - ПЕРВЫЙ вариант у нас параметры не передаются п. 34 и третий вариант п.44, где параметры передаются, по сути появляется блок WHERE. Идем на 52.

        // 59. Ур.3. - Этот запрос аналогичен п.53-58. SELECT * FROM tb_author WHERE author_name = ? (или age = ?) AND
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        // 60. Ур.3. -тип данных результата
        CriteriaQuery<Author> criteriaQuery = criteriaBuilder.createQuery(Author.class);
        // 70. Ур.3. - FROM tb_author // откуда мы получаем эти данные
        Root<Author> root = criteriaQuery.from(Author.class);

        // 72. Ур.3. - Для критерия OPI каждое условие блока WHERE это отдельный объект (1й объект - author_name = ?, 2й объект - age = ?), каждое условие из блока WHERE это ТД predict (predict из jakarta)
        // 73. Ур.3. - формируется следующим образом, обращаемся к criteriaBuilder, а методов много, можно выбрать из выпадающего списка, нам нужен - equal и далее с чем сравниваем "name" из root с переменной name.
        Predicate nameCond = criteriaBuilder.equal(root.get("name"), name);
        // 74. Ур.3. - здесь по аналогии п 73
        Predicate ageCond = criteriaBuilder.equal(root.get("age"), age);
        // 75. Ур.3. - если было несколько условий создается один предикат (объединение), через criteriaBuilder (имеет три методе для объединения через and, not или or), почему то у нас and, поэтому в метод передаем све придикаты которые у нас есть. И итоговый придикат добавляется в блок WHERE.
        Predicate allCond = criteriaBuilder.and(nameCond, ageCond);

        // 71. Ур.3. -SELECT *
        criteriaQuery.select(root).where((allCond));

        TypedQuery<Author> query = manager.createQuery(criteriaQuery);
        Author author = query.getSingleResult();

        return author;
    }

}
