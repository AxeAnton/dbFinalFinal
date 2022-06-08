package ru.ifmo.lessons.dao;

import ru.ifmo.lessons.pool.C3P0DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AuthorDao implements Dao<Author, Integer> {
    @Override
    public Author add(Author author) {
        String sql = "INSERT INTO author (name, age) VALUES (?, ?)";
        try (PreparedStatement statement = C3P0DataSource
                .getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, author.getName());
            statement.setInt(2, author.getAge());

            int res = statement.executeUpdate();
            System.out.println("add res = " + res);

            // получение первичного ключа
            try (ResultSet resultSet = statement.getGeneratedKeys()){
                if (resultSet.next()) {
                    author.setId(resultSet.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return author;
    }

    @Override
    public void update(Author author) {
        //
        String sql = "UPDATE author SET name = ?, age = ? " +
                "WHERE author_id = ?";
        try (PreparedStatement statement = C3P0DataSource
                .getConnection().prepareStatement(sql)){
            statement.setString(1, author.getName());
            statement.setInt(2, author.getAge());
            statement.setInt(3, author.getId());

            int res = statement.executeUpdate();
            System.out.println("update res = " + res);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteByPK(Integer integer) {

    }

    @Override
    public void delete(Author author) {

    }

    @Override
    public Author getByPK(Integer integer) {
        String sql = "SELECT name, age FROM author WHERE author_id = ?";
        Author author = null;

        try (PreparedStatement statement = C3P0DataSource
                .getConnection().prepareStatement(sql)){
            statement.setInt(1, integer);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                author = new Author();
                author.setAge(resultSet.getInt("age"));
                author.setName(resultSet.getString("name"));
                author.setId(integer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Author> getAll() {
        return null;
    }
}
