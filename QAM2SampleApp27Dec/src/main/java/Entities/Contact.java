package Entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * <h3>Contact class </h3> <br/>
 * For the {@code client_schedule.contacts} table
 */
public class Contact {

    /**{@code Contact_ID} col*/
    public IntegerProperty id;

    /**{@code Contact_Name} col*/
    public StringProperty contact_name;

    /**{@code Email} col*/
    public StringProperty email;

    public Contact() {
        this.email = new SimpleStringProperty(null);
        this.id =  new SimpleIntegerProperty(-1);
        this.contact_name = new SimpleStringProperty(null);
    }

    /** new Contact(2, 'sean', hellotalk@gmail.com)*/
    public Contact(int id, String name, String email) {
        this.id = new SimpleIntegerProperty(id);
        this.contact_name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
    }

    /**Setters for the 3 columns in {@code client_schedule.contacts} table */

    /**{@code Contact_ID} col in the {@code contacts} table
     * 2nd col
     */
    public void set_contact_name(String contact_name) {
        this.contact_name.set(contact_name);
    }
    /**{@code Contact_ID} col in the {@code contacts} table
     * <br/>
     * 1st column
     */
    public void set_contact_id(int id) {
        this.id.set(id);
    }

    /**
     * {@code Email} col in the {@code contacts} table
     * <br/>
     * final column
     */
    public void set_contact_email(String email) {
        this.email.set(email);
    }

    /**
     * {@code Contact_ID} col in the {@code contacts} table
     * <br/>
     * 1st column
     */
    public int get_contact_id() {
        return id.get();
    }
    /**
     * {@code Contact_Name} col in the {@code contacts} table
     *
     * 2nd col
     */
    public String get_contact_name() {
        return contact_name.get();
    }

    /**
     * {@code Email} col in the {@code contacts} table
     * Final col
     */
    public String get_contact_email() {
        return email.get();
    }
}