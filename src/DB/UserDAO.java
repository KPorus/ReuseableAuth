package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import User_data.User;
import User_data.UserDetails;
import User_data.UserInfo;
import java.sql.Statement;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {

    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean isUserExists(String username) {
        String query = "SELECT * FROM users WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // If there is a result, username exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of an exception
        }
    }

    public boolean isEmailExists(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // If there is a result, email exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of an exception
        }
    }

    public boolean registerUser(String username, String email, String password, String role) {
        String insertUserQuery = "INSERT INTO users (name, email, pass, role) VALUES (?, ?, ?, ?)";
        String insertUserDetailsQuery = "INSERT INTO userDetails (user_id, address, phone) VALUES (?, NULL, NULL)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, role);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1); // Retrieve the generated user id

                    try (PreparedStatement userDetailsStatement = connection.prepareStatement(insertUserDetailsQuery)) {
                        userDetailsStatement.setInt(1, userId); // Set the user id
                        userDetailsStatement.executeUpdate();
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean loginUser(String username, String password) {
        String selectUserQuery = "SELECT * FROM users WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectUserQuery)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("pass");
                // Compare the hash of the entered password with the hash stored in the database
                if (BCrypt.checkpw(password, hashedPassword)) {
                    return true; // Authentication successful
                } else {
                    return false; // Authentication failed
                }
            } else {
                return false; // Username not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public UserInfo getUserInfo(String username) {
        String selectUserRoleQuery = "SELECT role,id FROM users WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectUserRoleQuery)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                int id = resultSet.getInt("id");
                return new UserInfo(id, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the role is not found
    }

    public User getUser(int Id) {
        String selectUserQuery = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectUserQuery)) {
            preparedStatement.setInt(1, Id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("pass");
                String role = resultSet.getString("role");

                // Create a User object and return it
                User user = new User(id, username, email, password, role);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the user is not found
    }

    public UserDetails getUserDetails(int user_id) {
        String selectUserQuery = "SELECT * FROM userDetails WHERE user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectUserQuery)) {
            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("user_id"); // Change variable name to avoid conflicts
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");

                // Create a UserDetails object and return it
                UserDetails userDetails = new UserDetails(id, userId, address, phone);
                return userDetails;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the user details are not found
    }

}
