package Entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * {@code Report class} <br/>
 * For the 'reports' button, it shows 3 report tables
 */
public class Report {
    LongProperty date;
    StringProperty type;
    IntegerProperty total;

    public Report(long date, String type, int total) {
        this.date = new SimpleLongProperty(date);
        this.type = new SimpleStringProperty(type);
        this.total = new SimpleIntegerProperty(total);
    }

    public String get_type() {return type.get();}
    public long get_date() {return date.get();}
    public int get_total_num_of_appmts() {return total.get();}

    public void set_type(String type) {this.type.set(type);}
    public void set_date(long date) {this.date.set(date);}
    public void set_total_num_of_appmts(int total) {this.total.set(total);}
}