package com.tutorial;

import com.tutorial.db.DataSource;
import com.tutorial.db.entities.Entity;
import com.tutorial.db.entities.Users;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static EntityManagerFactory emf;

    private static void log(String message) {
        logger.info(message);
    }

    private static EntityManager getEntityManager() {
        if (emf == null) {
            HikariDataSource dataSource = DataSource.getDataSource();
            HashMap<String, Object> properties = new HashMap<>();
            properties.put("jakarta.persistence.nonJtaDataSource", dataSource);
            emf = Persistence.createEntityManagerFactory("persist", properties);
        }
        return emf.createEntityManager();
    }

    private static <T extends Entity> List<T> queryDb(Class<T> clazz, String sql, Object... params) {
        try (EntityManager em = getEntityManager()) {
            Query query = em.createQuery(sql, clazz);
            return query.getResultList();
        }  catch (Exception ex) {
            log(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        queryDb(Users.class, "SELECT a FROM console_users a").forEach((user) -> {
            log(String.format("User: %s - %s", user.getFullName(), user.getEmail()));
        });
    }
}
