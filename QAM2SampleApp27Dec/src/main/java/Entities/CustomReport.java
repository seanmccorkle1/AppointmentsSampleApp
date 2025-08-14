package Entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Controls the second small table in the bottom-middle on {@code ThreeReportsView.fxml} <br/>
 * The one that groups appmts by state/division
 */
public class CustomReport {
    StringProperty state;
    //    StringProperty  name;
    IntegerProperty total;

    public CustomReport(String state, String name, int total) {
        this.state = new SimpleStringProperty(state);
        this.total = new SimpleIntegerProperty(total);
    }

    /**Getters*/
    public String get_state() {return state.get();}


    public int get_total() {return total.get();}
    /**Setters*/
    public void set_state(String state) {this.state.set(state);}
    //    public void set_person(String name) {this.name.set(name);}
    public void set_total (int total) {this.total.set(total);}

}