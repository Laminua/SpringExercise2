package org.example.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.accounts.models.UserProfile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery("SELECT u from UserProfile u", UserProfile.class)
                .getResultList();
    }

    @Transactional
    public void addUser(UserProfile profile) {
        Session session = sessionFactory.getCurrentSession();
        session.save(profile);
    }

    @Transactional(readOnly = true)
    public UserProfile getUserById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(UserProfile.class, id);
    }

    @Transactional
    public void updateUser(int id, UserProfile profile) {
        Session session = sessionFactory.getCurrentSession();
        UserProfile userProfile = session.get(UserProfile.class, id);

        userProfile.setLogin(profile.getLogin());
        userProfile.setRole(profile.getRole());
        userProfile.setName(profile.getName());
        userProfile.setEmail(profile.getEmail());
    }

    @Transactional
    public void deleteUserById(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(session.get(UserProfile.class, id));
    }

    @Transactional(readOnly = true)
    public UserProfile getUserByLogin(String login) {
        UserProfile userProfile;
        Session session = sessionFactory.getCurrentSession();

        Query<UserProfile> query = session.createQuery("from UserProfile where login=:login",
                UserProfile.class);
        query.setParameter("login", login);
        userProfile = query.uniqueResult();

        return userProfile;
    }

    @Transactional
    public boolean checkIfLoginExists(String login) {
        UserProfile userProfile;
        userProfile = getUserByLogin(login);
        if (userProfile == null) {
            return false;
        }
        return true;
    }
}
