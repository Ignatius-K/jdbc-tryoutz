package com.tutorial;

import com.tutorial.db.DataSource;
import com.tutorial.db.entities.Entity;
import com.tutorial.db.entities.Schema;
import com.tutorial.db.entities.Table;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static void log(String message) {
        logger.log(Level.INFO, message);
    }

    private static <T extends Entity> List<T> queryDb(Class<T> clazz, String sql, Object... params) {
        try (Connection connection = DataSource.getConnection()) {

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
        int TOTAL_QUERIES = 10000;
        ExecutorService executor = Executors.newFixedThreadPool(500);
        CountDownLatch latch = new CountDownLatch(TOTAL_QUERIES);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();

        System.out.println("Initial Pool Stats: \n" + DataSource.info());

        for (int i = 0; i < TOTAL_QUERIES; i++) {
            int finalI = i;
            executor.submit(() -> {
                if (finalI % 1000 == 0) {
                    System.out.println(DataSource.info());
                }
                try (
                        Connection conn = DataSource.getConnection();
                        PreparedStatement stmt = conn.prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_schema = ?")
                ) {
                    stmt.setString(1, "public");
                    try (ResultSet rs = stmt.executeQuery()) {
                        int rowCount = 0;
                        while (rs.next()) {
                            rowCount++;
                        }
                        successCount.incrementAndGet();
                    }

                } catch (SQLException e) {
                    System.err.println("Query failed: " + e.getMessage());
                    failureCount.incrementAndGet();
                } finally {
                    latch.countDown();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ignored) {}
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted while waiting.");
        }
        executor.shutdown();

        System.out.println("\n=== Complete ===");
        System.out.printf("Success: %s\tFailed: %s%n", successCount.get(), failureCount.get());
        System.out.println("Final Pool Stats: \n" + DataSource.info());

        DataSource.shutdown();
    }
}
