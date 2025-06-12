package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        //fire up the scanner for user input
        Scanner sakilaScanner = new Scanner(System.in);

        // welcome message for user
        System.out.println("\t╔══════════════════════════════════════════════════╗");
        System.out.println("\t║        Welcome to The Sakila Film Studio!        ║");
        System.out.println("\t╚══════════════════════════════════════════════════╝");

        // make sure we passed in 2 arguments from the command line when we run the app
        // this is done with the app configuration in intellij
        if (args.length != 2) {
            System.out.println("❌ Please provide DB username and password: " + "java com.pluralsight.UsingDriverManager <username> <password>");
            System.exit(1);
        }

        // get the username, password, and url from the command line args
        String username = args[0];
        String password = args[1];

        // set up datasource and create the connection
        try (BasicDataSource dataSource = new BasicDataSource();) {

            // configure the datasource
            dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
            dataSource.setUsername(username);
            dataSource.setPassword(password);

            Connection connection = dataSource.getConnection();

            boolean appRunning = true;

            while (appRunning) {

                // display menu
                System.out.println("\n🌟 What would you like to do today, superstar? 🌟");
                System.out.println("1️⃣  ✨ Search actors by last name");
                System.out.println("2️⃣  🎥 Search films by actor's full name");
                System.out.println("0️⃣  ❌ Exit");
                System.out.print("👉 Choose an option: ");
                String choice = sakilaScanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        // ask the user for a last name of an actor they like
                        System.out.print("\n🔍 Enter the last name of an actor you like: ");
                        String lastName = sakilaScanner.nextLine().trim();

                        // use that to display a list of all actors with ^^ that last name
                        displayActorsByLastName(connection, lastName);
                        break;
                    case "2":
                        // ask the user to enter a first name and last name of an actor they want to see the movies of
                        System.out.print("\n🎬 Enter the full name of an actor to view their films (e.g. Alondra Gamez): ");
                        String[] fullName = sakilaScanner.nextLine().trim().split(" ");

                        // some user validation - if they don't enter a valid full name (2 words)
                        if (fullName.length != 2) {
                            System.out.println("\n⚠️ Please enter a valid first and last name.\n");
                        } else {
                            String firstName = fullName[0];
                            String last = fullName[1];

                            // use that to display a list of the films that actor has been in
                            displayFilmsByActor(connection, firstName, last);
                        }
                        break;
                    case "0":
                        System.out.println("\n👋 Bye bye, thanks for visiting Sakila Studio!");
                        appRunning = false;
                        break;
                    default:
                        // user validation
                        System.out.println("\n⚠️ Invalid option, please try again! 🙃");
                }
            }

            // catches any exceptions
        } catch (SQLException e) {
            // nice message to the user
            System.out.println("\n❌ Oopsie! Error connecting to database: " + e.getMessage() + "\n");
        }
    }

    // method to display actors with matching lastname
    public static void displayActorsByLastName(Connection connection, String lastName) {

        // start prepared statement
        // the statement is tied to the open connection
        // execute the query
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT actor_id, first_name, last_name " +
                        "FROM actor " +
                        "WHERE last_name = ?")) {

            // find the question mark by index and provide its safe value
            preparedStatement.setString(1, lastName);

            // execute the query
            try (ResultSet actorResults = preparedStatement.executeQuery()) {

                boolean found = false;

                // loop through the results
                while (actorResults.next()) {
                    if (!found) {

                        // header for the results
                        System.out.println("\n🎭 Matching actors:");
                        System.out.println("═".repeat(35));
                        found = true;
                    }

                    // process the data
                    int id = actorResults.getInt("actor_id");
                    String first = actorResults.getString("first_name");
                    String last = actorResults.getString("last_name");

                    // print out the data
                    System.out.printf("   🌟 [%d] %s %s%n", id, first, last);
                }

                // some user validation
                if (!found) {
                    System.out.println("\n🚫 No actors found with last name '" + lastName + "'.\n");
                }
            }
        } catch (SQLException e) {
            // maybe print a nicer message to the user
            System.out.println("\n❌ Error fetching actors: " + e.getMessage());
        }
    }

    // method to display films by actors full name
    public static void displayFilmsByActor(Connection connection, String firstName, String last) {

        // start prepared statement
        // the statement is tied to the open connection
        // execute the query
        try (PreparedStatement prepStatement = connection.prepareStatement(
                "SELECT f.title " +
                        "FROM film f " +
                        "JOIN film_actor fa ON f.film_id = fa.film_id " +
                        "JOIN actor a ON a.actor_id = fa.actor_id " +
                        "WHERE a.first_name = ? AND a.last_name = ?" +
                        "ORDER BY f.title")) {

            // find the question mark by index and provide its safe value
            prepStatement.setString(1, firstName);
            prepStatement.setString(2, last);

            // execute the query
            try (ResultSet filmResults = prepStatement.executeQuery()) {

                boolean found = false;

                // loop through the results
                while (filmResults.next()) {
                    if (!found) {

                        // header for the results
                        System.out.printf("\n🎬 Films starring %s %s:%n", firstName, last);
                        System.out.println("═".repeat(35));
                        found = true;
                    }

                    // process and print out the data
                    System.out.println("   🎞️ " + filmResults.getString("title"));
                }

                // some user validation
                if (!found) {
                    System.out.printf("\n🚫 No films found for %s %s. 😢\n", firstName, last);
                }
            }
        } catch (SQLException e) {
            // maybe print a nicer message to the user
            System.out.println("\n❌ Error fetching films: " + e.getMessage());
        }
    }
}