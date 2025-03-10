package org.currencyexchange.database.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseUtils {
    private static final String DB_URL = "jdbc:sqlite:C:/workspace/currencyExchange/src/main/resources/mydb.db";

    private DataBaseUtils() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}