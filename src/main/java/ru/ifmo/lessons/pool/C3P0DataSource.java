package ru.ifmo.lessons.pool;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class C3P0DataSource {
    // пул соединений
    private static ComboPooledDataSource pool =
            new ComboPooledDataSource();

    /*static {
        try {
            pool.setDriverClass("org.postgresql.Driver");
            pool.setJdbcUrl("jdbc:postgresql://localhost:5432/lessons");
            pool.setUser("postgres");
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }*/
    public static Connection getConnection() throws SQLException {
        // получение поединения из пула
        return pool.getConnection();
    }
}

