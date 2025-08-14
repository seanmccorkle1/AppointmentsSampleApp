package Utilities.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import Entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**<h3> Interface for 'User' </h3> <br/>
 * This uses the {@code client_schedule.users} table (related to logging in)<br/>
 * the DB only comes with 2 users,<br/>
 * {@code script} and {@code admin}<br/>
 * {@code admin} login info is this:<br/>
 * username: admin<br/>
 * password: admin
 */
public class UserObject implements DataObject<User>{

    /** Get the user's info from the table {@code client_schedule.users} */
    private User fetch(ResultSet user_results) throws SQLException {
        return new User(user_results.getInt("User_ID"), user_results.getString("User_Name"));
    }

    /**
     *
     * @param user The given username in the
     * @param pass The given password
     * @return Optional, it depends on whether the given credentials exist in the DB
     */
    public Optional<User> try_credentials(String user, String pass) {

        try (Connection db_connect = MySQLConnector.open_sql_connection()){

            String col1="User_ID, ";
            String col2="User_Name ";

            String table = "users ";
            String cond1="User_Name=? ";
            String cond2 ="Password=?";

            PreparedStatement sql_statement = db_connect.prepareStatement(
                    "SELECT " + col1+col2+"FROM " + table+"WHERE "+ cond1+"AND "+ cond2);

            sql_statement.setString(1, user);
            sql_statement.setString(2, pass);

            ResultSet single_result = sql_statement.executeQuery();

            if(single_result.next()) {
                return Optional.of(fetch(single_result));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * @param id
     * @return the 1 user that matches the WHERE condition
     */
    @Override
    public Optional<User> fetch(int id) {

        try(Connection db_connection = MySQLConnector.open_sql_connection()){

            Statement sql_statement =db_connection.createStatement();

            String col1="User_ID, ";
            String col2="User_Name ";
            String table = "users ";
            String cond = "id=";

            ResultSet user_result = sql_statement.executeQuery(
                    "SELECT " + col1+col2+"FROM "+table+"WHERE "+cond+id);

            if (user_result.next()) {
                return Optional.of(fetch(user_result));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * show the 2 users
     * @return List of the 2 users
     */
    @Override
    public ObservableList<User> get_every() {
        try(Connection db_connection = MySQLConnector.open_sql_connection()){

            Statement sql_statement = db_connection.createStatement();

            String col1="User_ID, ";
            String col2="User_Name ";
            String table="users";

            ResultSet user_results = sql_statement.executeQuery(
                    "SELECT " + col1+col2+"FROM " + table);

            ObservableList<User> users = FXCollections.observableArrayList();

            while(user_results.next()) {
                User user = fetch(user_results);
                users.add(user);
            }

            return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }

    @Override
    public boolean insert(User u) {
        return false;
    }

    @Override
    public boolean update(User u) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

}