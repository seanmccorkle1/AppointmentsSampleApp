package com.example.qam2sampleapp;

import Entities.Appointment;
import Entities.Contact;
import Entities.Report;
import Entities.CustomReport;
import Utilities.Database.AppointmentObject;
import Utilities.Database.ContactObject;

import java.time.*;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

/**
 * <h4>Requirement A3F: "Reports"</h4><br/>
 * Controls {@code ThreeReportsView.fxml}
 */
public class ThreeReportsViewController {

    /**
     * The main dropdown at the top, controls {@code main_table}<br/>
     * Which is a TableView of {@code Appointmentsl}<br/>
     * Lets you see what appointments the chosen contact has
     */
    @FXML
    public ComboBox<Contact> contact_dropdown;

    /**
     * The biggest table on the ThreeReportsView.fxml screen
     */
    @FXML
    public TableView<Appointment> main_table;

    /**
     * The appointment id
     */
    @FXML
    public TableColumn<Appointment, Integer> id_col;

    /**
     * The title, its a varchar(50) in the db
     */
    @FXML
    public TableColumn<Appointment, String> title_col;

    @FXML
    public TableColumn<Appointment, String> type_col;

    /**
     * The description, its a varchar(50) in the DB That means you have to limit how long this is
     */
    @FXML
    public TableColumn<Appointment, String> desc_col;

    /**
     * Both of these {@code start} and {@code end} are the type DATETIME in the db
     */
    @FXML
    public TableColumn<Appointment, LocalDateTime> start_date_col;
    @FXML
    public TableColumn<Appointment, LocalDateTime> end_date_col;

    @FXML
    public TableColumn<Appointment, Integer> customer_id_col;
    // Table for grouping by "month"
    @FXML
    public TableView<Report> first_table;

    /** mini table for grouping by state or {@code Division} */
    @FXML
    public TableView<Report> second_table;

    /** Type of the appmt, report table */
    @FXML
    public TableColumn<Report, String> appmt_type_column;

    /** Month of the appmt */
    @FXML
    public TableColumn<Report, Month> month_col;

    /** Total num. of appmts by month, regardless of the year */
    @FXML
    public TableColumn<Report, Integer> by_month_total;

    /** Name of the {@code Contact}, eg. "Rachel" */
    @FXML
    public TableColumn<Report, String> contact_name_col;

    /** Year of the appmt, eg.2020 */
    @FXML
    public TableColumn<Report, Year> contact_year_col;

    /** Total num. of appmts by year */
    @FXML
    public TableColumn<Report, Integer> year_total;

    /** Table of {@code CustomReport}s */
    @FXML
    public TableView<CustomReport> div_table;

    /** Name of the {@code Division} in the 2nd table */
    @FXML
    public TableColumn<CustomReport, String> div_col;

//    @FXML
//    public TableColumn<CustomReport, String> name_col;

    /** Total num. of appmts by division */
    @FXML
    public TableColumn<CustomReport, Integer> by_div_total;

    public final static DateTimeFormatter PROPER_DATE = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a z");

    /** The DB comes with 3 contacts */
    public final ObservableList<Contact> contacts_list = FXCollections.observableArrayList();

    /** The DB comes with 2 appmts */
    public final ObservableList<Appointment> appointments_list = FXCollections.observableArrayList();

    /** List that goes with {@code first_table} */
    public final ObservableList month_list = FXCollections.observableArrayList();

    /** List that goes with {@code second_table} */
    public final ObservableList year_list = FXCollections.observableArrayList();

    /** List that goes with {@code div_table} */
    public final ObservableList div_list = FXCollections.observableArrayList();

    /** For being able to use {@code AppointmentObject} functions */
    public final AppointmentObject appointment_class = new AppointmentObject();

    public final FilteredList<Appointment> appointments_list_filtered = new FilteredList<>(appointments_list);

    /**
     * Shows all appmts that pertain to a specific contact.<br/>
     * The contact is determined with {@code getSelectedItem()} {@code setPredicate()}<br/>
     * This shows only the appmtsthat match the {@code Contact_ID}.<br/>
     * {@code setPredicate(appmt.get_contact_id() == contact.get_contact_id())}
     */
    @FXML
    public void show_appointments_for_a_contact() {
        Contact chosen_contact = contact_dropdown.getSelectionModel().getSelectedItem();

        if (chosen_contact != null) {
            appointments_list_filtered
                    .setPredicate(appmt -> appmt.show_contact_id() == chosen_contact.get_contact_id());
        }
    }

    /**
     * So these placeholder values were helpful at first, without it I couldn't render anything in the table. I don't know if theyre still needed, but I kept them.<br/>
     * Since each one is a lambda function, I decided to do javadoc for each of them<br/>
     *
     * {@code appmt_type_col.setCellValueFactory} - Sets a placeholder value in the cell related to {@code this.type} in {@code Report.java} - The type of the appmt<br/>
     * {@code month_col.setCellValueFactory} - Sets a placeholder value in the cell related to {@code this.date} in {@code Report.java} - The month of the appmt, regardless of year<br/>
     * {@code Month/type} table - {@code by_month_total.setCellValueFactory} - Sets a placeholder value in the cell related to {@code this.total} in {@code Report.java} - Total by month<br/>
     * {@code Month/type} table - {@code contact_year_col.setCellValueFactory} - Sets a placeholder value in the cell related to {@code this.date} in {@code Report.java} - The year of the appmt<br/>
     * {@code Year} table - {@code contact_name_col.setCellValueFactory} - Sets a placeholder value in the cell related to {@code this.type} in {@code Report.java} - The name of the person<br/>
     * {@code Year} table {@code year_total.setCellValueFactory} - Sets a placeholder value in the cell related to {@code this.total} in {@code Report.java} - The num. of appmts by year<br/>
     * {@code Year} table -  {@code div_col.setCellValueFactory} - Sets a placeholder value in the cell related to {@code this.state} in {@code CustomReport.java} - The name of the division/state<br/>
     * {@code State} table {@code name_col.setCellValueFactory} -sets a placeholder value in the cell related to {@code this.name} in {@code CustomReport.java} - The name of the customer<br/>
     * {@code State} table - {@code by_div_total.setCellValueFactory} - Sets a placeholder value in the cell related to {@code this.total} in {@code CustomReport.java} - The total count of appmts by subdivision- {@code State} table
     */
    @FXML
    public void initialize() {

        init_contacts();
        init_main_table();
        init_contact_dropdown();

        go_month_table();
        go_year_table();
        go_div_table();

        first_table.setItems(month_list);
        appmt_type_column.setCellValueFactory(
                c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_type() : "type1"));

        Month m = LocalDateTime.now().getMonth();

        month_col.setCellValueFactory(c -> {
            Report r = c.getValue();

            if (r == null) {
                return new SimpleObjectProperty<>(m);
            }

            try {
                Month month_of_the_appointment = Month.of((int) r.get_date());

                return new SimpleObjectProperty<>(month_of_the_appointment);
            } catch (Exception exception) {
                System.err.println("Bad date: " + r.get_date());
                return new SimpleObjectProperty<>(m);
            }
        });

        by_month_total.setCellValueFactory(
                c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_total_num_of_appmts() : 12));

        Year y = Year.of(LocalDateTime.now().getYear());

        second_table.setItems(year_list);

        contact_name_col.setCellValueFactory(
                c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_type() : "chat"));

        contact_year_col.setCellValueFactory(c -> {
            Report report = c.getValue();

            if (report != null) {
                try {
                    return new SimpleObjectProperty<>(Year.of((int) report.get_date()));
                } catch (NumberFormatException exception) {
                    System.err.println("Not a valid year: " + report.get_date());
                }
            }
            return new SimpleObjectProperty<>(Year.of(LocalDateTime.now().getYear()));
        });
        year_total.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_total_num_of_appmts() : 120));

        div_table.setItems(div_list);

        div_col.setCellValueFactory(
                c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_state() : "Guayaquil"));
//        name_col.setCellValueFactory(
//                c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_person() : "Andres"));
        by_div_total.setCellValueFactory(
                c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_total() : 1));
    }

    /**
     * Fetch the sample contact data in {@code client_schedule.contacts} by calling the utility, {@code new ContactObject()}
     */
    public void init_contacts() {
        ContactObject contact_obj = new ContactObject();
        contacts_list.addAll(contact_obj.get_every());
    }

    /**
     * Init the main table, the big one {@code id_col.setCellValueFactory()} - Sets a placeholder value in the cell that corresponds to {@code this.id} in {@code Appointment.java}<br/>
     *  {@code title_col.setCellValueFactory()} - Sets a placeholder value in the cell that corresponds to {@code this.title} in {@code Appointment.java} - The appointment title
     *
     *
     * {@code type_col.setCellValueFactory()} - Sets a placeholder value in the cell that corresponds to {@code this.type} in {@code Appointment.java} - The appointment type<br/>
     * {@code desc_col.setCellValueFactory()} - Sets a placeholder value in the cell that corresponds to {@code this.desc} in {@code Appointment.java} - The description of the appointment<br/>
     * {@code start_date_col.setCellValueFactory()} - Sets a placeholder value in the cell that corresponds to {@code this.start} in {@code Appointment.java} - The start datetime of the appmt<br/>
     * {@code end_date_col.setCellValueFactory()} - Sets a placeholder value in the cell that corresponds to {@code this.end} in {@code Appointment.java} - The end datetime of the appmt<br/>
     * {@code customer_id_col.setCellValueFactory()} - Sets a placeholder value in the cell that corresponds to {@code this.customer_id} in {@code Appointment.java} - The ID of the customer that the appointment is with.
     */
    public void init_main_table() {

        appointments_list.addAll(appointment_class.get_every());
        main_table.setItems(appointments_list_filtered);

        id_col.setCellValueFactory(
                c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_appmt_id() : 34));
        title_col.setCellValueFactory(
                c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_appmt_title() : "thing"));
        type_col.setCellValueFactory(
                c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_appmt_type() : "general"));
        desc_col.setCellValueFactory(
                c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_appmt_text() : "text"));

        start_date_col.setCellValueFactory(c -> new SimpleObjectProperty<>(
                c.getValue() != null ? c.getValue().get_start_hour() : LocalDateTime.now()));

        start_date_col.setCellFactory(new Callback<>() {
            /**
             * Create a custom column of appmt start times in {@code start_date_col}
             * The type defined here is  {@code <Appointment, LocalDateTime>}, because its defined as a {@code LocalDateTime} in the {@code Appointment.java} file
             * @param ldt
             * @return
             */
            @Override
            public TableCell<Appointment, LocalDateTime> call(TableColumn<Appointment, LocalDateTime> ldt) {
                return new TableCell<>() {
                    /**
                     * Init the column that says 'start', the start time of the appmt(s)
                     * Formats the datetime according to MDY (American) form
                     *
                     * @param t start times of appointments
                     * @param is_empty
                     */
                    @Override
                    protected void updateItem(LocalDateTime t, boolean is_empty) {
                        super.updateItem(t, is_empty);

                        if (is_empty) {
                            setText(null);
                        } else {
                            ZonedDateTime zone = t.atZone(ZoneId.systemDefault());
                            setText(zone.format(PROPER_DATE));
                        }
                    }
                };
            }
        });

        end_date_col.setCellValueFactory(c -> new SimpleObjectProperty<>(
                c.getValue() != null ? c.getValue().get_end_hour() : LocalDateTime.now().plusMinutes(30)));

        end_date_col.setCellFactory(new Callback<>() {
            /**
             * A custom column for <Appointment, LocalDateTime> Ending timestamps of the
             * appointment
             *
             * @param ldt
             * @return
             */
            @Override
            public TableCell<Appointment, LocalDateTime> call(TableColumn<Appointment, LocalDateTime> ldt) {
                return new TableCell<>() {
                    /**
                     * Do the same for the end dates, and format.
                     *
                     * @param t end time of the appmt, ex. 3:15pm
                     * @param is_empty
                     */
                    @Override
                    protected void updateItem(LocalDateTime t, boolean is_empty) {
                        super.updateItem(t, is_empty);
                        if (is_empty) {
                            setText(null);
                        } else {
                            ZonedDateTime zone = t.atZone(ZoneId.systemDefault());
                            setText(zone.format(PROPER_DATE));
                        }
                    }
                };
            }
        });
        customer_id_col.setCellValueFactory(
                c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_customer_code() : 22));
    }

    /**
     * Init the contact dropdown box at the top-left, so its not empty
     */
    public void init_contact_dropdown() {
        contact_dropdown.setItems(contacts_list);

        contact_dropdown.setCellFactory(new Callback<>() {
            /**
             * populate the dropdown
             * @param lc
             * @return
             */
            @Override
            public ListCell<Contact> call(ListView<Contact> lc) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Contact cont, boolean is_empty) {
                        super.updateItem(cont, is_empty);

                        if (is_empty || cont == null) {
                            setText(null);
                        } else {
                            String name = cont.get_contact_name();
                            setText(name);
                        }
                    }
                };
            }
        });

        contact_dropdown.setButtonCell(new ListCell<>() {

            /**
             * Fill {@code contact_dropdown} with the contact, if he exists.
             *
             * @param cont
             * @param is_empty
             */
            @Override
            protected void updateItem(Contact cont, boolean is_empty) {

                super.updateItem(cont, is_empty);
                if (is_empty || cont == null) {
                    setText(null);
                } else {
                    String name = cont.get_contact_name();
                    setText(name);
                }
            }
        });
        // contact_dropdown.getSelectionModel().selectFirst();
        contact_dropdown.getSelectionModel().selectLast();
        show_appointments_for_a_contact();
    }

    /**
     * A table that can group by:<br/>
     * 1. month, eg. december 2019 or december 2020<br/>
     * 2. The contact, whoever was selected as the 'contact' of the appointment.<br/>
     * "Contact_Name" is one of the columns in the database thingy Implemented by just doing {@code GROUP BY month, total} in the sql statement<br/>
     * It's one of the things in the rubric
     */
    public void go_month_table() {
        month_list.addAll(appointment_class.group_by_month());
    }

    /**
     * This is the custom table thingy, in the rubric I made it count the amount of people (the amount of customers) that are from each subdivision<br/>
     * eg. 2 from United States 1 from United Kingdom
     *
     *
     *
     * It takes the output of {@code group_by_country()} function and store it in {@code div_list}
     */
    public void go_div_table() {
        div_list.addAll(appointment_class.group_by_country());
    }

    /**
     * The custom table that can group by year of the appointment.
     *
     * I think 'year' is a better one than month anyway, month can be december 1966 or december 2024<br/>
     * Also one of the things in the rubric
     */
    public void go_year_table() {
        year_list.addAll(appointment_class.group_by_year());
    }
}