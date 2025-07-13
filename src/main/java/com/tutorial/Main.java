package com.tutorial;

import com.tutorial.entities.Entity;
import com.tutorial.entities.Schema;
import com.tutorial.entities.Table;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static Connection getConnection() throws SQLException {
        Properties properties = new Properties();
        properties.put("user", System.getenv("DATABASE_USER"));
        properties.put("password", System.getenv("DATABASE_PASSWORD"));

        return DriverManager.getConnection(System.getenv("DATABASE_URL"), properties);
    }

    private static void log(String message) {
        logger.log(Level.INFO, message);
    }

    private static <T extends Entity> List<T> queryDb(Class<T> clazz, String sql, Object... params) {
        try (Connection connection = getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();


            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            List<T> list = new ArrayList<>();

            while (resultSet.next()) {
                T instance = clazz.getDeclaredConstructor().newInstance();
                for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                    String columnName = resultSetMetaData.getColumnName(i);
                    Object value  = resultSet.getObject(i);

                    Field field;
                    try {
                        field = clazz.getDeclaredField(instance.getMappedColumns().get(columnName));
                        field.setAccessible(true);
                        field.set(instance, value);
                    } catch (Exception ignored) {}
                }
                list.add(instance);
            }
            return list;
        } catch (Exception e) {
            log(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(queryDb(Table.class, "SELECT * FROM information_schema.tables where table_schema = ?", "public"));
        System.out.println("\n****************************************************\n");
        System.out.println(queryDb(Schema.class, "SELECT * FROM information_schema.schemata"));
    }
}
