package Utilities.Database;

import Entities.Customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * {@code customers} table sql functions <br/>
 * Implements the fns in {@code DataObject.java} in src/main/java/utilities
 */
public class CustomerObject implements DataObject<Customer> {

    public int c=3;

    /**
     * @param results
     * @return new {@code Customer} out of the resultset
     */
    public Customer show_customer(ResultSet results) throws SQLException {

        String cntry = results.getString("Country");
        int cntry_id = results.getInt("Country_ID");

        LocalDateTime created_time = results.getTimestamp("Create_Date").toLocalDateTime();
        LocalDateTime last_update_time = results.getTimestamp("Last_Update").toLocalDateTime();

        String person_who_created_it = results.getString("Created_By");
        String div_name = results.getString("Division");

        int div_code = results.getInt("Division_ID");
        int customer_id = results.getInt("Customer_ID");

        String person_who_updated_it = results.getString("Last_Updated_By");
        String person_name = results.getString("Customer_Name");
        String phone_num = results.getString("Phone");

        String zip_code = results.getString("Postal_Code");
        String address = results.getString("Address");


        return new Customer(
                customer_id, div_code, cntry_id, created_time, last_update_time, person_name, phone_num, address,
                zip_code, div_name, cntry, person_who_created_it,person_who_updated_it);

    }


    /**
     * @param id
     * @return
     */
    @Override
    public Optional<Customer> fetch(int id) {
        try (Connection connection = MySQLConnector.open_sql_connection()) {
            Statement statement = connection.createStatement();

            String cols= "customers.*, countries.Country, first_level_divisions.Country_ID, first_level_divisions.Division ";
            String tables=  "customers, countries, first_level_divisions ";

            String cond1= "first_level_divisions.Division_ID=customers.Division_ID ";
            String cond2 = "first_level_divisions.Country_ID=countries.Country_ID ";
            String cond3="customer.Customer_ID=";

            ResultSet results = statement.executeQuery(
                    "SELECT " + cols  + "FROM "  +tables+"WHERE "+ cond1 +"AND "+cond2+"AND "+cond3 + id);
            if (results.next()) {
                return Optional.of(show_customer(results));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public ObservableList<Customer> get_every() {
        try (Connection connection = MySQLConnector.open_sql_connection()) {
            Statement statement = connection.createStatement();
            //COUNTRY_ID?
            ResultSet results = statement.executeQuery("SELECT customers.*, first_level_divisions.Division, first_level_divisions.Country_ID, countries.Country FROM customers, first_level_divisions, countries WHERE customers.Division_ID=first_level_divisions.Division_ID AND first_level_divisions.Country_ID=countries.Country_ID");
            ObservableList<Customer> array_of_customers = FXCollections.observableArrayList();

            while (results.next()) {
                Customer customer = show_customer(results);
                array_of_customers.add(customer);
            }

            return array_of_customers;

        } catch (SQLException err) {
            err.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }
    public int get_max_id_customer() {
        try (Connection db_connect = MySQLConnector.open_sql_connection()) {
//            String query = "SELECT COUNT(*) FROM client_schedule.appointments";
            String query = "SELECT MAX(Customer_ID) AS highest_id FROM client_schedule.customers";

            try (PreparedStatement stmt = db_connect.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
//                System.out.println("rs: " + rs);

                if (rs.next()) {
                    int highest_id = rs.getInt("highest_id");
//                    System.out.println("HIGHEST CUST ID:  " + highest_id);
                    return highest_id;
                }
                else {
                    return 32;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 33;
    }
    /**
     *
     * @param cust the {@code Customer} to do the INSERT INTO statement on
     * @return boolean, a true/false flag
     */
    @Override
    public boolean insert(Customer cust) {

        int max=get_max_id_customer();
//        System.out.println("max cust id: " + max);

        try (Connection connection = MySQLConnector.open_sql_connection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO client_schedule.customers VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            c=max;
            c++; // one higher than the max ID, to keep it unique

            statement.setInt(1, c);
            statement.setString(2, cust.get_customer_name());
            statement.setString(3, cust.get_address());
            statement.setString(4, cust.get_zip_code());
            statement.setString(5, cust.get_phone());
            statement.setTimestamp(6, Timestamp.valueOf(cust.get_created_time()));
            statement.setString(7, cust.get_created_person());
            statement.setTimestamp(8, Timestamp.valueOf(cust.get_updated_time()));
            statement.setString(9, cust.get_updated_person());
            statement.setInt(10, cust.get_division_code());

            int flag = statement.executeUpdate();
            ResultSet results = statement.getGeneratedKeys();

            try (ResultSet keys= statement.getGeneratedKeys()) {
                if (keys.next()) {
                    int auto_generated_id = keys.getInt(1);

//                    System.out.println("Generated CUST ID: " + auto_generated_id);
                    cust.set_customer_id(auto_generated_id);
                }
            } catch (SQLException e){
                e.printStackTrace();
//                System.out.println("fail");
            }
//            if (result == 1 & results.next()) {
            if (flag > 0 && results.next()) {
                cust.set_customer_id(results.getInt(1));
                return true;
            }

        } catch (SQLException err) {
            err.printStackTrace();
        }

        return false;
    }

    /**
     * @param cust customer to update
     * @return true/false
     */
    @Override
    public boolean update(Customer cust) {

        try (Connection connection = MySQLConnector.open_sql_connection()) {

            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE customers SET Customer_Name=?, Address=?, Division_ID=?, Postal_Code=?, Phone=?, Create_Date=?, Created_By=?, Last_Update=?, Last_Updated_By=? WHERE Customer_ID=?");
            statement.setString(1, cust.get_customer_name());
            statement.setString(2, cust.get_address());

            statement.setInt(3, cust.get_division_code());
            statement.setString(4, cust.get_zip_code());
            statement.setString(5, cust.get_phone());

            statement.setTimestamp(6, Timestamp.valueOf(cust.get_created_time()));

            statement.setString(7, cust.get_created_person());

            statement.setTimestamp(8, Timestamp.valueOf(cust.get_updated_time()));

            statement.setString(9, cust.get_updated_person());
            statement.setInt(10, cust.get_customer_id());

            int result = statement.executeUpdate();

            if (result > 0)
                return true;

        } catch (SQLException err) {
            err.printStackTrace();
        }

        return false;
    }

    /**
     * @param id, id of the guy to delete
     * @return bool
     */
    @Override
    public boolean delete(int id) {

        try (Connection connection = MySQLConnector.open_sql_connection()) {
            Statement statement = connection.createStatement();

            int result = statement.executeUpdate("DELETE FROM customers WHERE Customer_ID=" + id);

            if (result >0)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}