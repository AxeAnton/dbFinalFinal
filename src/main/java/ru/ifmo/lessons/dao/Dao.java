package ru.ifmo.lessons.dao;

import java.util.List;

public interface Dao<T, PK> {
    // добавление данных в таблицу
    T add(T t);
    // обновление данных в таблице
    void update(T t);
    // удаление данных по первичному ключу из таблицы
    void deleteByPK(PK pk);
    void delete(T t);
    // получение данных по первичному ключу
    T getByPK(PK pk);
    // получение всех записей
    List<T> getAll();
}
