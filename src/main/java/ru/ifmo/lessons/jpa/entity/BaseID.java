package ru.ifmo.lessons.jpa.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.ToString;

// 59. Ур.2 - для вывода добавляем toString
@ToString

// 60. Ур.2 - ЕСЛИ на основе класса BaseID (родительский класс entity класса) таблица НЕ СОЗДАЕТСЯ, то он отмечается аннотацией MappedSuperclass. Это необходимо для понимания, что класс хоть и не создает таблицу, но все равно относится к классу сущностей.
@MappedSuperclass

// 55. Ур.2 - делаем его абстрактным, т.к, объекты, мы точно не собираемся создавать
public abstract class BaseID {

    //58. Ур.2 - аналогично @Id и @GeneratedValue в классе Author.
    @Id

    // 56. Ур.2 - интересная вещь, т.к все на аннотациях, то и проверки можно сделать на анастациях, вот самые популярные  javax.validation или hibernate-validation
    // 57. Ур.2 - и получится, что вот эта библиотеки (javax.validation(без реализаций) или hibernate-validation(с реализацией)) в момент вызова сетторами добавляют проверку id, это валидация которая генерируется самостоятельно, и если проверка не проходит, то программа падает с сообщениями.
/*
    @Max(23, "")
    @Mib(11,)
*/

    // 58. Ур.2 - единственное меняем стратегию на IDENTITY - автоинкримент меняется за счет ТД SERIAL
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;

}