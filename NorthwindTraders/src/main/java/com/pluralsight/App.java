package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        // welcome message for user
        System.out.println("\t╔═════════════════════════════════════════════╗");
        System.out.println("\t║        Welcome to Northwind Traders!        ║");
        System.out.println("\t╚═════════════════════════════════════════════╝");

        // make sure we passed in 2 arguments from the command line when we run the app
        // this is done with the app configuration in intellij
        if (args.length != 2) {
            System.out.println("Application needs two arguments to run: " + "java com.pluralsight.UsingDriverManager <username> <password>");
            System.exit(1);
        }

        // get the username, password, and url from the command line args
        String username = args[0];
        String password = args[1];
        String url = "jdbc:mysql://localhost:3306/northwind";

        //fire up the scanner for user input
        Scanner northwindScanner = new Scanner(System.in);

        // establish the variables with null outside the try scope
        Connection connection = null;

        try {
            // create the connection and prepared statement
            connection = DriverManager.getConnection(url, username, password);

            boolean appRunning = true;

            while (appRunning) {

                // menu
                System.out.println("\n🌟 What would you like to do? 🌟");
                System.out.println("1️⃣)  Display all 🛍️ products");
                System.out.println("2️⃣)  Display all 👥 customers");
                System.out.println("0️⃣)  Exit 🚪");

                //prompt user for input and store it
                System.out.print("👉 Select an option: ");
                String choice = northwindScanner.nextLine();

                switch (choice) {
                    case "1":
                        System.out.println("\n🛍️ Showing all fabulous products:\n");
                        displayAllProducts(connection);
                        break;
                    case "2":
                        System.out.println("\n👥 Bringing you the coolest customers:\n");
                        displayAllCustomers(connection);
                        break;
                    case "0":
                        System.out.println("\n👋 Bye bye, thanks for stopping by! 💖\n");
                        appRunning = false;
                        break;
                    default:
                        System.out.println("\n⚠️ Invalid option. Please try again.\n");
                }
            }

        } catch (SQLException e) {
            // maybe print a nicer message to the user
            System.out.println("\n❌ Error connecting to database: " + e.getMessage() + "\n");
        } finally {
            // close the resources
            // in the opposite order you opened them
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("🔒 Connection closed.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void displayAllProducts(Connection connection) {

        //PreparedStatement preparedStatement = null;
        //ResultSet resultSet = null;

        try {
            // start prepared statement
            // the statement is tied to the open connection
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT ProductID, ProductName, UnitPrice, UnitsInStock " +
                            "FROM products " +
                            "ORDER BY ProductName");

            // execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // header for the results
            System.out.printf("🆔 %-3s 📦 %-33s 💲%-6s 📦 %-6s%n",
                    "ID", "Name", "Price", "Stock");
            System.out.println("═".repeat(80));

            // loop through the results
            while (resultSet.next()) {

                // process the data
                int id = resultSet.getInt("ProductID");
                String name = resultSet.getString("ProductName");
                double price = resultSet.getDouble("UnitPrice");
                int stock = resultSet.getInt("UnitsInStock");

                System.out.printf("   %-5d  %-35s $%-8.2f  %-5d%n", id, name, price, stock);

            }
        } catch (SQLException e) {
            // maybe print a nicer message to the user
            System.out.println("❌ Error fetching products: " + e.getMessage());
        }
    }

    public static void displayAllCustomers(Connection connection) {

        // PreparedStatement preparedStatement = null;
        // ResultSet resultSet = null;

        try {
            // start prepared statement
            // the statement is tied to the open connection
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT ContactName, CompanyName, City, Country, Phone " +
                            "FROM customers " +
                            "ORDER BY Country");

            // execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // header for the results
            System.out.printf("\uD83D\uDE4B %-22s \uD83C\uDFE2 %-34s \uD83C\uDFD9 %-13s \uD83C\uDF0D %-11s ☎\uFE0F %-15s%n",
                    "Contact", "Company", "City", "Country", "Phone");
            System.out.println("═".repeat(120));

            // loop through the results
            while (resultSet.next()) {

                // process the data
                String contact = resultSet.getString("ContactName");
                String company = resultSet.getString("CompanyName");
                String city = resultSet.getString("City");
                String country = resultSet.getString("Country");
                String phone = resultSet.getString("Phone");

                System.out.printf("   %-25s %-37s %-16s %-15s %-15s%n", contact, company, city, country, phone);

            }
        } catch (SQLException e) {
            // maybe print a nicer message to the user
            System.out.println("❌ Error fetching customers: " + e.getMessage());
        }
    }
}