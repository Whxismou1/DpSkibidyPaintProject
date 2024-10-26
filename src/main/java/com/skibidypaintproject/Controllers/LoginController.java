package com.skibidypaintproject.Controllers;

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.skibidypaintproject.App;
import com.skibidypaintproject.Daos.UserDAO;
import com.skibidypaintproject.Entities.User;
import com.skibidypaintproject.Utils.AlertUtil;

public class LoginController {

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

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            App.setRoot("dashboard");
        } else {
            AlertUtil.showAlert("Error", "Usuario o contraseña incorrectos", usernameTextInput.getScene().getWindow());
        }
    }

}
