package org.example.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.accounts.models.UserProfile;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HibernateUserDAO implements AccountService {

    private final SessionFactory sessionFactory;

    public List<UserProfile> index() {
        List<UserProfile> userProfiles = null;

        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        try {
            transaction.begin();

            userProfiles = session.createQuery("SELECT u from UserProfile u", UserProfile.class)
                    .getResultList();

            transaction.commit();
        } catch (HibernateException e) {
            log.error("Ошибка при получении списка пользователей из базы данных");
            e.printStackTrace();
            transaction.rollback();
        } finally {
            session.close();
        }
        return userProfiles;
    }

    public void addUser(UserProfile profile) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        try {
            transaction.begin();

            session.save(profile);

            transaction.commit();
        } catch (HibernateException e) {
            log.error("Ошибка при добавлении пользователя в базу данных");
            e.printStackTrace();
            transaction.rollback();
        } finally {
            session.close();
        }
    }

    public UserProfile getUserById(int id) {
        UserProfile userProfile = null;

        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        try {
            transaction.begin();

            userProfile = session.get(UserProfile.class, id);

            transaction.commit();
        } catch (HibernateException e) {
            log.error("Ошибка при получении пользователя по ID из базы данных");
            e.printStackTrace();
            transaction.rollback();
        } finally {
            session.close();
        }
        return userProfile;
    }

    public void updateUser(int id, UserProfile profile) {
        UserProfile userProfile = null;

        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        try {
            transaction.begin();

            userProfile = session.get(UserProfile.class, id);

            userProfile.setLogin(profile.getLogin());
            userProfile.setRole(profile.getRole());
            userProfile.setName(profile.getName());
            userProfile.setEmail(profile.getEmail());

            session.save(userProfile);

            transaction.commit();
        } catch (HibernateException e) {
            log.error("Ошибка при обновлении пользователя в базе данных");
            e.printStackTrace();
            transaction.rollback();
        } finally {
            session.close();
        }
    }

    public void deleteUserById(int id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        try {
            transaction.begin();

            session.delete(session.get(UserProfile.class, id));

            transaction.commit();
        } catch (HibernateException e) {
            log.error("Ошибка при удалении пользователя из базы данных");
            e.printStackTrace();
            transaction.rollback();
        } finally {
            session.close();
        }
    }

    public UserProfile getUserByLogin(String login) {
        UserProfile userProfile = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        try {
            transaction.begin();

            userProfile = session.createQuery("from UserProfile where login=:login",
                            UserProfile.class)
                    .setParameter("login", login)
                    .uniqueResult();

            transaction.commit();
        } catch (HibernateException e) {
            log.error("Ошибка при получении пользователя по логину из базы данных");
            e.printStackTrace();
            transaction.rollback();
        } finally {
            session.close();
        }
        return userProfile;
    }

    public boolean checkIfLoginExists(String login) {
        UserProfile userProfile = null;
        try {
            userProfile = getUserByLogin(login);
        } catch (HibernateException e) {
            log.error("Ошибка при проверке наличия логина в базе данных");
            e.printStackTrace();
        }
        return userProfile != null;
    }
}
