package com.skibidypaintproject.Controllers;

import io.github.cdimascio.dotenv.Dotenv;

public final class BBDDController {

    private Dotenv dotenv = Dotenv.load();
    private String URL = dotenv.get("DB_URL");
    private String USER = dotenv.get("USER_DB");
    private String PASSWORD = dotenv.get("PASS_DB");
    private static BBDDController bbddInstance;

    private BBDDController() {
    }

    public static BBDDController getInstance() {
        if (bbddInstance == null) {
            bbddInstance = new BBDDController();
        }
        return bbddInstance;
    }

}
