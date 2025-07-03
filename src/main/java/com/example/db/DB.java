package com.example.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.Properties;

public class DB {

    public static Connection connection = null;

    public static Connection getConnection() {
        if (connection != null) return connection;

        try {
            Properties properties = DB.getProperties();

            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String url = properties.getProperty("url");

            DB.connection = DriverManager.getConnection(url, user, password);
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }

        if (connection != null) {
            System.out.println("--Connected to database successfully");
        }

        return DB.connection;
    }

    public static void closeConnection() {
        if (connection == null) return;

        try {
            connection.close();
            System.out.println("--Connection closed successfully");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeStatement(Statement stmt) {
        if (stmt == null) return;
        try {
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs == null) return;
        try {
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Properties getProperties() {
        Properties prop = new Properties();

        try (BufferedReader br = new BufferedReader(new FileReader("db.properties"))) {
            prop.load(br);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return prop;
    }

}
