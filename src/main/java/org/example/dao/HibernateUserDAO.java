package org.example.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.accounts.models.UserProfile;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HibernateUserDAO implements AccountService {

    private final EntityManager entityManager;

    @Autowired
    public HibernateUserDAO(SessionFactory sessionFactory) {
        this.entityManager = sessionFactory.createEntityManager();
    }

    public List<UserProfile> index() {
        List<UserProfile> userProfiles = null;

        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }

            userProfiles = entityManager.createQuery("SELECT u from UserProfile u", UserProfile.class)
                    .getResultList();

            entityManager.getTransaction().commit();
        } catch (PersistenceException e) {
            log.error("Ошибка при получении списка пользователей из базы данных");
            e.printStackTrace();
        }
        return userProfiles;
    }

    public void addUser(UserProfile profile) {

        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }

            entityManager.persist(profile);

            entityManager.getTransaction().commit();
        } catch (PersistenceException e) {
            log.error("Ошибка при добавлении пользователя в базу данных");
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }

    public UserProfile getUserById(int id) {
        UserProfile userProfile = null;

        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }

            userProfile = entityManager.find(UserProfile.class, id);

            entityManager.detach(userProfile);
        } catch (PersistenceException e) {
            log.error("Ошибка при получении пользователя по ID из базы данных");
            e.printStackTrace();
        }
        return userProfile;
    }

    public void updateUser(int id, UserProfile profile) {
        UserProfile userProfile = null;

        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }

            userProfile = entityManager.find(UserProfile.class, id);

            userProfile.setLogin(profile.getLogin());
            userProfile.setRole(profile.getRole());
            userProfile.setName(profile.getName());
            userProfile.setEmail(profile.getEmail());

            entityManager.persist(userProfile);

            entityManager.getTransaction().commit();
        } catch (PersistenceException e) {
            log.error("Ошибка при обновлении пользователя в базе данных");
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }

    public void deleteUserById(int id) {

        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }

            entityManager.remove(entityManager.find(UserProfile.class, id));

            entityManager.getTransaction().commit();
        } catch (PersistenceException e) {
            log.error("Ошибка при удалении пользователя из базы данных");
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }

    public UserProfile getUserByLogin(String login) {
        UserProfile userProfile = null;

        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }

            userProfile = entityManager.createQuery("from UserProfile where login=:login",
                            UserProfile.class)
                    .setParameter("login", login).getSingleResult();

            entityManager.getTransaction().commit();
        } catch (NoResultException e) {
            return null;
        } catch (PersistenceException e) {
            log.error("Ошибка при получении пользователя по логину из базы данных");
            e.printStackTrace();
        }
        return userProfile;
    }

    public boolean checkIfLoginExists(String login) {
        UserProfile userProfile = null;
        try {
            userProfile = getUserByLogin(login);
        } catch (PersistenceException e) {
            log.error("Ошибка при проверке наличия логина в базе данных");
            e.printStackTrace();
        }
        return userProfile != null;
    }
}
