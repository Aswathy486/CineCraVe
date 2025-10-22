package com.cinecrave.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static final String URL = "jdbc:mysql://localhost:3306/CineCrazeDB";
    private static final String USER = "root"; 
    private static final String PASSWORD = "4863";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found! Ensure the JAR is in the classpath.");
            throw new SQLException("JDBC Driver not available.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

