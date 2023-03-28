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
                     "INSERT INTO UserProfile(role, name, email) VALUES(?, ?, ?)")) {

            preparedStatement.setString(1, profile.getRole().toString());
            preparedStatement.setString(2, profile.getName());
            preparedStatement.setString(3, profile.getEmail());

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
                     connection.prepareStatement("UPDATE UserProfile SET role=?, name=?, email=? WHERE id=?")) {

            preparedStatement.setString(1, profile.getRole().toString());
            preparedStatement.setString(2, profile.getName());
            preparedStatement.setString(3, profile.getEmail());
            preparedStatement.setInt(4, id);

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

    public UserProfile getUserByName(String name) {
        UserProfile userProfile = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT * FROM UserProfile WHERE name=?")) {

            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() == false) {
                return userProfile;
            }

            userProfile = new UserProfile();
            userProfile.setId(resultSet.getInt("id"));
            userProfile.setRole(Role.valueOf(resultSet.getString("role")));
            userProfile.setName(resultSet.getString("name"));
            userProfile.setEmail(resultSet.getString("email"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userProfile;
    }
}
