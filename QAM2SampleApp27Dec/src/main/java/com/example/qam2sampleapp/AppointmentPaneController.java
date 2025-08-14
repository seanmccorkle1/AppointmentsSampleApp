package com.example.qam2sampleapp;

import Entities.Appointment;
import Entities.Contact;
import Entities.Customer;

import Utilities.Database.AppointmentObject;
import Utilities.Database.ContactObject;
import Utilities.Database.CustomerObject;
import Utilities.FifteenMinAlert;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * <h3>"Add appointment" view</h3><br/>
 * Controls the {@code AppointmentPane.fxml} file. <br/>
 * <p>Uses {@code getText()} fn to retrieve text input from the user in a textfield</p> <br/>
 * <p>And then eg.{@code set_appmt_title()} setter to store that input into the object like {@code this.title=title}</p> <br/>
 * An example:<br/>
 * <pre>
 *      new Appointment(title: "business meeting", location:"Virginia", start: 2020-05-28);
 *      etc
 * </pre>
 */
public class AppointmentPaneController {
    AppointmentObject APO = new AppointmentObject();

    /** Appointment obj, start it off with null*/
    public Appointment appointment = null;

    public Consumer<Appointment> processor;

    /**
     * List of customers used in {@code customer_dropdown}
     * */
    public final ObservableList<Customer> customers_list = FXCollections.observableArrayList();
    public final ObservableList<Contact> contacts = FXCollections.observableArrayList();

    /**
     * First time-related dropdown, when it starts
     */
    public final ObservableList<LocalTime> start_hour_list = FXCollections.observableArrayList();

    /**
     * second time-related dropdown, when it ends
     */
    public final ObservableList<LocalTime> end_hour_list = FXCollections.observableArrayList();

    /**{@code DateTimeFormatter} instance<br/>
     * This gives 10:52 PM for example
     **/
    public final DateTimeFormatter time_formatting = DateTimeFormatter.ofPattern("h:mm a");

    /**Title like 'routine meeting'*/
    @FXML
    public TextField title_of_appointment;

    @FXML
    public TextField user_id_tf;

    @FXML
    public TextField id_of_appointment;

    /**address like: 1420 something dr.*/
    @FXML
    public TextField location_of_appointment;

    /**The type, 'supplier meeting'*/
    @FXML
    public TextField type_of_appointment;

    /**
     * Description of the appointment<br/>
     *{@code TextArea} cause its longer<br/>
     * Example: 'routine meeting for x'
     */
    @FXML
    public TextArea appointment_description;

    /**The database comes with 3 contacts in {@code client_schedule.contacts} */
    @FXML
    public ComboBox<Contact> contact_dropdown;

    /**
     * <p>It loads the 3 customers that come in the {@code client_schedule} database</p>
     */
    @FXML
    public ComboBox<Customer> customer_dropdown;

    /**
     * {@code DatePicker}<br/>
     * Selects the day, like 23 nov 2024, but not hours, that is what {@code start_dropdown} and {@code end_dropdown} does
     *
     */
    @FXML
    public DatePicker start_calendar;

    /**
     * <ul><li>Type {@code LocalTime}, corresponds to a certain moment during the day like {@code 8:00 pm}</li><br/>
     * <li>That minute is when the appmt starts</li></ul>
     */
    @FXML
    public DatePicker end_calendar;

    /**Type {@code LocalTime}, corresponds to a certain moment during the day like {@code 8:00 pm}<br/>
     * That minute is when the appmt starts
     */
    @FXML
    public ComboBox<LocalTime> start_dropdown;

    /**
     * Type {@code LocalTime}, corresponds to a certain moment during the day like {@code 8:15 pm}<br/>
     * That time is when the appmt ends
     */
    @FXML
    public ComboBox<LocalTime> end_dropdown;

    /**
     * In the fxml file AppointmentPane.fxml, there's the code -  {@code onAction=#submit_appmt}<br/>
     * This is what this button is for, submitting the info by calling the {@code submit} function
     */
    @FXML
    public Button submit_button;

    /**
     * Cancel button.<br/>
     * Simply calls {@code cancel()} which is defined below
     */
    @FXML
    public Button cancel_button;

    /**
     * {@code initialize()} fn<br/>
     * It just calls other init functions
     */
    @FXML
    void initialize() {

        initialize_calendars();
        initialize_contacts_and_customers();
        initialize_start_time();
        initialize_end_time();
    }
    /**
     * Initialize the two {@code DatePicker}s that are used for choosing the day of the appointment<br/>
     * {@code start_calendar.setDayCellFactory()} and {@code end_calendar.setDayCellFactory()} functions make all the saturdays and sundays on the calendar "grayed-out"<br/>
     * Because those are not workdays.
     */
    public void initialize_calendars() {
        start_calendar.setValue(LocalDate.now());
        start_calendar.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate curr_date, boolean empty) {
                super.updateItem(curr_date, empty);

            }
        });

        start_calendar.setEditable(false);

        // appointments should last just one day
        end_calendar.setValue(LocalDate.now());

        end_calendar.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate curr_date, boolean empty) {
                super.updateItem(curr_date, empty);
            }
        });
        end_calendar.setEditable(false);
    }

    /**
     * Populate {@code contact_dropdown} and {@code customer_dropdown} with data
     * */
    public void initialize_contacts_and_customers() {

        //ComboBox.setItems()
        contact_dropdown.setItems(contacts);
        contact_dropdown.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Contact> call(ListView<Contact> lv) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Contact cont, boolean is_empty) {
                        super.updateItem(cont, is_empty);

                        if (cont==null || is_empty){
                            setText(null);
                        }
                        else {
                            String name = cont.get_contact_name();
                            setText(name);
                        }
                    }
                };
            }
        });

        contact_dropdown.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Contact cont, boolean is_empty) {
                super.updateItem(cont, is_empty);

                if (cont==null || is_empty) {
                    setText(null);
                }
                else {
                    setText(cont.get_contact_name());
                }
            }
        });

        customer_dropdown.setItems(customers_list);
        customer_dropdown.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Customer> call(ListView<Customer> lv) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Customer cust, boolean is_empty) {
                        super.updateItem(cust, is_empty);

                        if (cust==null || is_empty) {
                            setText(null);
                        }
                        else {
                            setText(cust.get_customer_name());
                        }
                    }
                };
            }
        });

        customer_dropdown.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Customer cust, boolean is_empty) {
                super.updateItem(cust, is_empty);

                if (cust==null || is_empty) {
                    setText(null);
                }
                else {
                    setText(cust.get_customer_name());
                }
            }
        });
    }


    /**
     * Start the beginning times in {@code start_dropdown} loser to the current time in the day<br/>
     * * If it's already 5 pm, start it near 5:00pm then 7:00am */
    public void initialize_start_time() {
        start_dropdown.setItems(start_hour_list);
        start_dropdown.setCellFactory(new Callback<>() {
            @Override
            public ListCell<LocalTime> call(ListView<LocalTime> lv) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(LocalTime time, boolean is_empty) {
                        super.updateItem(time, is_empty);

                        if (time == null) {
                            setText(null);
                        }
                        else {
                            setText(time.format(time_formatting));
                        }

                    }
                };
            }
        });

        start_dropdown.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(LocalTime time, boolean is_empty) {
                super.updateItem(time, is_empty);
                if (time == null || is_empty){
                    setText(null);
                }
                else setText(time.format(time_formatting));
            }
        });
    }

    /** Same idea, just +15 or +30 minutes*/
    public void initialize_end_time() {
        end_dropdown.setItems(end_hour_list);
        end_dropdown.setCellFactory(new Callback<>() {
            @Override
            public ListCell<LocalTime> call(ListView<LocalTime> time_list) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(LocalTime time, boolean is_empty) {
                        super.updateItem(time, is_empty);

                        if (time==null) {
                            setText(null);
                        }
                        else {
                            setText(time.format(time_formatting));
                        }

                    }
                };
            }
        });
        end_dropdown.setButtonCell(new ListCell<>() {

            @Override
            protected void updateItem(LocalTime time, boolean is_empty) {

                super.updateItem(time, is_empty);

                if (time==null || is_empty) {
                    setText(null);
                }
                else {
                    setText(time.format(time_formatting));
                }
            }
        });
    }

    /**
     * @param app The appointment to init
     * @param customers_list The customers that correspond with the appmt
     * @param processor oncomplete processor<br/>
     * <p>Runs after an appointment is added, or when {@code add_appointment} is called
     * </p>
     */
    public void initialize_appointment(Appointment app, ObservableList<Customer> customers_list, Consumer<Appointment> processor) throws IOException {
        this.processor = processor;
        this.customers_list.addAll(customers_list);

//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("CalendarView.fxml"));
//        Parent newRoot = loader.load();

//        CalendarController controller = loader.getController();
//        controller.has_appmt_in_15_min();

        load_all_contacts();
        load_times();
        fix_appmt_times();
        load_appmt(app);
    }

    /**calls  {@code Contact.get_every()} function to load the 3 contacts that come pre-stored in the db
     */
    public void load_all_contacts() {

        ContactObject my_contacts = new ContactObject();
        contacts.addAll(my_contacts.get_every());
    }

    /**Calls  {@code Customer.get_every()} functionto load the customers that come<br/>
     * Same idea as above
     */
    public void load_all_customers() {

        CustomerObject my_customers = new CustomerObject();
        ObservableList<Customer> list_of_customers= my_customers.get_every();
        customers_list.addAll(list_of_customers);
    }

    /** This one uses 15 min intervals and puts those intervals into the ComboBox {@code start_dropdown} and {@code end_dropdown}<br/>
     * 4 intervals for every hour.<br/>
     * So it shows eg.
     * <ul>
     *     <li> 8:00 am</li>
     *     <li> 8:15 am</li>
     *     <li> 8:30 am</li>
     * </ul><br/>
     * in the boxes in {@code AppointmentPane.fxml}
     * */
    public void load_times() {

        int[] hour_steps = {0,15,30,45};

        // i=8 j=0,1,2,3
        // i=9  j=0,1,2,3

        LocalDate today = LocalDate.now();

//        for (int idx= 8; idx < 22; idx++) {

        for (int idx= 6; idx < 24; idx++) {

            int hour=idx;

            for (int minutes_idx = 0; minutes_idx < hour_steps.length; minutes_idx++) {

                int minutes = hour_steps[minutes_idx];

//                LocalDateTime ldt = LocalDateTime.of(today, now);
                LocalDateTime ldt = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, minutes));

                ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/New_York"));

                // use the user's time zone
                ZonedDateTime zoned_time = zdt.withZoneSameInstant(ZoneId.systemDefault());
                LocalTime final_time = zoned_time.toLocalTime();

                start_hour_list.add(final_time);
                end_hour_list.add(final_time);
            }
        }

//        end_hour_list.add(LocalDateTime.of())
//        LocalDateTime closing_time= LocalDateTime.of(today, LocalTime.of(22,0));

//        ZonedDateTime closing_time_with_zone = closing_time.atZone(ZoneId.of("America/New_York"));
//        ZonedDateTime point_in_time_corresponding_with_close = closing_time_with_zone.withZoneSameInstant(ZoneId.systemDefault());

//        end_hour_list.add(point_in_time_corresponding_with_close.toLocalTime());
    }

    /**Make the default-selected time (in the dropdown/ComboBox) closer to the user's local time<br/>
     * Don't start it at 7am unless it is already 7am
     * */
    public void fix_appmt_times() {
        LocalTime right_now = LocalTime.now();

        start_dropdown.getSelectionModel().select(0);
        end_dropdown.getSelectionModel().select(2);    // +30 minutes from above

        for (LocalTime interval_of_15 : start_hour_list) {

            // making sure the event lies in the future

            if (right_now.isBefore(interval_of_15)) {

                // start_hour_list.interval_of_15

                int plus_0_idx= start_hour_list.indexOf(interval_of_15);
                int plus_15_idx = end_hour_list.indexOf(interval_of_15.plusMinutes(15));

                start_dropdown.getSelectionModel().select(plus_0_idx);
                end_dropdown.getSelectionModel().select(plus_15_idx);

                // start_dropdown.getSelectionModel().select(1);
                // end_dropdown.getSelectionModel().select(1);

                // select a good time  once and break
                break;
            }
        }
    }

    /**
     * The same view, {@code AppointmentPaneController.fxml} is shown for both "adding" and "updating" an appointment.<br/>
     * The difference is, when updating an appointment, the textfields and such come pre-loaded with the data.<br/>
     * Uses {@code setText()} functions to render the text on the screen<br/>
     * {@code matching_customer()} and {@code matching_contact()} functions attempt to load the {@code customer_dropdown} and {@code contact_dropdown}  ComboBoxes respectively, if they exist.<br/>
     * They both use {@code filter()} functions to pre-populate the boxes with the customer that is already picked, {@code a.cust_id == b.cust_id}
     */
    public void load_appmt(Appointment appmt) {

        if (appmt == null) {
            return;
        }

        String title =appmt.get_appmt_title();
        String desc =appmt.get_appmt_text();
        String loc  =appmt.get_appmt_location();
        String form= appmt.get_appmt_type();

        int id_in_load_appmt=appmt.get_appmt_id();

        //System.out.println("ID IN load_appmt():" + id_in_load_appmt);
        id_of_appointment.setText(String.valueOf(id_in_load_appmt));

        // id_of_appointment.setId();

        title_of_appointment.setText(title);
        appointment_description.setText(desc);
        location_of_appointment.setText(loc);
        type_of_appointment.setText(form);

        user_id_tf.setText(String.valueOf(appmt.get_user_id()));

        Optional<Customer> matching_customer = customers_list.stream().filter(cust -> {
            int cust_id  = cust.get_customer_id();
            int matching_cust_id =appmt.get_customer_code();
            return cust_id == matching_cust_id;
        }).findAny();

        Optional<Contact> matching_contact = contacts.stream().filter(cont-> {

            int cont_id_A = cont.get_contact_id();
            int cont_id_B  = appmt.show_contact_id();

            return cont_id_A ==cont_id_B;
        }).findAny();

        customer_dropdown.getSelectionModel().select(matching_customer.orElse(null));
        contact_dropdown.getSelectionModel().select(matching_contact.orElse(null));

        LocalDateTime appmt_start_datetime = appmt.get_start_hour();
        LocalDateTime appmt_end_datetime = appmt.get_end_hour();

        start_calendar.setValue(appmt_start_datetime.toLocalDate());
        start_dropdown.getSelectionModel().select(appmt_start_datetime.toLocalTime());

        end_calendar.setValue(appmt_end_datetime.toLocalDate());
        end_dropdown.getSelectionModel().select(appmt_end_datetime.toLocalTime());
    }

    /**
     * for the 'cancel' or 'quit' button
     */
    @FXML
    void cancel() {
        if (FifteenMinAlert.confirmation(FifteenMinAlert.ConfirmType.CANCEL)) {
            Stage stage = (Stage) cancel_button.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * for the 'submit' button on {@code AppointmentPane.fxml}
     * runs with onClick
     */
    @FXML
    void submit() {
        int max_found= APO.get_max_id();
//        processor.accept(get_appointment(max_found));

        processor.accept(get_appointment());
        Stage stage = (Stage) submit_button.getScene().getWindow();
        stage.close();
    }

    void submit_update(){
//        Stage stage = (Stage) update_button.getScene().getWindow();
//        stage.close();
    }


    /**
     *
     * This runs all the setters in {@code Appointment.java}<br/>
     * To store the info, given from the user's input
     * */
    public Appointment get_appointment() {
        // For ADDING an appointment
        if (appointment == null) {
            appointment = new Appointment();
        }
        // if UPDATING
        else {
            String id_as_string = id_of_appointment.getText();
            appointment.set_appmt_id(Integer.parseInt(id_as_string));
        }

        appointment.set_appmt_title(title_of_appointment.getText());
        appointment.set_appmt_text(appointment_description.getText());

        appointment.set_appmt_location(location_of_appointment.getText());
        appointment.set_appmt_type(type_of_appointment.getText());

        LocalDate day_of_appmt = start_calendar.getValue();
        LocalDate day_of_appmt_end = end_calendar.getValue();

        LocalTime time_of_appmt=start_dropdown.getSelectionModel().getSelectedItem();
        LocalTime time_of_appmt_end=end_dropdown.getSelectionModel().getSelectedItem();

        LocalDateTime start_of_appmt = LocalDateTime.of(day_of_appmt, time_of_appmt);
        LocalDateTime end_of_appmt = LocalDateTime.of(day_of_appmt_end,  time_of_appmt_end);

        appointment.set_start_hour(start_of_appmt);
        appointment.set_end_hour(end_of_appmt);

        Contact host = contact_dropdown.getSelectionModel().getSelectedItem();
        //System.out.println("contact: " + host);

        appointment.set_contact_code(host.get_contact_id());
        appointment.set_contact(host.get_contact_name());

//        appointment.set_appmt_user_id(Integer.parseInt(user_id_tf.getText()));

        Customer guest  =customer_dropdown.getSelectionModel().getSelectedItem();
        appointment.set_customer_code(guest.get_customer_id());
//        appointment.set_customer_name(guest.get_customer_name());

//        DateTimeFormatter no_microseconds = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now_moment = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime create_time = appointment.get_creation_time();

        if (create_time == null) {
            //System.out.println("NO CREATION TIME");
            appointment.set_creation_time(now_moment);
        }
        else {
            appointment.set_creation_time(create_time);
        }
        //System.out.println("createtime: " + create_time);

        appointment.set_most_recently_updated_time(now_moment);

        //System.out.println(appointment);
        return appointment;
    }
}