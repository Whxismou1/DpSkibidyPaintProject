package com.skibidypaintproject.Controllers;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.mindrot.jbcrypt.BCrypt;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.skibidypaintproject.App;
import com.skibidypaintproject.Daos.UserDAO;
import com.skibidypaintproject.Entities.User;
import com.skibidypaintproject.Utils.AlertUtil;

public class LoginController {

    // final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private static final Logger logger = LogManager.getLogger(LoginController.class);
    @FXML
    private TextField usernameTextInput;
    @FXML
    private PasswordField passwordInput;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void switchToRegister() throws IOException {
        App.setRoot("register");
    }

    @FXML
    private void login() throws IOException {
        String username = usernameTextInput.getText();
        String password = passwordInput.getText();
        logger.info("Username: " + username + " trying to login");
        if (isInputSuspicious(username) || isInputSuspicious(password)) {
            logger.warn("Suspicious input detected");
            App.setRoot("dashboard");
            return;
        }

        User user = userDAO.getUserByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            logger.info(user.toString() + " logged in");
            App.setRoot("planificador");
        } else {
            logger.warn("Failed login attempt: Wrong username or password");
            AlertUtil.showAlert("Error", "Usuario o contraseña incorrectos", usernameTextInput.getScene().getWindow());
        }
    }

    private boolean isInputSuspicious(String input) {

        String suspiciousPattern = ".*[;''\"<>|].*";
        return input.matches(suspiciousPattern);
    }

}
