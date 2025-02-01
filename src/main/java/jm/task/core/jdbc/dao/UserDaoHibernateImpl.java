package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

@Slf4j
public class UserDaoHibernateImpl implements UserDao {

    @Override
    public void createUsersTable() {
        String sql = Util.getProperty("hibernate.create_table");
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
            log.info("Таблица пользователей успешно создана.");
        } catch (Exception e) {
            log.error("Ошибка при создании таблицы пользователей.", e);
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = Util.getProperty("hibernate.drop_table");
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
            log.info("Таблица пользователей успешно удалена.");
        } catch (Exception e) {
            log.error("Ошибка при удалении таблицы пользователей.", e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sql = Util.getProperty("hibernate.insert_user");
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            transaction.commit();
            log.info("Пользователь с именем {} добавлен в базу данных.", name);
        } catch (Exception e) {
            log.error("Ошибка при добавлении пользователя {}.", name, e);
        }
    }

    @Override
    public void removeUserById(long id) {
        String sql = Util.getProperty("hibernate.delete_user");
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                transaction.commit();
                log.info("Пользователь с id {} удален из базы данных.", id);
            } else {
                log.warn("Пользователь с id {} не найден.", id);
            }
        } catch (Exception e) {
            log.error("Ошибка при удалении пользователя с id {}.", id, e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sql = Util.getProperty("hibernate.select_all_users");
        try (Session session = Util.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            log.error("Ошибка при получении всех пользователей.", e);
            return Collections.emptyList();
        }
    }

    @Override
    public void cleanUsersTable() {
        String sql = Util.getProperty("hibernate.clean_table");
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("delete from User").executeUpdate();
            transaction.commit();
            log.info("Таблица пользователей успешно очищена.");
        } catch (Exception e) {
            log.error("Ошибка при очистке таблицы пользователей.", e);
        }
    }
}