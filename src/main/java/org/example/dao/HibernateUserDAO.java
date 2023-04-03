package org.example.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.accounts.models.UserProfile;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HibernateUserDAO implements AccountService {

    private final SessionFactory sessionFactory;

    @Transactional(readOnly = true)
    public List<UserProfile> index() {
        List<UserProfile> userProfiles = null;

        Session session = sessionFactory.getCurrentSession();
        try {
            userProfiles = session.createQuery("SELECT u from UserProfile u", UserProfile.class)
                    .getResultList();
        } catch (HibernateException e) {
            log.error("Ошибка при получении списка пользователей из базы данных");
            e.printStackTrace();
        }
        return userProfiles;
    }

    @Transactional
    public void addUser(UserProfile profile) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.save(profile);
        } catch (HibernateException e) {
            log.error("Ошибка при добавлении пользователя в базу данных");
            e.printStackTrace();
        }
    }

    @Transactional(readOnly = true)
    public UserProfile getUserById(int id) {
        UserProfile userProfile = null;

        Session session = sessionFactory.getCurrentSession();

        try {
            userProfile = session.get(UserProfile.class, id);
        } catch (HibernateException e) {
            log.error("Ошибка при получении пользователя по ID из базы данных");
            e.printStackTrace();
        }
        return userProfile;
    }

    @Transactional
    public void updateUser(int id, UserProfile profile) {
        UserProfile userProfile = null;

        Session session = sessionFactory.getCurrentSession();
        try {
            userProfile = session.get(UserProfile.class, id);

            userProfile.setLogin(profile.getLogin());
            userProfile.setRole(profile.getRole());
            userProfile.setName(profile.getName());
            userProfile.setEmail(profile.getEmail());
        } catch (HibernateException e) {
            log.error("Ошибка при обновлении пользователя в базе данных");
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteUserById(int id) {
        Session session = sessionFactory.getCurrentSession();

        try {
            session.delete(session.get(UserProfile.class, id));
        } catch (HibernateException e) {
            log.error("Ошибка при удалении пользователя из базы данных");
            e.printStackTrace();
        }
    }

    @Transactional(readOnly = true)
    public UserProfile getUserByLogin(String login) {
        UserProfile userProfile = null;
        Session session = sessionFactory.getCurrentSession();

        try {
            userProfile = session.createQuery("from UserProfile where login=:login",
                            UserProfile.class)
                    .setParameter("login", login)
                    .uniqueResult();
        } catch (HibernateException e) {
            log.error("Ошибка при получении пользователя по логину из базы данных");
            e.printStackTrace();
        }
        return userProfile;
    }

    @Transactional
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
