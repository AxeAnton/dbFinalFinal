package ru.ifmo.lessons.base;

import ru.ifmo.lessons.dao.Author;
import java.sql.*;
import java.util.HashSet;

// DAO паттерн

public class JDBCLesson {
    // данные для подключения
    // 3. Ур.1 строка подключения
    static final String CONNECTION_STR =
            "jdbc:postgresql://localhost:5432/lessons"; // 4. Ур.1 jdbc в языке управляет к подключениям к любым БД. Абстракные классы, интерфейсы, обстрактные методы. Используется для работы любых реляционных БД, затем идет двоеточие и название ДБ postgresql, далее :// localhost, это сервир на котором запущена БД, далее порт 5432 на котором запущен postgresql (если ни чего не меняли), далее слэш имя БД lessons.
    static final String LOGIN = "postgres"; // 5. Ур.1 логин
    static final String PWD = " Kemira2023"; // 6. Ур.1 пароль был 123


    private static void createTable() // 7 Ур.1 метод который создает табличку
            throws ClassNotFoundException, SQLException {
        String create = "CREATE TABLE IF NOT EXISTS tb_books(" + // 8 Ур.1 начинаем с запроса create
                "id SERIAL PRIMARY KEY," +
                "title VARCHAR(50) NOT NULL" +
                ");"; //9 Ур.1  круглые скобочки и точка запятой в конце запроса
        Class.forName("org.postgresql.Driver"); // 10 Ур.1  нужна что бы в моменте выполнения подключились классы той библиотеки которая прописана в marven, тоесть мы пишим код использую методы JDBC, методы в которых ни чего не реализованно но в момент выполнения подключаются конкретные реализации методов которые мы прописываем "org.postgresql.Driver" (класс из скаченной библиотеки) и уже они будут работать. Это подключение класса во впемя выполнения
        try (Connection connection = DriverManager.getConnection( // 11 Ур.1 Далее создаем соединения использую данные которые мы перечесляли (строка соединения, логин и пароль - CONNECTION_STR, LOGIN, PWD)
                CONNECTION_STR, LOGIN, PWD
        )){
            // PreparedStatement
            try(Statement statement = connection.createStatement()){ // 12 Ур.1 в очередном try создоем объект что бы используя соединения выполнять запрос,
                int res = statement.executeUpdate(create); // 13 Ур.1 вот это непосредственно выполнение запроса, метод executeUpdate(create) отправит запрос на сервер и вернет количество измененных строк (при изменении  талицы)
                System.out.println(res);
            }
        }
    }

    /*INSERT INTO author (name, age)
    VALUES ('qwerty', 28),*/

    private static void insertIntoAuthorTb(String name, int age)
            throws ClassNotFoundException, SQLException {
        /* String insert = "INSERT INTO author (name, age) " +
                "VALUES (" + name + "," + age + ")"; */
        String insert = "INSERT INTO author (name, age) " +
                "VALUES (?, ?)";

        Class.forName("org.postgresql.Driver");

        try (Connection connection = DriverManager.getConnection(
                CONNECTION_STR, LOGIN, PWD
        )){
            try (PreparedStatement statement =
                         connection.prepareStatement(insert)){
                // передача данных вместо вопросительных знаков
                statement.setString(1, name);
                statement.setInt(2, age);
                int res = statement.executeUpdate();
                System.out.println("результат добавления = " + res);
            }
        }
    }

    private static void getAllAuthors()
            throws ClassNotFoundException, SQLException {
        String select = "SELECT * FROM author;";

        Class.forName("org.postgresql.Driver");

        try (Connection connection = DriverManager.getConnection(
                CONNECTION_STR, LOGIN, PWD
        )){
            try (Statement statement = connection.createStatement()){
                // выполнение select запросов, метод executeQuery
                ResultSet resultSet = statement.executeQuery(select);
                while (resultSet.next()){
                    String name = resultSet.getString("name");
                    int age = resultSet.getInt("age");
                    int id = resultSet.getInt("author_id");
                    System.out.println("name " + name);
                    System.out.println("age " + age);
                    System.out.println("id " + id);
                }
            }
        }
    }

    private static void articlesByDate(String date) throws ClassNotFoundException, SQLException {
        String select = "SELECT title FROM article WHERE created_on > ?";
        Class.forName("org.postgresql.Driver");
        try (Connection connection = DriverManager.getConnection(
                CONNECTION_STR, LOGIN, PWD
        )) {
            try (PreparedStatement statement = connection.prepareStatement(select)){
                statement.setTimestamp(1, Timestamp.valueOf(date));
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    System.out.println("title " +
                            resultSet.getString("title"));
                }
            }
        }
    }

    private static HashSet<Author> getAuthorsByAge(int age)
            throws ClassNotFoundException, SQLException {
        HashSet<Author> authors = new HashSet<>();
        String select = "SELECT * FROM author WHERE age > ?;";
        Class.forName("org.postgresql.Driver");
        try (Connection connection = DriverManager.getConnection(
                CONNECTION_STR, LOGIN, PWD
        )){
            try (PreparedStatement statement
                         = connection.prepareStatement(select)){
                statement.setInt(1, age);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()){
                    Author author = new Author();
                    author.setId(resultSet.getInt("author_id"));
                    author.setAge(resultSet.getInt("age"));
                    author.setName(resultSet.getString("name"));
                    authors.add(author);
                }
            }
        }
        return authors;
    }

    public static void main(String[] args) {
        try {
            createTable(); // 14. завернули в try, зачем не понял, ну и все.
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        /*
        try {
            insertIntoAuthorTb("author 1", 55);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        */

        try {
            getAllAuthors();
            getAuthorsByAge(30).forEach(System.out::println);
            articlesByDate("2019-08-07 00:00:00");
        } catch (ClassNotFoundException | SQLException e) {
            e.getStackTrace();
        }
    }
}
// вывести в консоль названия статей,
// дата создания которых больше '2019-08-07';
// SELECT title FROM article WHERE created_on > '2019-08-07'
/*Date date = new Date();
date.setMonth()..
        date.setYear()..
        date.setDay()..*/


