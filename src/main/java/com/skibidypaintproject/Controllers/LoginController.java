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

/**
 * Clase encargada de manejar la lógica de la vista de login
 */

public class LoginController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);
    @FXML
    private TextField usernameTextInput;
    @FXML
    private PasswordField passwordInput;

    private UserDAO userDAO = new UserDAO();



    /**
     * Método que se encarga de cambiar la vista a la de registro
     * @throws IOException Excepción en caso de que no se pueda cambiar la vista
     */
    @FXML
    private void switchToRegister() throws IOException {
        logger.info("Switching to register page");
        App.setRoot("register");
    }

    /**
     * Método que se encarga de realizar el login de un usuario
     * @throws IOException Excepción en caso de que no se pueda realizar el login
     */
    @FXML
    private void login() throws IOException {
        String username = usernameTextInput.getText();
        String password = passwordInput.getText();
        logger.info("Username: " + username + " trying to login");

        User user = userDAO.getUserByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            logger.info(user.toString() + " logged in");
            App.setRoot("planificador");
        } else {
            logger.warn("Failed login attempt: Wrong username or password");
            AlertUtil.showAlert("Error", "Usuario o contraseña incorrectos", usernameTextInput.getScene().getWindow());
        }
    }

}
