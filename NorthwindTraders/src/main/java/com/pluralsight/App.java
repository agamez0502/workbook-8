package com.pluralsight;

import java.sql.*;

public class App {

    public static void main(String[] args) {

        System.out.println("╔══════════════╗");
        System.out.println("║   Products   ║");
        System.out.println("╚══════════════╝");

        try {

            // 1. open a connection to the database
            // use the database URL to point to the correct database
            Connection connection;
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", "root", "L@l@t!n@5");

            // create statement
            // the statement is tied to the open connection
            Statement statement = connection.createStatement();

            // define your query
            String query = "SELECT ProductName FROM products";


            // 2. Execute your query
            ResultSet results = statement.executeQuery(query);

            // process the results
            while (results.next()) {
                String city = results.getString("ProductName");
                System.out.println(city);
            }

            // 3. Close the connection
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}