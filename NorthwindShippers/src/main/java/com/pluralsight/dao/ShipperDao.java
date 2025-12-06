package com.pluralsight.dao;

import com.pluralsight.models.Shipper;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShipperDao {

    // attributes
    private BasicDataSource dataSource;

    // constructor
    public ShipperDao(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    // add new shipper data (name and phone)
    public int insert(String name, String phone) {

        // this is a "try-with-resources" block
        // it ensures that the Connection, Statement, and ResultSet are closed automatically after we are done
        try (Connection conn = dataSource.getConnection();

             // start prepared statement - tied to the open connection
             PreparedStatement prepStatement = conn.prepareStatement("INSERT INTO shippers (CompanyName, Phone) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            // set parameters
            prepStatement.setString(1, name);
            prepStatement.setString(2, phone);

            // execute the update to the query - inserts a row to the db
            prepStatement.executeUpdate();

            // grab the auto generated id
            ResultSet keys = prepStatement.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }

        } catch (SQLException e) {
            // If something goes wrong (SQL error), print the stack trace to help debug.
            System.out.println("❌ Error inserting shipper: " + e.getMessage());
        }
        return -1;
    }

    // get all shippers
    public List<Shipper> getAll() {

        // create an empty list to hold the all the Shippers
        List<Shipper> shippers = new ArrayList<>();

        // this is a "try-with-resources" block
        // it ensures that the Connection, Statement, and ResultSet are closed automatically after we are done
        try (Connection conn = dataSource.getConnection();

             // start prepared statement - tied to the open connection
             PreparedStatement prepStatement = conn.prepareStatement("SELECT * FROM shippers");

             // execute the query
             ResultSet allResults = prepStatement.executeQuery()) {

            // loop through each row in the ResultSet
            while (allResults.next()) {

                // add and create the Shipper object to our list
                shippers.add(new Shipper(
                        allResults.getInt("ShipperID"),
                        allResults.getString("CompanyName"),
                        allResults.getString("Phone")));
            }

        } catch (SQLException e) {
            // if something goes wrong (SQL error), print the stack trace to help debug
            System.out.println("❌ Error fetching shippers: " + e.getMessage());
        }

        // return the list of Shippers
        return shippers;
    }

    // update a shippers phone number
    public void update(int id, String phone) {

        // this is a "try-with-resources" block
        // it ensures that the Connection, Statement, and ResultSet are closed automatically after we are done
        try (Connection conn = dataSource.getConnection();

             // start prepared statement - tied to the open connection
             PreparedStatement prepStatement = conn.prepareStatement("UPDATE shippers SET Phone = ? WHERE ShipperID = ?")) {

            // set parameters
            prepStatement.setString(1, phone);
            prepStatement.setInt(2, id);

            // execute the update to the query - updates a row in the db
            prepStatement.executeUpdate();

        } catch (SQLException e) {
            // If something goes wrong (SQL error), print the stack trace to help debug.
            System.out.println("❌ Error updating phone number: " + e.getMessage());
        }
    }

    // delete a shipper
    public void delete(int id) {

        // this is a "try-with-resources" block
        // it ensures that the Connection, Statement, and ResultSet are closed automatically after we are done
        try (Connection conn = dataSource.getConnection();

             // start prepared statement - tied to the open connection
             PreparedStatement prepStatement = conn.prepareStatement("DELETE FROM shippers WHERE ShipperID = ?")) {

            // set parameters
            prepStatement.setInt(1, id);

            // execute the update to the query - deletes a row in the db
            prepStatement.executeUpdate();

        } catch (SQLException e) {
            // If something goes wrong (SQL error), print the stack trace to help debug.
            System.out.println("❌ Error deleting shipper: " + e.getMessage());
        }
    }
}