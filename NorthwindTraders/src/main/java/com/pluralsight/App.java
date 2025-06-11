package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        //fire up the scanner for user input
        Scanner northwindScanner = new Scanner(System.in);

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

        // create the connection and prepared statement
        try (BasicDataSource dataSource = new BasicDataSource();) {

            // configure the datasource
            dataSource.setUrl("jdbc:mysql://localhost:3306/northwind");
            dataSource.setUsername(username);
            dataSource.setPassword(password);

            Connection connection = dataSource.getConnection();

            boolean appRunning = true;

            while (appRunning) {

                // menu
                System.out.println("\n🌟 What would you like to do? 🌟");
                System.out.println("1️⃣)  Display all 🛍️ products");
                System.out.println("2️⃣)  Display all 👥 customers");
                System.out.println("3️⃣)  Display all 🗂️ categories");
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
                    case "3":
                        System.out.println("\n🗂️ Showing all product categories:\n");
                        displayAllCategories(connection, northwindScanner);
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
        }
    }

    public static void displayAllProducts(Connection connection) {

        try (
                // start prepared statement
                // the statement is tied to the open connection
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT ProductID, ProductName, UnitPrice, UnitsInStock " +
                                "FROM products " +
                                "ORDER BY ProductName");

                // execute the query
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {

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

                // print out the data
                System.out.printf("   %-5d  %-35s $%-8.2f  %-5d%n", id, name, price, stock);

            }
        } catch (SQLException e) {
            // maybe print a nicer message to the user
            System.out.println("❌ Error fetching products: " + e.getMessage());
        }
    }

    public static void displayAllCustomers(Connection connection) {

        try (
                // start prepared statement
                // the statement is tied to the open connection
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT ContactName, CompanyName, City, Country, Phone " +
                                "FROM customers " +
                                "ORDER BY Country");

                // execute the query
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {

            // header for the results
            System.out.printf("🙋 %-22s 🏢 %-34s 🏙 %-13s 🌍 %-11s ☎️ %-15s%n",
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

                // print out the data
                System.out.printf("   %-25s %-37s %-16s %-15s %-15s%n", contact, company, city, country, phone);

            }
        } catch (SQLException e) {
            // maybe print a nicer message to the user
            System.out.println("❌ Error fetching customers: " + e.getMessage());
        }
    }

    public static void displayAllCategories(Connection connection, Scanner scanner) {

        try (
                // start prepared statement
                // the statement is tied to the open connection
                PreparedStatement categoryStatement = connection.prepareStatement(
                        "SELECT CategoryID, CategoryName " +
                                "FROM categories " +
                                "ORDER BY CategoryID");

                // execute the query
                ResultSet categoryResults = categoryStatement.executeQuery();
        ) {

            // header for the results
            System.out.printf("🆔 %-5s 🗂️ %-30s%n", "ID", "Category Name");
            System.out.println("═".repeat(50));

            // loop through the results
            while (categoryResults.next()) {

                // process the data
                int id = categoryResults.getInt("CategoryID");
                String name = categoryResults.getString("CategoryName");

                // print out the data
                System.out.printf("   %-5d    %-30s%n", id, name);

            }

            //prompt user for input and store it
            System.out.print("\n👉 Enter a Category ID to view its products: ");
            String input = scanner.nextLine().trim();

            try {
                int categoryId = Integer.parseInt(input);
                displayProductsByCategory(connection, categoryId);

            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid input. Please enter a numeric category ID.");
            }
        } catch (SQLException e) {
            // maybe print a nicer message to the user
            System.out.println("❌ Error fetching products: " + e.getMessage());
        }
    }

    public static void displayProductsByCategory(Connection connection, int categoryId) {
        try (
                // start prepared statement
                // the statement is tied to the open connection
                PreparedStatement productStatement = connection.prepareStatement(
                        "SELECT ProductID, ProductName, UnitPrice, UnitsInStock " +
                                "FROM products " +
                                "WHERE CategoryID = ? " +
                                "ORDER BY ProductName");
        ) {

            // set the parameters for the prepared statement
            productStatement.setInt(1, categoryId);

            // execute the query
            try (ResultSet productResults = productStatement.executeQuery()) {

                // header for the results
                System.out.printf("\n🆔 %-3s 📦 %-33s 💲%-6s 📦 %-6s%n",
                        "ID", "Name", "Price", "Stock");
                System.out.println("═".repeat(80));

                boolean found = false;

                // loop through the results
                while (productResults.next()) {
                    found = true;

                    // process the data
                    int pid = productResults.getInt("ProductID");
                    String pname = productResults.getString("ProductName");
                    double price = productResults.getDouble("UnitPrice");
                    int stock = productResults.getInt("UnitsInStock");

                    // print out the data
                    System.out.printf("   %-5d  %-35s $%-8.2f  %-5d%n", pid, pname, price, stock);
                }

                // user validation
                if (!found) {
                    System.out.println("🚫 No products found in this category.");
                }
            }
        } catch (SQLException e) {
            // maybe print a nicer message to the user
            System.out.println("❌ Error fetching products: " + e.getMessage());
        }
    }
}