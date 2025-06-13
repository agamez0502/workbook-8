package com.pluralsight;

import com.pluralsight.dao.DataManager;
import com.pluralsight.models.Actor;
import com.pluralsight.models.Film;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.List;
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

            DataManager dataManager = new DataManager(dataSource);

            try (Connection connection = dataSource.getConnection()) {
                boolean appRunning = true;

                while (appRunning) {

                    // display menu
                    System.out.println("\n🌟 What would you like to do today, superstar? 🌟");
                    System.out.println("1️⃣  ✨ Search actors by full name");
                    System.out.println("2️⃣  🎥 Search films by actor ID");
                    System.out.println("0️⃣  ❌ Exit");
                    System.out.print("👉 Choose an option: ");
                    String choice = sakilaScanner.nextLine().trim();

                    switch (choice) {

                        case "1":
                            // ask the user to search for actor by name
                            System.out.print("\n🔍 Enter the full name of an actor (e.g. Alondra Gamez): ");
                            String[] name = sakilaScanner.nextLine().trim().split(" ");

                            // user validation - if they don't enter a valid full name (2 words)
                            if (name.length != 2) {
                                System.out.println("\n⚠️ Please enter a valid first and last name.\n");
                                break;
                            }

                            String firstName = name[0];
                            String lastName = name[1];

                            // display list of actors
                            List<Actor> actors = dataManager.displayActorsByFullName(connection, firstName, lastName);

                            if (actors.isEmpty()) {

                                // user validation
                                System.out.println("\n🚫 No actors found.\n");
                            } else {

                                // header for the results
                                System.out.println("\n🎭 Matching actors:");
                                System.out.println("═".repeat(35));
                                for (Actor actor : actors) {

                                    // print out the data
                                    System.out.printf("   🌟 [%d] %s %s%n",
                                            actor.getActor_id(),
                                            actor.getFirst_name(),
                                            actor.getLast_name());
                                }
                            }
                            break;

                        case "2":
                            // ask the user to enter an actor ID
                            System.out.print("\n🎬 Enter the actor ID to view their films: ");

                            try {
                                int actorID = Integer.parseInt(sakilaScanner.nextLine());
                                List<Film> films = dataManager.displayFilmsByActorId(connection, actorID);

                                if (films.isEmpty()) {

                                    // user validation
                                    System.out.printf("\n🚫 No films found.\n");
                                } else {

                                    // header for the results
                                    System.out.printf("\n🎬 Films by Actor ID %d:%n", actorID);
                                    System.out.println("═".repeat(50));
                                    for (Film film : films) {

                                        // print out the data
                                        System.out.printf("🎞️  %s (%d) — %d mins\n   📖 %s\n\n",
                                                film.getTitle(),
                                                film.getRelease_year(),
                                                film.getLength(),
                                                film.getDescription());
                                    }
                                }

                            } catch (NumberFormatException e) {
                                System.out.println("\n⚠️ Invalid actor ID. Please enter a number.\n");
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
            }
        } catch (SQLException e) {

            // nice message to the user
            System.out.println("\n❌ Oopsie! Error connecting to database: " + e.getMessage() + "\n");
        }
    }
}