package ru.ifmo.lessons.jpa.entity;

// 22. Ур.3. - данный класс необходим, т.к у нас возникает табличка конкурсы, и образовывается связь ManyToMany между табличкой статьи и конкурсы, т.к одна статья может принять участие во многих конкурсах и в одном конкурсе может учавствовать много статей. При создании такой связи ManyToMany создается табличка (но мы ее не видим, с двумя столбцами где каждой статье ссотведствете конкурс)

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NonNull
@Entity
// 24. Ур.3. - в persistence.xml добавляем класс - <class>ru.ifmo.lessons.jpa.entity.Nomination</class>
public class Nomination extends BaseID{

    private String title;
    private LocalDateTime date;

    // 25. Ур.3. - описание аннотации без класса. Создаем свойство - коллекция статей. Аннотация @ManyToMany так же должна быть добавлена. Аналогично создаем и для Article п. 26.
    @ManyToMany
    private List<Article> articles;


    public Nomination(){
        articles = new ArrayList<>();

    }

}