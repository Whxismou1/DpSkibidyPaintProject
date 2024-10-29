package com.skibidypaintproject.Daos;

import com.skibidypaintproject.Controllers.BBDDController;
import com.skibidypaintproject.Entities.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {

    private final Connection connection;
    private static final Logger logger = LogManager.getLogger(UserDAO.class);

    public UserDAO() {
        this.connection = BBDDController.getInstance().getConnection();
    }

    public boolean insertUser(User user) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            String hashedPass = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPass);
            statement.setString(1, user.getUsername());
            statement.setString(2, hashedPass);
            logger.info("User " + user.getUsername() + " registered");
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error registering user " + user.getUsername());
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String password = resultSet.getString("password");
                logger.info("User " + username + " found");
                return new User(id, username, password);
            }
        } catch (SQLException e) {
            logger.error("Error getting user " + username);
            e.printStackTrace();
        }
        return null;
    }
}
