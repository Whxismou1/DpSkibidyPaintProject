package com.skibidypaintproject.Controllers;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que se encarga de la conexión y gestión a la base de datos
 * 
 * @version 1.0
 */

public final class BBDDController {
    private Dotenv dotenv = Dotenv.configure()
            .ignoreIfMalformed()
            .ignoreIfMissing()
            .load();

    private String URL = dotenv.get("DB_URL");
    private String USER = dotenv.get("USER_DB");
    private String PASSWORD = dotenv.get("PASS_DB");
    private static BBDDController bbddInstance;
    private Connection connectionBBDD;

    /**
     * Constructor privado de la clase
     * Se encarga de establecer la conexión con la base de datos
     * 
     * @throws RuntimeException si no se puede establecer la conexión
     */
    private BBDDController() {
        try {
            connectionBBDD = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que devuelve la instancia de la clase
     * 
     * @return instancia de la clase
     */
    public static BBDDController getInstance() {
        if (bbddInstance == null) {
            synchronized (BBDDController.class) {
                if (bbddInstance == null) {
                    bbddInstance = new BBDDController();
                }
            }
        }
        return bbddInstance;
    }

    /**
     * Método que devuelve la conexión a la base de datos
     * 
     * @return conexión a la base de datos
     */
    public Connection getConnection() {
        return connectionBBDD;
    }

    /**
     * Método que cierra la conexión a la base de datos
     * 
     * @throws RuntimeException si no se puede cerrar la conexión
     */
    public void closeConnection() {
        if (connectionBBDD != null) {
            try {
                connectionBBDD.close();
            } catch (SQLException e) {
                e.printStackTrace();

                throw new RuntimeException("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

}
