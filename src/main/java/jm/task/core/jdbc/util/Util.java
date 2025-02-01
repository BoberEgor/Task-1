package jm.task.core.jdbc.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import jm.task.core.jdbc.model.User;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {

    private static final Properties properties = new Properties();
    private static SessionFactory sessionFactory;

    static {
        try (InputStream input = Util.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Не удалось найти файл application.properties");
            }
            properties.load(input);


            sessionFactory = new Configuration()
                    .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                    .setProperty("hibernate.connection.url", properties.getProperty("db.url"))
                    .setProperty("hibernate.connection.username", properties.getProperty("db.username"))
                    .setProperty("hibernate.connection.password", properties.getProperty("db.password"))
                    .setProperty("hibernate.dialect", properties.getProperty("hibernate.dialect"))
                    .setProperty("hibernate.show_sql", properties.getProperty("hibernate.show_sql"))
                    .setProperty("hibernate.hbm2ddl.auto", properties.getProperty("hibernate.hbm2ddl.auto"))
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при настройке Hibernate", e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Connection getJdbcConnection() {
        try {
            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при подключении к базе данных через JDBC", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
