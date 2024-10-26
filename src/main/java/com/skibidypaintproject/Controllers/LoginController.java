package com.skibidypaintproject.Controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.skibidypaintproject.App;

public class LoginController {

    @FXML
    private TextField usernameTextInput;
    @FXML
    private PasswordField passwordInput;

    @FXML
    private void switchToRegister() throws IOException {
        App.setRoot("register");
    }

    @FXML
    private void login() throws IOException {
        String username = usernameTextInput.getText();
        String password = passwordInput.getText();

        System.out.println("Usuario: " + username);
        System.out.println("Contraseña: " + password);
    }

}
