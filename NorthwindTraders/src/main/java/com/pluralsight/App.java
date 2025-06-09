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

            // this is like me opening MySQL Workbench and clicking localhost
            Connection connection;
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", "root", "L@l@t!n@5");

            // create statement
            // the statement is tied to the open connection

            // this is like me opening a new query window
            Statement statement = connection.createStatement();

            // define your query

            // this is like me typing the query in the new query windows
            String query = "SELECT ProductName FROM products";


            // 2. Execute your query

            // this is like me clicking the lightning bolt
            ResultSet results = statement.executeQuery(query);

            // process the results

            // this is a way to view the result set but java doesn't have a spreadsheet view for us
            while (results.next()) {
                String city = results.getString("ProductName");
                System.out.println(city);
            }

            // 3. Close the connection

            // this is like me closing MySQL Workbench
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}