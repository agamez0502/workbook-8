package com.pluralsight;

import com.pluralsight.dao.ShipperDao;
import com.pluralsight.models.Shipper;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        // fire up scanner to allow user input
        Scanner shipperScanner = new Scanner(System.in);

        // welcome message for user
        System.out.println("╔════════════════════════════╗");
        System.out.println("║   Northwind Shippers App   ║");
        System.out.println("╚════════════════════════════╝");

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
            dataSource.setUrl("jdbc:mysql://localhost:3306/northwind");
            dataSource.setUsername(username);
            dataSource.setPassword(password);

            Connection connection = dataSource.getConnection();

            ShipperDao dao = new ShipperDao(dataSource);

            // prompt user for new shipper data (name and phone)
            System.out.print("\n📦 Enter shipper name: ");
            String name = shipperScanner.nextLine().trim();

            System.out.print("📞 Enter phone number: ");
            String phone = shipperScanner.nextLine().trim();

            // insert it into the table
            int newId = dao.insert(name, phone);

            // display new shipper id when insert is complete
            if (newId > 0) {
                System.out.println("✅ New shipper added with ID: " + newId);
            } else {
                System.out.println("❌ Failed to insert new shipper.");
            }

            // display all the shippers
            System.out.println("\n📋 All Shippers:");
            displayShippers(dao);

            // prompt user to change the phone number of a shipper
            // user should enter id and the phone number
            System.out.print("\n✏️ Enter the ID of the shipper to update: ");
            int updateId = Integer.parseInt(shipperScanner.nextLine().trim());

            System.out.print("📞 Enter the new phone number: ");
            String newPhone = shipperScanner.nextLine().trim();

            // update the table
            dao.update(updateId, newPhone);
            System.out.println("✅ Phone number updated!");

            // display all the shippers
            System.out.println("\n📋 Updated Shippers:");
            displayShippers(dao);

            // prompt user to delete a shipper
            // DO NOT ENTER SHIPPERS 1-3
            System.out.print("\n🗑️ Enter the ID of the shipper to delete (NOT 1–3): ");
            int deleteId = Integer.parseInt(shipperScanner.nextLine().trim());

            // delete the new shipper just made
            if (deleteId >= 1 && deleteId <= 3) {
                System.out.println("⚠️ Cannot delete shippers with ID 1–3.");
            } else {
                dao.delete(deleteId);
                System.out.println("✅ Shipper deleted!");
            }

            // display all the shippers
            System.out.println("\n📋 Final Shippers List:");
            displayShippers(dao);

            // catches any exceptions
        } catch (SQLException e) {
            // nice message to the user
            System.out.println("\n❌ Oopsie! Error connecting to database: " + e.getMessage() + "\n");
        }
    }

    // helper method to print shippers all prettyyy
    private static void displayShippers(ShipperDao dao) {
        List<Shipper> shippers = dao.getAll();
        for (Shipper shipper : shippers) {
            System.out.printf("📦 ID: %-3d | Name: %-20s | Phone: %s%n",
                    shipper.getShipperId(),
                    shipper.getCompanyName(),
                    shipper.getPhone());
        }
    }
}