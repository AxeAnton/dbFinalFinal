package ru.ifmo.lessons.jpa.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//4. Ур.3 - затем прописываем все необходимые аннотации
@Getter
@Setter
//5. Ур.3 - интересный момент, что данный notNull для свойства, а п.6 для столбца таблички.
@NonNull
//7. Ур.3 - если планируем хранить данные в коллекциях, вызываем родителя и еще можно исключить некоторые поля..
@EqualsAndHashCode(callSuper = true, exclude = {"text", "createdOn"})
@Entity

public class Article extends BaseID {

    //6. Ур.3 - интересный момент, что в п.5 notNull для свойства, а здесь для столбца таблички.
    //1. Ур.3 - записываем свойства и прописываем им аннотации
    @Column(length = 100, unique = true, nullable = false)
    private String title;

    //2. Ур.3 - так же аннотацию можно записать в виде языка SQL
    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;
    /*
        // 15. Ур.3. - добавляем аннотацию @ManyToOne, много статей у одного автора.
        По умолчанию свойство аннотации FetchType.EAGER - безусловное извлечение (вся инфо об авторе), если этого не надо то меняем на LAZY
    */
    //8. Ур.3 - ВАЖНО если мы используем ORM библиотеки то связи между сущностями выражаются в ссылках на объекты или в сылках на коллекции этих объектов. По русски это означает, что если у таблички Article есть колонка в которой учитывается Id автора (следовательно есть связь между табличками автор и статья), то в Article должно быть и СВОЙСТВО Author (private Author author;). А еще это означает, что свойство Article, должно быть добавлено в класс Author п.9.
    @ManyToOne(fetch = FetchType.LAZY) //FetchType.LZYA
    private Author author;


    // если это не jakarta, а javax-persistence-api (стара версия) не умеет работать с  LocalDateTime, а умеют работать только с Date и Calender.
    private LocalDateTime createdOn;

    //26. Ур.3. - аналогично п. 25, только в обратную сторону. Не забываем добавить в конструктор свойство ination> nominations; п. 27.
    //28. Ур.3. - затем мы должны определить кто владелец взаимосвязи (может быть любая сторона), и ему добавляем (mappedBy = "articles") при этом имя столбца "articles" это связанное свойство из класса номинации п 25.
    //28. Ур.3. - Будет создана таблица (невидимая) с двумя колонками, добавив аннотацию (позволяет немного корректировать таблицу) @JoinTable(name = "имя таблицы", joinColumns = @JoinColumn("имя столбца для текущей таблички", inversejoinColumns = @JoinColumn(name = (имя столбца для другой таблицы))
    @ManyToMany(mappedBy = "articles")
    private List<Nomination> nominations;

    //29. Ур.3. - Важно, что такая связь не позволяет фактически видеть/создать табличку сравнения (она создается где-то внутри), следовательно, ее нельзя раcширить, добавив колонки, например, кто победил, сколько баллов и т.д., что бы это было возможно, необходимо создать НЕ виртуальную табличку, а через новый класс (новая табличка где будет две колонки статьи и номинации + все необходимые) и в классе создать связи  @ManyToOne (от конкурса к колонке с конкурсами ) и @ManyToOne (от статьи к колонке со статьями)

    //3. Ур.3 - чтобы при создании статьи сразу подставлялось текущее дата и время необходимо в конструкторе прописать свойству - createdOn = LocalDateTime.now();
    public Article() {
        createdOn = LocalDateTime.now();
        // 27. Ур.3. - для nominations создаем ArrayList<>();
        nominations = new ArrayList<>();
    }
}

// 30. Ур.3. - В задание добавил. Еcли в курсовой #3 будут класс user или Group, то надо менять название таблицы используя @Table