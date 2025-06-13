package com.pluralsight.dao;

import com.pluralsight.models.*;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    // properties=================================================================================
    private BasicDataSource dataSource;

    // constructor================================================================================
    public DataManager(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    // methods====================================================================================

    // method to display actors with matching full name
    public List<Actor> displayActorsByFullName(Connection connection, String firstName, String lastName) {
        List<Actor> actors = new ArrayList<>();

        // start prepared statement
        // the statement is tied to the open connection
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT actor_id, first_name, last_name " +
                        "FROM actor " +
                        "WHERE first_name = ? AND last_name = ?")) {

            // find the question mark by index and provide its safe value
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);

            // execute the query
            try (ResultSet actorResults = preparedStatement.executeQuery()) {

                // loop through the results
                while (actorResults.next()) {

                    // process the data
                    int id = actorResults.getInt("actor_id");
                    String first = actorResults.getString("first_name");
                    String last = actorResults.getString("last_name");

                    actors.add(new Actor(id, first, last));
                }
            }
        } catch (SQLException e) {
            // maybe print a nicer message to the user
            System.out.println("\n❌ Error fetching actors: " + e.getMessage());
        }
        return actors;
    }

    // method to display films with matching actor id
    public List<Film> displayFilmsByActorId(Connection connection, int actorId) {
        List<Film> films = new ArrayList<>();

        // start prepared statement
        // the statement is tied to the open connection
        try (PreparedStatement prepStatement = connection.prepareStatement(
                "SELECT f.film_id, f.title, f.description, f.release_year, f.length " +
                        "FROM film f " +
                        "JOIN film_actor fa ON f.film_id = fa.film_id " +
                        "WHERE fa.actor_id = ? " +
                        "ORDER BY f.title")) {

            // find the question mark by index and provide its safe value
            prepStatement.setInt(1, actorId);

            // execute the query
            try (ResultSet filmResults = prepStatement.executeQuery()) {
                ;

                // loop through the results
                while (filmResults.next()) {
                    films.add(new Film(
                            filmResults.getInt("film_id"),
                            filmResults.getString("title"),
                            filmResults.getString("description"),
                            filmResults.getInt("release_year"),
                            filmResults.getInt("length")
                    ));
                }
            }
        } catch (SQLException e) {
            // maybe print a nicer message to the user
            System.out.println("\n❌ Error fetching films: " + e.getMessage());
        }
        return films;
    }
}