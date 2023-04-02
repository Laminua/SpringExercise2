package org.example.dao;

import org.example.accounts.models.UserProfile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    List<UserProfile> index();
    void addUser(UserProfile profile);
    UserProfile getUserById(int id);
    void updateUser(int id, UserProfile profile);
    void deleteUserById(int id);
    UserProfile getUserByLogin(String login);
    boolean checkIfLoginExists(String login);
}
