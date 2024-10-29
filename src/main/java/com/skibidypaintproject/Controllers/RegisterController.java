package com.skibidypaintproject.Controllers;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;

import com.skibidypaintproject.App;
import com.skibidypaintproject.Daos.UserDAO;
import com.skibidypaintproject.Entities.User;
import com.skibidypaintproject.Utils.AlertUtil;
import com.skibidypaintproject.Utils.PasswordValidator;

public class RegisterController {

    @FXML
    private TextField usernameTextInput;
    @FXML
    private PasswordField passwordInput;

    @FXML
    private PasswordField confirmPasswordInput;

    private UserDAO userDAO = new UserDAO();

    private static final Logger logger = LogManager.getLogger(RegisterController.class);

    @FXML
    private void backToLogin() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void register() throws IOException {
        String username = usernameTextInput.getText();
        String password = passwordInput.getText();
        String confirmPassword = confirmPasswordInput.getText();
        Window actualWIndow = usernameTextInput.getScene().getWindow();
        logger.info("Username: " + username + " trying to register");
        if (!password.equals(confirmPassword)) {
            AlertUtil.showAlert("Error", "Las contraseñas no coinciden", actualWIndow);
            logger.warn("Passwords do not match");
            return;
        }

        if (!PasswordValidator.isValid(password)) {
            AlertUtil.showAlert("Error",
                    "La contraseña debe tener al menos 8 caracteres, una letra mayúscula, una letra minúscula, un número y un carácter especial",
                    actualWIndow);
            logger.warn(
                    "Invalid password, must have at least 8 characters, one uppercase letter, one lowercase letter, one number and one special character");
            return;
        }

        User newUser = new User(username, password);

        if (newUser != null && userDAO.insertUser(newUser)) {
            AlertUtil.showAlert("Éxito", "Usuario registrado correctamente", actualWIndow);
            logger.info(newUser.toString() + " registered");
            App.setRoot("login");
        } else {
            AlertUtil.showAlert("Error", "No se pudo registrar el usuario", actualWIndow);
            logger.error("Error registering user " + username);
        }

    }

}
