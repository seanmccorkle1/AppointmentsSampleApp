package Entities;
import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * <h3>Customer class</h3> <br/>
 * Corresponds to the {@code customers} table.<br/>
 *  Statements that pull customer-related stuff from the db is in {@code CustomerObject.java}
 */
public class Customer {


    /** {@code Customer_ID} column in {@code client_schedule.customers} */
    public IntegerProperty customer_id;

    /** {@code Division_ID} column in {@code client_schedule.customers} */
    public IntegerProperty division_code;

    /** {@code Country_ID} column in {@code client_schedule.customers} */
    public IntegerProperty country_code;

    /** {@code Create_Date} column in {@code client_schedule.customers} */
    public ObjectProperty<LocalDateTime> time_created;

    /** {@code Last_Update} column in {@code client_schedule.customers} */
    public ObjectProperty<LocalDateTime> time_updated;

    /** {@code Customer_Name} column in {@code client_schedule.customers} */
    public StringProperty name;

    /** {@code Address} column in {@code client_schedule.customers} */
    public StringProperty home_address;

    /** {@code Postal_Code} column in {@code client_schedule.customers} */
    public StringProperty zip_code;

    /** {@code Phone} column in {@code client_schedule.customers} */
    public StringProperty phone_num;

    /** {@code Created_By} column in {@code client_schedule.customers} */
    public StringProperty person_who_created;

    /** {@code Last_Updated_By} column in {@code client_schedule.customers} */
    public StringProperty person_who_updated;

    private StringProperty country;
    private StringProperty division;


    public Customer() {
        this(-1, -1, -1, null, null, null, null, null,null, null,null, null, null);
    }

    public Customer(int customer_id, int division_code, int country_code, LocalDateTime time_created, LocalDateTime time_updated,String name, String phone_num, String home_address, String zip_code, String division, String country,String person_who_created, String person_who_updated) {

        this.country = new SimpleStringProperty(country);
        this.country_code = new SimpleIntegerProperty(country_code);
        this.time_created = new SimpleObjectProperty<LocalDateTime>(time_created);

        this.person_who_created = new SimpleStringProperty(person_who_created);
        this.division=new SimpleStringProperty(division);
        this.division_code = new SimpleIntegerProperty(division_code);
        this.customer_id = new SimpleIntegerProperty(customer_id);
        this.time_updated = new SimpleObjectProperty<LocalDateTime>(time_updated);
        this.person_who_updated =new SimpleStringProperty(person_who_updated);

        this.name = new SimpleStringProperty(name);

        this.phone_num = new SimpleStringProperty(phone_num);
        this.zip_code = new SimpleStringProperty(zip_code);
        this.home_address = new SimpleStringProperty(home_address);
    }

    public void set_country_code(int country_code) {
        this.country_code.set(country_code);
    }
    public void set_division_code(int division_code) {
        this.division_code.set(division_code);
    }
    public void set_customer_id(int customer_id) {
        this.customer_id.set(customer_id);
    }


    /**
     * {@code Create_Date} col in {@code customers} table
     */
    public void set_created_time(LocalDateTime time_created) {
        this.time_created.set(time_created);
    }

    /**
     * {@code Last_Update} col in {@code customers} table
     */
    public void set_updated_time(LocalDateTime time_updated) {
        this.time_updated.set(time_updated);
    }

    /**
     * {@code Customer_Name} col in {@code customers} table
     */
    public void set_customer_name(String name) {
        this.name.set(name);
    }

    /**
     * {@code Phone} col in {@code customers} table
     */
    public void set_phone(String phone_num) {
        this.phone_num.set(phone_num);
    }

    /**
     * {@code Postal_Code} col in {@code customers} table
     */
    public void set_zip_code(String zip_code) {
        this.zip_code.set(zip_code);
    }

    /**
     * {@code Address} col in {@code customers} table
     */
    public void set_address(String home_address) {
        this.home_address.set(home_address);
    }

    public void set_country(String country) {
        this.country.set(country);
    }


    /**
     * {@code Created_By} col in {@code customers} table
     */
    public void set_created_person(String person_who_created) {
        this.person_who_created.set(person_who_created);
    }

    public void set_division(String division) {
        this.division.set(division);
    }

    /**
     * {@code Last_Updated_By} col in {@code customers} table
     */
    public void set_updated_person(String person_who_updated) {
        this.person_who_updated.set(person_who_updated);
    }

    // GETTERS
    public LocalDateTime get_updated_time() {
        return time_updated.get();
    }
    public ObjectProperty<LocalDateTime> get_updated_time_property() {
        return time_updated;
    }

    public LocalDateTime get_created_time() {
        LocalDateTime tc= time_created.get();

        if (tc != null){
            return tc;
        }
        return LocalDateTime.now();
    }

    public ObjectProperty<LocalDateTime> get_created_time_property() {
        return time_created;
    }

    public int get_country_code() {
        return country_code.get();
    }

    public int get_division_code() {
        return division_code.get();
    }
    public IntegerProperty get_division_code_property() {
        return division_code;
    }

    /**
     * @return customer's {@code customer_id} as an {@code int}
     */
    public int get_customer_id() {
        return customer_id.get();
    }
    public IntegerProperty get_customer_id_property() {
        return customer_id;
    }

    /**
     * @return customer's {@code Address} as an {@code String}
     */
    public String get_address() {
        return home_address.get();
    }
    public StringProperty get_address_property() {
        return home_address;
    }

    /**
     * @return customer's {@code Address} as an {@code String}
     */
    public String get_country() {
        return country.get();
    }

    public StringProperty get_country_property() {
        return country;
    }

    public String get_division() {
        return division.get();
    }
    public StringProperty get_division_property() {
        return division;
    }

    /**
     * @return creator of the entry as an {@code String}
     */
    public String get_created_person() {
        return person_who_created.get();
    }

    /**
     * @return updater of the entry as an {@code String}
     */
    public String get_updated_person() {
        return person_who_updated.get();
    }
    public StringProperty get_updated_person_property() {
        return person_who_updated;
    }

    /**
     * @return Name of the customer as an {@code String}
     */
    public String get_customer_name() {
        return name.get();
    }

    public StringProperty get_customer_name_property(){
        return name;
    }
    /**
     * @return Phone# as {@code String}
     */
    public String get_phone() {
        return phone_num.get();
    }
    public StringProperty get_phone_property() {
        return phone_num;
    }
    /**
     * @return  zip code as {@code String}
     */
    public String get_zip_code() {
        return zip_code.get();
    }
    public StringProperty get_zip_code_property(){
        return zip_code;
    }
}