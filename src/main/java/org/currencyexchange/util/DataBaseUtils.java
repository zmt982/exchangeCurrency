package org.currencyexchange.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DataBaseUtils {
    private static final String DB_URL = "jdbc:sqlite:C:/workspace/currencyExchange/src/main/resources/mydb.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver not found");
        }
    }

    private DataBaseUtils() {
        throw new UnsupportedOperationException("Not supported");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}