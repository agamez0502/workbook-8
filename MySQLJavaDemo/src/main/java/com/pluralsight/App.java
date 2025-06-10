package com.pluralsight;

import java.sql.*;

public class App {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        // making sure we passed in 2 arguments from the command line when we run the app
        // this is done with the app configuration in intellij
        if (args.length != 2) {
            System.out.println("Application needs two arguments to run: " + "java com.pluralsight.UsingDriverManager <username> <password>");
            System.exit(1);
        }

        // get the username and password from the command line args
        String username = args[0];
        String password = args[1];

        // create the connection and prepared statement
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", username, password);

        // start our prepared statement
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT ProductName FROM Products WHERE ProductID = ?");

        // find the question mark by index and provide its safe value
        preparedStatement.setInt(1, 14);

        // execute the query
        ResultSet resultSet = preparedStatement.executeQuery();

        // loop through the results
        while (resultSet.next()) {
            // process the data
            System.out.printf("productName = %s\n", resultSet.getString("ProductName"));
        }

        // close the resources
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}