package com.skibidypaintproject.Controllers;

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.skibidypaintproject.App;
import com.skibidypaintproject.Daos.UserDAO;
import com.skibidypaintproject.Entities.User;
import com.skibidypaintproject.Utils.AlertUtil;

public class LoginController {

    final Logger logger = LoggerFactory.getLogger(LoginController.class);

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

        User user = userDAO.getUserByUsername(username);
        logger.info("User: " + user);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            App.setRoot("planificador");
        } else {
            AlertUtil.showAlert("Error", "Usuario o contraseña incorrectos", usernameTextInput.getScene().getWindow());
        }
    }

}
