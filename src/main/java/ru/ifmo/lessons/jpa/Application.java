package ru.ifmo.lessons.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import ru.ifmo.lessons.jpa.dao.JpaAuthorDao;
import ru.ifmo.lessons.jpa.entity.Article;
import ru.ifmo.lessons.jpa.entity.Author;

public class Application {
    public static void main(String[] args) {

        // 38. Ур.2 - EntityManager это центральное звено (всех реализаций persistenceap), менеджер управления объектами класса сущностей, через него проходит взаимодействие с БД, создаются фабриками (когда набор классов создают объекты), но есть целый набор классов который создает объекты в данном случаи используется эта EntityManagerFactory,
        // 39. Ур.2 - чтобы создать менеджера сущности, сначала нужно создать фабрику которая его создаст, EntityManager создается фабрикой, поэтому сначало создаем фабрику (объекты такого EntityManagerFactory типа), один объект на ОДИН persistence unit, и в методе createEntityManagerFactory мы должны указать имя persistence unit из persistence.xml файла. Получается, что вот эта фабрика factory, которая привязана к unit "ormLesson" будет создавать менеджеров сущности (EntityManager) которые управляют классами вот этих объектов <class> ru.ifmo.lessons.jpa.entity.Author </class>
        // 40. Ур.2 - Манеджер должен создаваться в try (с ресурсами) или быть закрытым после использования НЕ сразу (вручную через метод close).
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("ormLesson");

        // 41. Ур.2 -Если есть фабрика, то можно создавать менеджера, который будет управлять объектами unit-а (вот этими <class> ru.ifmo.lessons.jpa.entity.Author </class>).
        // 43. Ур.2 - Менеджер так же болжен быть в try (с ресурсами) или закрыт после использования НЕ сразу (вручную через метод close).
        EntityManager manager = factory.createEntityManager();

        // 45. Ур.2 - создаем объект
        Author max = new Author();
        max.setName("Max");
        max.setAge(37);

        // 48. Ур.2 - По не понятной причине, manager до сих пор не управляет Author max, но если мы manager.persist(max); вынесем перед begin() то информация добовляется в heshMapu и  manager берется под управление объект и добавляет инфо в БД.
        manager.persist(max);

        // 45. Ур.2 -  добавление информации об объекте в таблицу, через транзакцию и открытие
        manager.getTransaction().begin();

        //46. Ур.2 - между добавлением запроса (manager.getTransaction().begin();) и подтверждением запроса (manager.getTransaction().commit()) можно выполнить несколько запросов к БД, в случаи ошибки, можно вызвать метод manager.getTransaction().rollback();, этот запрос должен быть в try (), тогда ни один запрос не будет выполнен.

        //48. Ур.2 - что бы добавить информацию (данные) в табличку мы должны обратиться к  manager вызвать  метод persist(max), а размещен он должен быть межюу begin и commit
        manager.persist(max);

        //47. Ур.2 - подтверждение запроса, состояние БД меняться согласно выполненным запросам: rollback(); - отмена транзакции, отменяет все запросы, если ошибки нет то транзакция выполнится.
        manager.getTransaction().commit();

        // 48. Ур.2 - какие возможности есть по умолчанию у манагера, написать запрос на получение данных по первичному ключу
        // 49. Ур.2 - например, создается запрос manager.find (метод fine позволяет получить инфо из таблицы + объект который сгенерируется методом fine, находится под управлением manager), затем определит к какой таблички нужно обращаться Author.class, где значение первичного ключа = 1, получается что он извлечет данные из таблички, на основе этих данных соберет объект и отдаст нам на выходе объект вот этого класса - Author
        Author authorFromDB = manager.find(Author.class, 1);
        System.out.println("--- authorFromDB ----" + authorFromDB);

        // 50. Ур.2 - обновление уже существующего объекта в таблице (должен существовать в базе) max меняем на maxim (замена свойства объекта).
        authorFromDB.setName("Maxim");

        // 51. Ур.2 - далее ОБЯЗАТЕЛЬНО в рамках транзакции (между .begin(); и n().commit();) мы вызываем метод merge (необходим, что бы инфо с БД и инфо из HashMup менеджера слились.)
        manager.getTransaction().begin();
        manager.merge(max);
        manager.getTransaction().commit();

        // 52. Ур.2 - затем можно его снова получить и посмотреть каким же он стал в итоге этот объект из БД
        authorFromDB = manager.find(Author.class, 1);
        System.out.println(authorFromDB);

        // 53. Ур.2 - а потом можно и удалить.
        manager.getTransaction().begin();
        manager.remove(max);
        manager.getTransaction().commit();

        // 16. Ур.3. - информация по этому автору и статье, пока, нет в табличке, мы их хотим добавить + их конструктор.
        Author paul = new Author();
        paul.setName("Paul");
        paul.setAge(32);

        Article article = new Article();
        article.setTitle("Java");
        article.setText("Java text");
/*
        // 19. Ур.3. - вместо двух инструкций п. 17 и 18 (не соотведствуют ООП) можно в классе Author прописать метод:
            void addArticle(Article article) { // в фигурных скобках можно добавить необходимые проверки например на не null
                article.setAuthor(this); // привязка статьи к автору this это текущий автор.
                articles.add(article)
*/

        // 17. Ур.3. - связываем автора и статью (взаимная ссылка обязательна)
        paul.getArticles().add(article);
        // 18. Ур.3. - связываем статью и автора (взаимная ссылка обязательна)
        article.setAuthor(paul);

        // 20. Ур.3. - остается подтвердить транзакцию (теги bigin и commit).
        manager.getTransaction().begin();
        // 21. Ур.3. - при отсудствии каскадной вставки, метод persist нужно вызывать и для Article
        manager.persist(paul);
        manager.getTransaction().commit();

        JpaAuthorDao dao = new JpaAuthorDao(manager);
        System.out.println("AUTHORS" + dao.getAll());

    }
}
