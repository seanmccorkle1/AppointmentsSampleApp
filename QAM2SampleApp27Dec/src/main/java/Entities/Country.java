package Entities;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** {@code client_schedule.countries} */
public class Country {

    private IntegerProperty id;
    private StringProperty country_name;

    public Country() {
        this.id = new SimpleIntegerProperty(-1);
        this.country_name = new SimpleStringProperty(null);
    }

    //    public Country(int id, String country_name) {
    public Country(int id, String country_name) {
        this.id = new SimpleIntegerProperty(id);
        this.country_name =new SimpleStringProperty(country_name);
    }

    /** Related to the {@code Country_ID} column in {@code client_schedule.countries}*/
    public int get_country_code() {
        return  id.get();
    }
    public String get_country_name() {
        return country_name.get();
    }

    /** Related to the {@code Country} column in {@code client_schedule.countries}*/
    public void set_country_name(String country_name) {
        this.country_name.set(country_name);
    }

    /** Sets the {@code Country_ID} column in {@code client_schedule.countries}*/
    public void set_country_code(int id) {
        this.id.set(id);
    }
}