package com.pluralsight;

import java.sql.*;

public class App {

    public static void main(String[] args) {

        // welcome message for user
        System.out.println("╔════════════════════════════╗");
        System.out.println("║   Northwind Products App   ║");
        System.out.println("╚════════════════════════════╝");

        // make sure we passed in 2 arguments from the command line when we run the app
        // this is done with the app configuration in intellij
        if (args.length != 2) {
            System.out.println("Application needs two arguments to run: " + "java com.pluralsight.UsingDriverManager <username> <password>");
            System.exit(1);
        }

        // get the username and password form the command line args
        String username = args[0];
        String password = args[1];

        try {

            // create a connection to the database and prepared statement
            // use the database URL to point to the correct database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", username, password);

            // start prepared statement
            // the statement is tied to the open connection
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products");

            // execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // header for the results
            System.out.printf("%-5s %-30s %-10s %-10s%n", "ID", "Name", "Price", "Stock");
            System.out.println("═════════════════════════════════════════════════════════════");

            // loop through the results
            while (resultSet.next()) {

                // process the data
                int id = resultSet.getInt("ProductID");
                String name = resultSet.getString("ProductName");
                double price = resultSet.getDouble("UnitPrice");
                int stock = resultSet.getInt("UnitsInStock");

                System.out.printf("%-5d %-35s %10s %-10d%n", id, name, String.format("$%.2f", price), stock);

            }

            // close the resources
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("❌ Error connecting to database: " + e.getMessage());
        }
    }
}