package Entities;

import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * <h3>Appointment class</h3> <br/>
 * This class, {@code Appointment.java} is related to {@code client_schedule.appointments} which is the biggest table of the
 * database {@code client_schedule} (has the most columns)<br/>
 * The data that appears in the tables {@code Calendar.fxml} and {@code ThreeReportsView.fxml} depends on this
 */
public class Appointment {

    /**
     * {@code Appointment_ID} column in {@code client_schedule.appointments} table
     */
    public IntegerProperty id = new SimpleIntegerProperty();

    /**
     * {@code Title} column in {@code client_schedule.appointments} table.<br/>
     * The title of the appmt, eg. 'Meeting with john'
     */
    public StringProperty title;

    /**
     * {@code Description} column in {@code client_schedule.appointments} table<br/>
     * The description of the appmt, eg. 'First attempt at learning spanish'
     */
    public StringProperty desc;

    /**
     * {@code Location} column in {@code client_schedule.appointments} table<br/>
     * Where the appmt will be, eg. 'room 211'
     */
    public StringProperty location;

    /**
     * {@code Type} column in{@code client_schedule.appointments} table<br/>
     * The 'type' of the appmt, 'routine meeting', 'new employee'
     */
    public StringProperty type;

    /**
     * {@code Create_Date} column in {@code appointments} table<br/>
     * The time the appmt was created<br/>
     * Defined as a {@code DATETIME} type in the table
     */
    public ObjectProperty<LocalDateTime> created_time;
//    public LocalDateTime created_time;

    /**
     * {@code Last_Update} column in the {@code appointments} table<br/>
     * The time of day the appmt was most recently updated.<br/>
     * Defined as a {@code TIMESTAMP} type in the table
     */
    public ObjectProperty<LocalDateTime> last_updated_time;

    /**
     * {@code Start} column in the {@code appointments} table<br/>
     * The time the appmt starts.<br/>
     * Defined as a {@code DATETIME} type in the table
     */
    public ObjectProperty<LocalDateTime> start;

    /**
     * {@code End} column in the {@code appointments} table<br/>
     * The time in the day where the appmt ends<br/>
     * Defined as a {@code DATETIME} type in the table
     */
    public ObjectProperty<LocalDateTime> end;

    /**
     * {@code Create_Date} column in the {@code appointments} table<br/>
     * The person who created the appmt<br/>
     * Defined as a {@code VARCHAR(50)} type in the table
     */
    public StringProperty created_by_person;
//    public String created_by_person;

    /**
     * {@code Last_Updated_By} column in {@code appointments} table<br/>
     * The person who last updated this appmt<br/>
     * Defined as a {@code VARCHAR(50)} type in the table
     */
    public StringProperty last_updated_person;
//    public String last_updated_person;

    /**
     * {@code Customer_ID} column in the {@code appointments} table<br/>
     * The ID of the customer who made this appmt<br/>
     * Defined as a {@code INT(10)} type in the table
     */
    public IntegerProperty customer_id;
//    public int customer_id;

    /**
     * {@code Contact_ID} column in the {@code appointments} table<br/>
     * The ID of the contact who this appmt is<br/>
     * Defined as a {@code INT(10)} type in the table with
     */
    public IntegerProperty contact_id;
//    public int contact_id;

    /**
     * {@code User_ID} column in the {@code appointments} table<br/>
     * The ID of the logged-in user who created this appmt<br/>
     * Defined as a {@code INT(10)} type in the table
     */
//    public int user_id;
    public IntegerProperty user_id;

    /**
     * Defined as a {@code INT(10)} type in the table<br/>
     * The ID of the logged-in user who created this appmt
     */
    public StringProperty customer;

    /**
     * {@code User_ID} column in the {@code appointments} table<br/>
     * The ID of the logged-in user who created this appmt<br/>
     * Defined as a {@code INT(10)} type in the table
     */
    public StringProperty contact;
    /**
     * The name of the logged-in user who created this appmt
     */
    public StringProperty user;

    public Appointment() {
        this(-1, null, null, null, null, null, null, null,null, null, null,-1,-1,-1,null);
    }

    public Appointment(int id, String title, String desc, String location, String type, LocalDateTime start, LocalDateTime end, LocalDateTime created_time, String created_by_person, LocalDateTime last_updated_time, String last_updated_person, int customer_id,  int contact_id, int user_id, String contact) {

        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.desc = new SimpleStringProperty(desc);
        this.location = new SimpleStringProperty(location);
        this.type = new SimpleStringProperty(type);

        this.start = new SimpleObjectProperty<>(start);
        this.end = new SimpleObjectProperty<>(end);
        this.created_time = new SimpleObjectProperty<>(created_time);

        this.created_by_person = new SimpleStringProperty(created_by_person);

        this.last_updated_time = new SimpleObjectProperty<>(last_updated_time);
        this.last_updated_person = new SimpleStringProperty(last_updated_person);

        this.customer_id = new SimpleIntegerProperty(customer_id);

        this.contact_id = new SimpleIntegerProperty(contact_id);
        this.contact = new SimpleStringProperty(contact);

        this.user_id = new SimpleIntegerProperty(user_id);
    }

    /**
     * Getter for {@code start} in the {@code appointments} table<br/>
     * The starting time of the appointment, in the table<br/>
     * Its type is: {@code DATETIME}
     * @return start time
     */
    public LocalDateTime get_start_hour() {
        return start.get();
    }
    public ObjectProperty<LocalDateTime> get_start_hour_property() {
        return start;
    }

    /**
     * Getter for {@code end} in the {@code appointments} table<br/>
     * The ending time of the appointment<br/>
     * Type is: {@code DATETIME}
     * @return end time
     */
    public LocalDateTime get_end_hour() {
        return end.get();
    }
    public ObjectProperty<LocalDateTime> get_end_hour_property() {
        return end;
    }

    /**
     * Getter for {@code Create_Date} in the {@code appointments} table<br/>
     * The time the appointment was <i>created</i>, not the time it will occur at.<br/>
     * Its type is: {@code DATETIME}
     * @return time created
     */
    public LocalDateTime get_creation_time() {
        return created_time.get();
    }
    public ObjectProperty<LocalDateTime> get_creation_time_property() {
        return created_time;
    }

    /**
     * Getter for {@code Last_Update} in the {@code appointments} table<br/>
     * The time the appointment was most recently <i>updated</i>. by anyone<br/>
     * Its type is: {@code TIMESTAMP}
     * @return last time updated
     */
    public LocalDateTime get_most_recently_updated_time() {
        return last_updated_time.get();
    }

    public ObjectProperty<LocalDateTime> get_most_recently_updated_time_property() {
        return last_updated_time;
    }

    /**
     * Getter for {@code Title} in the {@code appointments} table<br/>
     * Title of the appointment<br/>
     * Its type is: {@code VARCHAR(50)}
     * @return title
     */
    public String get_appmt_title() {
        return title.get();
    }

    public StringProperty get_appmt_title_property(){
        return title;
    }

    /**
     * Getter for {@code Description} in the {@code appointments} table<br/>
     * A brief description of the appointment<br/>
     * Its type is: {@code VARCHAR(50)}<br/>
     * You have to keep it under 50 characters
     * @return brief appmt description
     */
    public String get_appmt_text() {
        return desc.get();
    }
    public StringProperty get_appmt_text_property() {
        return desc;
    }
    /**
     * Getter for {@code Location} in the {@code appointments} table<br/>
     * Location of the appointment<br/>
     * Its type is: {@code VARCHAR(50)}
     * @return place
     */
    public String get_appmt_location() {
        return location.get();
    }
    public StringProperty get_appmt_location_property() {
        return location;
    }

    /**
     * Getter for {@code Type} in the {@code appointments} table<br/>
     * Type of the appointment, ex. 'recurring weekly checkup'<br/>
     * Its type is: {@code VARCHAR(50)}
     * @return appmt type
     */
    public String get_appmt_type() {
        return type.get();
    }
    public StringProperty get_appmt_type_property() {
        return type;
    }

    /**
     * Getter for {@code Created_By} in the {@code appointments} table<br/>
     * Person who made the appmt<br/>
     * Its type is: {@code VARCHAR(50)}
     * @return creator
     */
    public String get_author_of_appointment() {
        return created_by_person.get();
    }
    public StringProperty get_author_of_appointment_property() {
        return created_by_person;
    }

    /**
     * Getter for {@code Last_Updated_By} in the {@code appointments} table<br/>
     * Person who last updated it<br/>
     * Its type is: {@code VARCHAR(50)}
     * @return person who last updated it
     */
    public String get_most_recently_updated_person() {
        return last_updated_person.get();
    }
    public StringProperty get_most_recently_updated_person_property() {
        return last_updated_person;
    }

    public String get_customer_name() {
        return customer.get();
    }

    public String get_contact_name() {
        return contact.get();
    }
    public StringProperty get_contact_name_property() {
        return contact;
    }

    public String get_appmt_user() {
        return user.get();
    }

    /**
     * Getter for {@code Appointment_ID} in the {@code appointments} table<br/>
     * It's the primary key.<br/>
     * Its type is: {@code INT(10)}
     * @return id
     */
    public int get_appmt_id() {
        return id.get();
    }

    public IntegerProperty get_appmt_id_property() {
        return id;
    }

    /**
     * Getter for {@code Contact_ID} in the {@code appointments} table<br/>
     * Foreign key.<br/>
     * Its type is: {@code INT(10)}
     * @return contact's id, 1,2, or 3
     */
    public int show_contact_id() {
        return contact_id.get();
    }

    /**
     * Getter for {@code User_ID} in the {@code appointments} table<br/>
     * Foreign key.<br/>
     * Its type is: {@code INT(10)}
     * @return user's id
     */
    public int get_user_id() {
        return user_id.get();
    }
    public IntegerProperty get_user_id_property() {
        return user_id;
    }

    /**
     * Getter for {@code Customer_ID} in the {@code appointments} table<br/>
     * Foreign key.<br/>
     * Its type is: {@code INT(10)}
     * @return customer's id
     */
    public int get_customer_code() {
        return customer_id.get();
    }
    /**
     *
     * @return customer's id as a property
     */
    public IntegerProperty get_customer_code_property() {
        return customer_id;
    }

    /** setter for {@code Customer_ID} column, INT(10)
     * @param customer_id customer's id
     */
    public void set_customer_code(int customer_id) {
        this.customer_id.set(customer_id);
    }

    /**
     * Setter for {@code Appointment_ID} column, INT(10)<br/>
     * This is the primary key
     * @param id id
     **/
    public void set_appmt_id(int id) {
        this.id.set(id);
    }

    /** Setter for {@code Contact_ID} column, INT(10)
     * @param contact_id contact's id
     */
    public void set_contact_code(int contact_id) {
        this.contact_id.set(contact_id);
    }

    /**
     * Setter for {@code User_ID} col, INT(10)
     * @param user_id user's id
     */
    public void set_appmt_user_id(int user_id) {
        this.user_id.set(user_id);
    }

    /**
     * Setter for {@code Title} column, varchar(50)
     *
     * @param title
     */
    public void set_appmt_title(String title) {
        this.title.set(title);
    }

    /** Setter for {@code Description}<br/>
     * varchar(50)
     * @param desc description
     * */
    public void set_appmt_text(String desc) {
        this.desc.set(desc);
    }

    /** Setter for {@code Location},<br/>
     * VARCHAR(50)
     * @param location the location
     * */
    public void set_appmt_location(String location) {
        this.location.set(location);
    }

    /** setter for {@code Type}<br/>
     * VARCHAR(50)
     * @param type type of the appmt
     * */
    public void set_appmt_type(String type) {
        this.type.set(type);
    }

    /**
     *
     * @param created_by_person created_by_person Created_By
     */
    public void set_author_of_appointment(String created_by_person) {
        this.created_by_person.set(created_by_person);
    }
    /**
     * start hour
     * @param start starting time
     */
    public void set_start_hour(LocalDateTime start) {
        this.start.set(start);
    }

    public void set_end_hour(LocalDateTime end) {
        this.end.set(end);
    }


    public void set_creation_time(LocalDateTime created_time) {
        this.created_time.set(created_time);
    }

    /**
     * Set the time the appmt was <i>last</i> updated.
     * @param last_updated_time same as Last_Updated column
     */
    public void set_most_recently_updated_time(LocalDateTime last_updated_time) {
        this.last_updated_time.set(last_updated_time);
    }

    /**
     * Sets the MRU (most recently used) user, the most recent {@code appmt_user} who updated the appointment
     * @param last_updated_person same as Last_Updated_By column
     */
    public void set_most_recently_updated_person(String last_updated_person) {
        this.last_updated_person.set(last_updated_person);
    }

    /**
     * Name of the customer
     * @param customer customer
     */
    public void set_customer_name(String customer) {
        this.customer.set(customer);
    }

    /** Name of the contact
     * @param contact contact
     */
    public void set_contact(String contact) {
        this.contact.set(contact);
    }

    /**
     * Name of the user, the person logged into the account.
     * @param user user
     */
    public void set_appmt_user(String user) {
        this.user.set(user);
    }
}