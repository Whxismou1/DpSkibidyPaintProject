package com.skibidypaintproject.Controllers;

import java.io.IOException;

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

        if (!password.equals(confirmPassword)) {
            AlertUtil.showAlert("Error", "Las contraseñas no coinciden", actualWIndow);
            return;
        }

        if (!PasswordValidator.isValid(password)) {
            AlertUtil.showAlert("Error",
                    "La contraseña debe tener al menos 8 caracteres, una letra mayúscula, una letra minúscula, un número y un carácter especial",
                    actualWIndow);
            return;
        }

        User newUser = new User(username, password);

        if (newUser != null && userDAO.insertUser(newUser)) {
            AlertUtil.showAlert("Éxito", "Usuario registrado correctamente", actualWIndow);
            App.setRoot("login");
        } else {
            AlertUtil.showAlert("Error", "No se pudo registrar el usuario", actualWIndow);
        }

    }

}
