package Entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** For the {@code client_schedule.first_level_divisions} table */
public class Division {

    /**
     * {@code Division_ID} col in  {@code first_level_divisions} table
     * <br/>
     *  It is defined as {@code INT(10)}
     */
    public IntegerProperty id;

    /**{@code Country_ID} col in  {@code first_level_divisions} table.
     * <br/>
     * It is defined as {@code INT(10)}
     */
    public IntegerProperty country_code;

    /**{@code Division} col in  {@code first_level_divisions} table
     * <br/>
     * It is defined as {@code VARCHAR(50)} in the db
     */
    public StringProperty subdivision_name;

    // each division has an an ID, as well as each country
    public Division(int id, String name, int country_code) {
        this.id = new SimpleIntegerProperty(id);
        this.country_code = new SimpleIntegerProperty(country_code);
        this.subdivision_name = new SimpleStringProperty(name);
    }

    /** Getter for {@code Division} col in {@code client_schedule.first_level_divisions} table*/
    public String get_division() {
        return subdivision_name.get();
    }

    /** Getter for {@code Division_ID} col in {@code client_schedule.first_level_divisions} table*/
    public int get_division_code() {
        return id.get();
    }

    /** Getter for {@code Country_ID} col in {@code client_schedule.first_level_divisions} table*/
    public int get_country_code() {
        return country_code.get();
    }

    public void set_country_name(String country_name) {
        this.subdivision_name.set(country_name);
    }

    public void set_division(int id) {
        this.id.set(id);
    }

    /**setter for {@code Division_ID}, the code corresponding to the state */
    public void set_division_code(int division_code) {
        this.country_code.set(division_code);
    }

}