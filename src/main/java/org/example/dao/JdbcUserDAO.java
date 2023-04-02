package org.example.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.accounts.models.Role;
import org.example.accounts.models.UserProfile;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JdbcUserDAO implements AccountService{

    private final DataSource dataSource;

    public List<UserProfile> index() {
        UserProfile userProfile;
        List<UserProfile> usersList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM UserProfile");

            while (resultSet.next()) {
                userProfile = setUserProfileFieldsFromResultSet(resultSet);
                usersList.add(userProfile);
            }

        } catch (SQLException e) {
            log.error("Ошибка при попытке запроса списка пользователей из базы данных");
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
            log.error("Ошибка при попытке добавления пользователя в базу данных");
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

            userProfile = setUserProfileFieldsFromResultSet(resultSet);

        } catch (SQLException e) {
            log.error("Ошибка при попытке получения пользователя по ID из базы данных");
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
            log.error("Ошибка при попытке обновления пользователя в базе данных");
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
            log.error("Ошибка при попытке удаления пользователя из базы данных");
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
            if (!resultSet.next()) {
                return userProfile;
            }
            userProfile = setUserProfileFieldsFromResultSet(resultSet);

        } catch (SQLException e) {
            log.error("Ошибка при попытке получения пользователя по логину из базы данных");
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
            log.error("Ошибка при попытке проверки наличия логина в базе данных");
            e.printStackTrace();
        }
        return false;
    }

    private UserProfile setUserProfileFieldsFromResultSet(ResultSet resultSet) throws SQLException {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(resultSet.getInt("id"));
        userProfile.setLogin(resultSet.getString("login"));
        userProfile.setRole(Role.valueOf(resultSet.getString("role")));
        userProfile.setName(resultSet.getString("name"));
        userProfile.setEmail(resultSet.getString("email"));

        return userProfile;
    }
}
