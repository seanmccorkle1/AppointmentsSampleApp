package Utilities.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import Entities.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * <h3>'Contact' interface</h3> <br/>
 * Pulls stuff from the {@code client_schedule.contacts} table
 */
public class ContactObject implements DataObject<Contact> {

    /**
     * @param results ResultSet of a specific contact
     * @return Contact, the 1 contact and his info
     */
    public Contact show_contact(ResultSet results) throws SQLException {

        int contact_code = results.getInt("Contact_ID");
//        int contact_code = results.getInt("Contact_ID");  Contact UNDERSCORE ID
        String contact_name = results.getString("Contact_Name");
        String email = results.getString("Email");

        return new Contact(contact_code, contact_name, email);
    }

    /**
     * @param id
     * @return a table of data showing the matched contact<br/>
     * ex. contact whose ID is 2, contact whose ID is 1,<br/>
     * At max this can only return 1 row because of the condition
     */
    @Override
    public Optional<Contact> fetch(int id) {
        Connection connection = MySQLConnector.open_sql_connection();
        try {
            Statement statement = connection.createStatement();
            ResultSet results =
                    statement.executeQuery(
                            "SELECT  * FROM client_schedule.contacts WHERE Contact_ID=" + id);

            if (results.next()) {
                return Optional.of(show_contact(results));
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * @return all 3 contacts in the sample table
     */
    @Override
    public ObservableList<Contact> get_every() {

        try (Connection connection = MySQLConnector.open_sql_connection()) {
            Statement statement = connection.createStatement();

            ResultSet all_contacts = statement.executeQuery("SELECT * FROM client_schedule.contacts");

            ObservableList<Contact> contacts = FXCollections.observableArrayList();

            while (all_contacts.next()) {
                contacts.add(show_contact(all_contacts));
            }

            return contacts;

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }

    @Override
    public boolean insert(Contact c) {
        return false;
    }

    @Override
    public boolean update(Contact c) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}