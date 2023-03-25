package org.example.accounts;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    private static final String URL = "jdbc:postgresql://localhost:5432/users";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "1234";
    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<UserProfile> index() {
        List<UserProfile> usersList = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM UserProfile");

            while (resultSet.next()) {
                UserProfile userProfile = new UserProfile();

                userProfile.setId(resultSet.getInt("id"));
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
        int id = profile.hashCode();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO UserProfile VALUES(?, ?, ?)")) {

            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, profile.getName());
            preparedStatement.setString(3, profile.getEmail());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public UserProfile getUserById(int id) {
        UserProfile userProfile = null;

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT * FROM UserProfile WHERE id=?")) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            userProfile = new UserProfile();
            userProfile.setId(resultSet.getInt("id"));
            userProfile.setName(resultSet.getString("name"));
            userProfile.setEmail(resultSet.getString("email"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userProfile;
    }

    public void updateUser(int id, UserProfile profile) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("UPDATE UserProfile SET name=?, email=? WHERE id=?")) {

            preparedStatement.setString(1, profile.getName());
            preparedStatement.setString(2, profile.getEmail());
            preparedStatement.setInt(3, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUserById(int id) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("DELETE FROM UserProfile WHERE id=?")) {

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
