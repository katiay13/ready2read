package com.ready2read.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final Properties props = new Properties();

    // Static initializer runs once when the class is first loaded
    static {
        // Tomcat can't always auto-detect the MySQL driver, so we load it manually
        try {
            Class.forName("com.mysql.cj.jdbc.Driver", true,
                    Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found: " + e.getMessage());
        }

        // Load connection properties from db.properties on the classpath
        // Uses localhost defaults if the config file is missing (for local dev)
        try (InputStream is = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                props.load(is);
            } else {
                System.err.println("db.properties not found on classpath — using defaults");
                props.setProperty("db.url", "jdbc:mysql://localhost:3306/ready2read");
                props.setProperty("db.user", "root");
                props.setProperty("db.password", "");
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not load db.properties: " + e.getMessage());
        }
    }

    /**
     * Opens and returns a new physical JDBC connection to the database
     * Each call creates a new connection —> no connection pooling
     * Callers must close connection
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            props.getProperty("db.url"),
            props.getProperty("db.user"),
            props.getProperty("db.password")
        );
    }
}
