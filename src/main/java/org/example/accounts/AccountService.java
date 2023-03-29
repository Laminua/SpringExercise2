package org.example.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final DataSource dataSource;

    public List<UserProfile> index() {
        List<UserProfile> usersList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM UserProfile");

            while (resultSet.next()) {
                UserProfile userProfile = new UserProfile();

                userProfile.setId(resultSet.getInt("id"));
                userProfile.setLogin(resultSet.getString("login"));
                userProfile.setRole(Role.valueOf(resultSet.getString("role")));
                userProfile.setName(resultSet.getString("name"));
                userProfile.setEmail(resultSet.getString("email"));

                usersList.add(userProfile);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    public void addUser(UserProfile profile) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO UserProfile(login, role, name, email) VALUES(?, ?, ?, ?)")) {

            preparedStatement.setString(1, profile.getLogin());
            preparedStatement.setString(2, profile.getRole().toString());
            preparedStatement.setString(3, profile.getName());
            preparedStatement.setString(4, profile.getEmail());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UserProfile getUserById(int id) {
        UserProfile userProfile = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT * FROM UserProfile WHERE id=?")) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            userProfile = new UserProfile();
            userProfile.setId(resultSet.getInt("id"));
            userProfile.setLogin(resultSet.getString("login"));
            userProfile.setRole(Role.valueOf(resultSet.getString("role")));
            userProfile.setName(resultSet.getString("name"));
            userProfile.setEmail(resultSet.getString("email"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userProfile;
    }

    public void updateUser(int id, UserProfile profile) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(
                             "UPDATE UserProfile SET login=?, role=?, name=?, email=? WHERE id=?")) {

            preparedStatement.setString(1, profile.getLogin());
            preparedStatement.setString(2, profile.getRole().toString());
            preparedStatement.setString(3, profile.getName());
            preparedStatement.setString(4, profile.getEmail());
            preparedStatement.setInt(5, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUserById(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("DELETE FROM UserProfile WHERE id=?")) {

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UserProfile getUserByLogin(String login) {
        UserProfile userProfile = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT * FROM UserProfile WHERE login=?")) {

            preparedStatement.setString(1, login);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() == false) {
                return userProfile;
            }

            userProfile = new UserProfile();
            userProfile.setId(resultSet.getInt("id"));
            userProfile.setLogin(resultSet.getString("login"));
            userProfile.setRole(Role.valueOf(resultSet.getString("role")));
            userProfile.setName(resultSet.getString("name"));
            userProfile.setEmail(resultSet.getString("email"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userProfile;
    }

    public boolean checkIfLoginExists(String login) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT login FROM UserProfile WHERE login=?")) {

            preparedStatement.setString(1, login);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
