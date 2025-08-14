package com.example.qam2sampleapp;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Stream;

import Entities.Appointment;
import Entities.Customer;
import Entities.User;
import Utilities.Database.AppointmentObject;
import Utilities.Database.CustomerObject;
import Utilities.FifteenMinAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * <h3>Main view</h3><br/>
 * Controls the main {@code calendar.fxml} file<br/>
 * This one calls all the other java files, like {@code AppointmentPaneController.java}
 */
public class CalendarController  {
    String incoming_appmt_title = "";
    int incoming_appmt_id= 0;
    LocalDateTime incoming_appmt_ldt = LocalDateTime.now();
    int INCOMING_APPMT=0;

    /**user's locale, based on their IP address*/
    public final static Locale this_locale = Locale.getDefault();

    /**
     * multilingual {@code ResourceBundle}<br/>
     * based on users locale
     */
    public final static ResourceBundle BUNDLE =ResourceBundle.getBundle("/MessageBundle", this_locale);

    // public final static ResourceBundle BUNDLE =ResourceBundle.getBundle("MessageBundle", Locale.forLanguageTag("en"));
    // public final static ResourceBundle BUNDLE = ResourceBundle.getBundle(
    //         "MessageBundle", this_locale);

    /*
     * Extract from the db with {@code load_appmts()} and put it this list
     */
    public final ObservableList<Appointment> appointments_list = FXCollections.observableArrayList();

    /*
     * Filter the above {@code appointments_list} to only display the appointments that the 'user' made, and display that on {@code Calendar.fxml}<br/>
     * The user is whoever succesfully logged in on {@code LoginViewController.java}  screen
     */
    public final FilteredList<Appointment> filtered_appointments_list = new FilteredList<>(appointments_list);

    /*
     * Extract from the db with {@code load_customers()} and put it in here
     */
    public final ObservableList<Customer> customers_list = FXCollections.observableArrayList();


    /**
     * Calls {@code CustomerObject.java}  functions.<br/>
     * Works with the customers table and {@code customers_list}
     */
    public final CustomerObject customer_object = new CustomerObject();

    /**
     * calls {@code AppointmentObject.java}  function<br/>
     * Its in 'src\main\java\Utilities\AppointmentObject.java'<br/>
     * Works with the {@code appointments_list}
     */
    public final AppointmentObject appointment_class = new AppointmentObject();

    /**
     * eg. 12/13/2024 10:23 pm<br/>
     * means 13th of december, 2024<br/>
     * This is the normal  way to write it in the US
     */
//    public final static DateTimeFormatter full_date_format = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a z");
    public final static DateTimeFormatter full_date_format = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm");

    /*
     * Version with just the day, no hours
     */
    public final static DateTimeFormatter date_formatting = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//    public final static DateTimeFormatter date_formatting = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a z");

    public User user;

    /*
     * uses the {@code BUNDLE} thing to change based on language
     */
    @FXML
    public Label username_text;

    /**
     * For the text
     */
    @FXML
    public Label incoming_appmt_label;

    /**
     * Title for the appmt that could be coming within 15 min.<br/>
     * Only display it if it exists
     */
    @FXML
    public Label title_15min;

    /**
     * for the LDT
     */
    @FXML
    public Label incoming_appmt_label2;

    @FXML
    public Label incoming_appmt_id_label;

    /*
     * uses the {@code BUNDLE} thing to change based on language
     */
    @FXML
    public Label date_text;

    /**Changes based on locale*/
    @FXML
    public ImageView flag;

    /**
     *Button on the top-right of the screen
     <br/>
     * It calls {@code ThreeReportsView.fxml}
     **/
    @FXML
    public Button reports_button;

    @FXML
    public Button logout_button;

    /**
     * This changes the view based on it whether 'week' or  'month' is
     * */
    @FXML
    public ToggleGroup calendar_toggle;

    /**
     * The 1st {@code TableView}, on the top half of the screen<br/>
     * It shows appointments of the user
     */
    @FXML
    public TableView<Appointment> appointments_table;

    /**
     * The second table, on the bottom half of the screen<br/>
     * It shows the sample customers
     */
    @FXML
    public TableView<Customer> customers_table;

    /**
     *{@code Appointment_ID} column in {@code `client_schedule`.`appointments`} table
     */
    @FXML
    public TableColumn<Appointment, Integer> appmt_id;

    /**
     *{@code Title} column in {@code `client_schedule`.`appointments`} table
     */
    @FXML
    public TableColumn<Appointment, String> appmt_title;
    /**
     *{@code Description} column in the db
     */
    @FXML
    public TableColumn<Appointment, String> appmt_desc;

    /**
     *{@code Location} column in the db
     */
    @FXML
    public TableColumn<Appointment, String> appmt_location;

    @FXML
    public TableColumn<Appointment, String> appmt_is_with;

    /**
     *{@code Type} column in the {@code `client_schedule`.`appointments`} table
     */
    @FXML
    public TableColumn<Appointment, String> appmt_type;

    /**
     *{@code Start} column in the db
     */
    @FXML
    public TableColumn<Appointment, LocalDateTime> appmt_start;

    @FXML
    public TableColumn<Appointment, LocalTime> appmt_start_time;

    @FXML
    public TableColumn<Appointment, LocalDateTime> appmt_end;

    @FXML
    public TableColumn<Appointment, LocalDateTime> appmt_end_time_col;

    @FXML
    public TableColumn<Appointment, Integer> appmt_user_id_col;

    /**
     * {@code Customer_ID} column in `appointments` table
     */
    @FXML
    public TableColumn<Appointment, Integer> appmt_id2;

    /**
     * The second table, on the bottom half of the screen
     */
    @FXML
    public TableColumn<Customer, Integer> customer_id_column;

    /** Corresponds to {@code Customer_Name} column in the {@code client_schedule.customers} table<br/>
     * The customer's name, defined as {@code VARCHAR(50)}
     */
    @FXML
    public TableColumn<Customer, String> name_column;

    /** Corresponds to {@code Address} column in the {@code client_schedule.customers} table<br/>
     * The customer's address, defined as {@code VARCHAR(100)}
     */
    @FXML
    public TableColumn<Customer, String> address_column;

    @FXML
    public TableColumn<Customer, String> country_column;

    /**
     * This is the {@code Phone} column in the {@code client_schedule.customers} table
     */
    @FXML
    public TableColumn<Customer, String> division_column;
    /**
     * This is the {@code Phone} column in the {@code client_schedule.customers} table
     */

    @FXML
    public TableColumn<Customer, String> zip_code_column;

    /**
     * This is the {@code Phone} column in the {@code client_schedule.customers} table
     */
    @FXML
    public TableColumn<Customer, String> phone_num_column;

    /**
     * Calls {@code ThreeReportsViewController.java} and switches the GUI to {@code ThreeReportsView.fxml}<br/>
     * Runs when the 'reports' button is clicked
     */
    @FXML
    public void show_all_reports() throws IOException {

        Parent reports_view = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource(
                        "/ThreeReportsView.fxml")));

        Stage new_window = new Stage();

        new_window.setScene(new Scene(reports_view));
        new_window.setResizable(false);
        new_window.setTitle(BUNDLE.getString("reports"));
        new_window.initStyle(StageStyle.DECORATED);
        new_window.show();
    }

    /**
     * Runs when the "add" and "appointment" menu items are both clicked on {@code Calendar.fxml}<br/>
     * This will call the fxml in {@code AppointmentPane.fxml} and the java code in {@code AppointmentPane.java}<br/>
     * {@code appmt_processor} will do an 'early return' and stop processing if  there is a time conflict<br/>
     * It will also show an alert from {@code FifteenMinAlert.java}<br/>
     * If there is no conflict, then it will add the appointment
     * @throws IOException
     */
    @FXML
    public void add_appmt() throws IOException {
        FXMLLoader fxml_loader = new FXMLLoader();

        fxml_loader.setLocation(getClass().getResource("/AppointmentPane.fxml"));
        Parent appointment_window = fxml_loader.load();

        AppointmentPaneController appointment_controller = fxml_loader.getController();

        Consumer<Appointment> appmt_processor = appmt -> {

            if (appointment_conflict_exists(appmt)){
                String header="Customer not available";
                String text="Error scheduling the appointment.\n" + "The customer is already scheduled for that time";
                FifteenMinAlert.show_error_alert(header, text);
                return;
            }

            if (outside_business_hours(appmt)) {
                String header="Time conflict";
                String body= "The business will not be open then. Please reschedule.";

                FifteenMinAlert.show_error_alert(header,body);
                return;

            }

            //System.out.println("ID IN CC: " + appmt.get_appmt_id());
            //System.out.println("ID IN CC: " + appmt.get_appmt_id_property());
            //System.out.println("CUST ID IN CC: " + appmt.get_customer_code());

            //System.out.println("USER ID IN CC: " + appmt.get_user_id());

            int user_id = user.get_user_id();
            String the_user=user.get_user();
            appmt.set_author_of_appointment(the_user);

            //System.out.println("USER: " + the_user);
            //System.out.println("USER ID : " + user_id);

            appmt.set_most_recently_updated_person(the_user);
//            appmt.set_appmt_user(the_user);
            appmt.set_appmt_user_id(user_id);


            int max=appointment_class.get_max_id();
            max++;

            appmt.set_appmt_id(max);
            //System.out.println("NEW ID LINE 367 CC: " + appmt.get_appmt_id());

            if (appointment_class.insert(appmt)) {
                appointments_list.add(appmt);
            }


            if (has_appmt_in_15_min()){
                FifteenMinAlert.my_confirmation(
                        BUNDLE.getString("attention"),
                        "Appointment #" + appmt.get_appmt_id()+
                                " with type '"+ appmt.get_appmt_type()+
                                "' is starting in less than 15 minutes!" + System.lineSeparator() +    "\nDate: " +LocalDate.now() + System.lineSeparator() +  "\nTime: "  + appmt.get_start_hour().toLocalTime());
            }

        };

        appointment_controller.initialize_appointment(null, customers_list, appmt_processor);

        Stage current_window = new Stage();
        current_window.setScene(new Scene(appointment_window));
        current_window.setTitle(BUNDLE.getString("createAppointment"));


        current_window.setResizable(false);
        current_window.initStyle(StageStyle.DECORATED);

        current_window.show();

    }

    /**
     * This runs on when the 'add a customer' dropdown option is clicked<br/>
     * The code that controls this is all in {@code CustomerDetails.java}<br/>
     * So {@code customer_processor} does a few things:
     * It updates {@code Last_Updated_By} with whoever most recently updated the customer's entry, regardless of whether the intended action was 'update' or 'create'<br/>
     * 2. If 'create' was the intended action with respect to the customer, it  sets the creator<br/>
     * 3. If the {@code CustomerObject.java.insert()} SQL function goes through, meaning it succesfully inserted a customer into the {@code client_schedule.customers} table (and since it was successful returns a 1), then run the {@code add} function and add him to the array {@code customers_list}
     * @throws IOException
     */
    @FXML
    public void add_customer() throws IOException {
        FXMLLoader fxml_loader = new FXMLLoader();
        fxml_loader.setLocation(getClass().getResource("/CustomerView.fxml"));

        Parent add_customer_window = fxml_loader.load();
        CustomerViewController customer_controller = fxml_loader.getController();

        Consumer<Customer> customer_processor = this_customer -> {

            String name = user.get_user();
            String creator = this_customer.get_created_person();

            this_customer.set_updated_person(name);

            if (creator == null){
                this_customer.set_created_person(name);
            }

            // add him to the list
            if (customer_object.insert(this_customer)) {
                //System.out.println("customer add successful");
                customers_list.add(this_customer);
            }
        };

        customer_controller.initialize_customer_data(null, customer_processor);

        String window_title=BUNDLE.getString("createCustomer");


        Stage current_window = new Stage();
        current_window.setScene(new Scene(add_customer_window));
        current_window.setTitle(window_title);
        current_window.setResizable(false);
        current_window.initStyle(StageStyle.DECORATED);
        current_window.show();
    }

    /**
     * delete function<br/>
     * early return if:<br/>
     * 1. there is no appointment to delete, OR<br/>
     * 2. The {@code delete}() function doesnt work for some reason
     */
    @FXML
    public void delete_appmt() {

        if (!FifteenMinAlert.confirmation(FifteenMinAlert.ConfirmType.DELETE)){
            return;
        }

        Appointment appmt = appointments_table.getSelectionModel().getSelectedItem();

        if (appmt == null) {
            FifteenMinAlert.show_info_alert(
                    BUNDLE.getString("attention"),
                    BUNDLE.getString("noAppointment"));
            return;
        }

        if (!appointment_class.delete(appmt.get_appmt_id())) {
            FifteenMinAlert.show_error_alert(
                    BUNDLE.getString("error"),
                    BUNDLE.getString("errorDetailed"));
            return;
        }


        int id_deleted = appmt.get_appmt_id();
        String type_deleted= appmt.get_appmt_type();


        //System.out.println("ID TO DELETE: " + id_deleted);
        appointments_list.remove(appmt);

        FifteenMinAlert.my_confirmation(
                BUNDLE.getString("attention"),
                "Appointment with ID " + id_deleted + " and type "+ type_deleted
                        + " has been deleted.");
    }

    /**
     * Function that removes a customer.<br/>
     * The {@code customers_list.remove()} function comes at the end, after all the "checks" have failed to do an 'early return'<br/>
     * This line, {@code appointments_list.removeIf(appmt -> appmt.get_customer_code() == customer.get_customer_id())} makes sure any appmts with that now-deleted customer aren't in the appmts array anymore<br/>
     * This statement, {@code if (appointments_list.stream().anyMatch(appmt -> appmt.get_customer_code() == customer.get_customer_id()))}, asks the user if they actually <i>want</i> to delete those appmts in the first place<br/>
     * If they do, then {@code removeIf()} function runs
     */
    @FXML
    public void remove_customer() {

        if (!FifteenMinAlert.confirmation(FifteenMinAlert.ConfirmType.DELETE)){
            //System.out.println("Customer removal failed");
            return;
        }

        Customer customer = customers_table.getSelectionModel().getSelectedItem();

        if (customer == null) {
            FifteenMinAlert.show_info_alert(
                    BUNDLE.getString("attention"),
                    BUNDLE.getString("noCustomer"));
            return;
        }

        if (appointments_list.stream().anyMatch(appmt -> appmt.get_customer_code() == customer.get_customer_id())) {
            if (!FifteenMinAlert.my_confirmation(
                    "Deleting", "This will delete all the customer's appointments too." + System.lineSeparator() + "\nStill go ahead with deleting?")){
                return;
            }

            // cant delete
            if (!appointment_class.delete_all_appointments_of_a_customer(customer.get_customer_id())) {
                FifteenMinAlert.show_error_alert(
                        BUNDLE.getString("error"),
                        BUNDLE.getString("cantDelete"));
                return;
            }

            appointments_list.removeIf(appmt -> appmt.get_customer_code() == customer.get_customer_id());
        }

        int cust_id = customer.get_customer_id();
        boolean delete_successful = customer_object.delete(cust_id);

        if (!delete_successful) {
            FifteenMinAlert.show_error_alert(BUNDLE.getString("error"),
                    BUNDLE.getString("cantDelete"));
        }
        else {
            FifteenMinAlert.my_confirmation("Delete successful","Customer '" + customer.get_customer_name()  + "'"+ " has been deleted");
        }
        customers_list.remove(customer);
    }

    /**
     * Part 3b or A3b<br/>
     * Implement something that lets you see appmts by 'week' and 'month'<br/>
     * {@code filtered_list.setPredicate()} function automatically runs whenever any of the {@code RadioButton}s are pressed
     * The two RadioButtons are "week" and "month"<br/>
     * The function makes it so you see only those appmts that are:<br/>
     * 1. (notInThePast && within(7 days))  - for "week"<br/>
     * 2. (notInThePast && within(30 days))  - for "month"<br/>
     * Then it updates the appmts list to show only those appmts that are within 7 or 30 days.
     */
    @FXML
    public void switch_calendar_view() {

        RadioButton toggle_button = (RadioButton) calendar_toggle.getSelectedToggle();
        //filtered_appointments_list.setPredicate
        filtered_appointments_list.setPredicate(appmt -> {

//            String week_or_month = toggle_button.getText();

            LocalDateTime appmt_time = appmt.get_start_hour();

            LocalDateTime now = LocalDateTime.now();
            LocalDate today = LocalDate.now();

            // don't show the past
            boolean lower_bound = appmt_time.isAfter(today.atStartOfDay());

            if (toggle_button.getText().equals("Week")) {
                LocalDateTime one_week_in_the_future = now.plusDays(7);
                //System.out.println(lower_bound && appmt_time.isBefore(one_week_in_the_future));
                return lower_bound && appmt_time.isBefore(one_week_in_the_future);
            }

            // Show appointments coming in the next month
            // Also make sure they're not in the past

            if (toggle_button.getText().equals("Month")) {
                //System.out.println(lower_bound && appmt_time.isBefore(now.plusDays(30)));
                return lower_bound && appmt_time.isBefore(now.plusDays(30));
            }

            return true;
        });
    }
    /**
     * The consumer {@code update_appmt_processor()} does 2 things:<br/>
     * 1. It uses a {@code set()} function to update the list<br/>
     * 2. It uses an {@code update()} function to update the db table<br/>
     * The same is true for {@code update_customer_processor()} in the {@code update_customer} below
     */
    @FXML
    public void update_appmt() throws IOException {

//        boolean my_try_bool=appointment_class.my_try();
//        //System.out.println("my_try_bool: "+my_try_bool);

        Appointment this_appointment = appointments_table.getSelectionModel().getSelectedItem();

        //System.out.println("ID LINE 595: " + this_appointment.get_appmt_id());

//        //System.out.println("is string(38): " +this_appointment.get_appmt_id_property().get() == "39");

        if (this_appointment == null) {
            FifteenMinAlert.show_info_alert(
                    BUNDLE.getString("attention"),
                    BUNDLE.getString("noAppointment"));
            return;
        }

        Consumer<Appointment> update_appmt_processor = updated_appointment -> {

            //System.out.println("user id: "+user.get_user());

//            try (Connection db_connect = MySQLConnector.open_sql_connection()) {
//            }
//            catch (SQLException e){
//                //System.out.println("catch happened");
//                e.printStackTrace();

//            updated_appointment.set_author_of_appointment(name);
//            updated_appointment.set_most_recently_updated_person(name);

            //System.out.println("Title in calendarcontroller: " + updated_appointment.get_appmt_title());
            //System.out.println("Id in calendarcontroller: " + this_appointment.get_appmt_id());
            //System.out.println("Id 2 in calendarcontroller: " + updated_appointment.get_appmt_id());

            if (updated_appointment.get_appmt_id() == -1){
                updated_appointment.set_appmt_id(this_appointment.get_appmt_id());// keep same id
            }

            //System.out.println("get_user_id() 635 CC: " + this_appointment.get_user_id());

            if (updated_appointment.get_user_id() == -1){
                updated_appointment.set_appmt_user_id(this_appointment.get_user_id());
//                if (username_text.equals("admin")){
//                    updated_appointment.set_appmt_user_id(2);
//                }
//                if (username_text.equals("test")){
//                    updated_appointment.set_appmt_user_id(1);
//                }
            }

//            if (appointment_class.update(upd)) {
            if (appointment_class.update(updated_appointment)) {
                //System.out.println("SUCCESS updated, line 630 cc.java");
                int appmt_index = appointments_list.indexOf(this_appointment);
                appointments_list.set(appmt_index, updated_appointment);

            }
            else {
                System.out.println("UPDATE FAIL");
            }

        };


        FXMLLoader fxml_loader = new FXMLLoader();
        fxml_loader.setLocation(getClass().getResource("/AppointmentPane.fxml"));

        Parent new_load = fxml_loader.load();

        AppointmentPaneController appmt_view_controller = fxml_loader.getController();
        appmt_view_controller.initialize_appointment(this_appointment, customers_list, update_appmt_processor);

        Stage current_window = new Stage();
        current_window.setScene(new Scene(new_load));

        current_window.setTitle(BUNDLE.getString("changeAppointment"));
        current_window.setResizable(false);
        current_window.initStyle(StageStyle.DECORATED);

        current_window.show();
    }

    /**
     * {@code update_customer_processor} does the same thing as {@code update_appmt_processor} , just on the {@code customers} table and {@code customers_list} list.<br/>
     * Logic is similar to {@code update_appmt()} function
     */
    @FXML
    public void update_customer() throws IOException {

        Customer cust = customers_table.getSelectionModel().getSelectedItem();

        if (cust == null) {
            FifteenMinAlert.show_info_alert(
                    BUNDLE.getString("attention"),
                    BUNDLE.getString("noCustomer"));
            return;
        }

        Consumer<Customer> update_customer_processor = updated_cust -> {

            String name = user.get_user();
            updated_cust.set_updated_person(name);

            if (customer_object.update(updated_cust)){
                customers_list.set(customers_list.indexOf(cust), updated_cust);
            }
        };

        FXMLLoader fxml_loader = new FXMLLoader();
        fxml_loader.setLocation(getClass().getResource("/CustomerView.fxml"));

        Parent customer_window = fxml_loader.load();
        CustomerViewController customer_controller = fxml_loader.getController();
        customer_controller.initialize_customer_data(cust, update_customer_processor);

        Stage current_window = new Stage();
        current_window.setScene(new Scene(customer_window));

//        if (BUNDLE.getString("changeCustomer").length() >2){
//            current_window.setTitle(BUNDLE.getString("changeCustomer"));
//        }
//        else current_window.setTitle("Updating a customer");

        current_window.setTitle(BUNDLE.getString("changeCustomer"));
        current_window.setResizable(false);
        current_window.initStyle(StageStyle.DECORATED);

        current_window.show();
    }

    /**
     * Initialization function for {@code CalendarView.fxml}<br/>
     * {@code switch(cntry)} tries to guess which country the user is from and show the appropriate flag<br/>
     * Calls other load and init functions too
     */
    @FXML
    void initialize() {

//        appointments_table.refresh();

        String cntry=Locale.getDefault().getCountry();
        String flag_path = switch(cntry) {
            case "UK" -> "/flags/uk512.png";
            case "FR" -> "/flags/france.png";
            case "CA" -> "/flags/canada.png";
            case "BR","PT" -> "/flags/brazil512.png";
            case "IN" -> "/flags/india.png";
            case "PK" -> "/flags/pak512.png";
            case "BN" -> "/flags/bengal512.png";
            case "ID" -> "/flags/indo512.png";
            case "PH" -> "/flags/ph1024.png";
            case "EG" -> "/flags/egypt512.png";
            case "MX", "ES" -> "/flags/mexico512.png";
            case "GT", "SV","HN", "NI","PA","CR" -> "/flags/guatemala.png";
            case "CO", "VE", "EC" -> "/flags/ecuador.png";
            case "NG", "ZA", "ET", "TZ","UG", "KE" -> "/flags/africa.png";
            case "RU", "KZ", "BY" -> "/flags/russia.png";
            case "DE", "IT", "NL", "RO", "PL" -> "/flags/eu512.png";
            case "SY", "IQ", "DZ", "SA", "SD", "AE" -> "/flags/arab512.png";
            default -> "/flags/us1024.png";
        };

        try {
            flag.setImage(new Image(getClass().getResourceAsStream(flag_path)));
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

        String formatted_date= LocalDate.now().format(date_formatting);
        date_text.setText(formatted_date);

        LocalDateTime now = LocalDateTime.now();

        LocalDate current_day = LocalDate.now();
        LocalTime now_moment = LocalTime.now();

//        Stream<Appointment> NEAR_FUTURE_STREAM = appointments_list.stream().filter(
//                appmt -> appmt.get_user_id() == user.get_user_id()).filter(appmt-> {
//            LocalDate local_day = appmt.get_start_hour().toLocalDate();
//            return local_day.isEqual(current_day);
//        }).filter(appmt-> {
//            LocalTime appmt_time = appmt.get_start_hour().toLocalTime();
//            return appmt_time.isBefore(LocalTime.now().plusMinutes(15));
//        });
//
//        Optional<Appointment> incoming_appmt = NEAR_FUTURE_STREAM.findFirst();
//
//        if (incoming_appmt.isPresent()) {
//
//            Appointment incoming_appmt=NEAR_FUTURE_STREAM.findFirst().get();
//            incoming_appmt_title = incoming_appmt.get().get_appmt_title();
//            incoming_appmt_ldt=incoming_appmt.get().get_start_hour();

//            incoming_appmt_id = incoming_appmt.get().get_appmt_id();
//
//        }

//        Optional<Appointment> within_15_min_exists=
//                appointments_list.stream().filter(appmt -> appmt.get_user_id() == user.get_user_id())
//                        .filter(appmt -> appmt.get_start_hour().toLocalDate().isEqual(current_day))
//                        .filter(appmt -> appmt.get_start_hour().toLocalTime().isBefore(LocalTime.now().plusMinutes(15))).findAny();

//        appointments_table.getSelectionModel().select(within_15_min_exists.orElse(null));

//        Stream incoming_stream= appointments_list.stream().filter(appmt -> appmt.get_user_id() == user.get_user_id())
//                .filter(appmt-> {
//                    LocalDate local_day = appmt.get_start_hour().toLocalDate();
//                    return local_day.isEqual(current_day);
//                }).filter(appmt -> {
//                    LocalTime appmt_time = appmt.get_start_hour().toLocalTime();
//                    return appmt_time.isBefore(now_moment.plusMinutes(15));
//                });

//        if (incoming_stream.count() > 0){
//        Optional<Appointment> inc=incoming_stream.findFirst();
//
//        if (inc.isPresent()){
//
//            incoming_appmt_label.setText("You have an appointment soon");
//
//            String day_string= String.valueOf(incoming_appmt_ldt).substring(0, String.valueOf(incoming_appmt_ldt).indexOf("T"));
//            String mdy = incoming_appmt_ldt.format(date_formatting);

//            String full_date_string=String.valueOf(incoming_appmt_ldt.format(full_date_format));
//            String time_of_day_string= full_date_string.substring(full_date_string.indexOf(" ") + 1);

//            title_15min.setText(inc.get().get_appmt_title());

//            incoming_appmt_label2.setText("ID: " +inc.get().get_appmt_id() + " | " +
//                    "Date: "+ inc.get().get_start_hour().toLocalDate() + " | " +"Time: " + inc.get().get_start_hour());

//        }


//        if (        incoming_stream.anyMatch(appmt-> {
//                    LocalTime appmt_time = appmt.get_start_hour().toLocalTime();
//                    return appmt_time.isBefore(now_moment.plusMinutes(15));
//                });
//        )
        //.anyMatch(appmt-> {
//                    LocalTime appmt_time = appmt.get_start_hour().toLocalTime();
//                    return appmt_time.isBefore(now_moment.plusMinutes(15));
//                });
        for (Appointment curr_appmt : filtered_appointments_list) {

            LocalDateTime start_time = curr_appmt.get_start_hour();
            //System.out.println("START: " + start_time);

            int upcoming_id = curr_appmt.get_appmt_id();

            System.out.println(curr_appmt);
            if (start_time.isAfter(now) && start_time.isBefore(now.plusMinutes(15))) {

                INCOMING_APPMT=1;

                // FifteenMinAlert.my_confirmation(
                //         BUNDLE.getString("attention"),
                //         "Your appointment #" + upcoming_id +
                //                 " with type '"+ curr_appmt.get_appmt_type()+
                //                 "' is starting in less than 15 minutes!" +System.lineSeparator() + "\nDate: " +start_time.toLocalDate() + System.lineSeparator() +  "\nTime: " + start_time.toLocalTime());

                incoming_appmt_label.setText("You have an appointment soon");

                String mdy = incoming_appmt_ldt.format(date_formatting);

                String full_date_string=String.valueOf(incoming_appmt_ldt.format(full_date_format));
                String time_of_day_string= full_date_string.substring(full_date_string.indexOf(" ") + 1);

                title_15min.setText(incoming_appmt_title);
                incoming_appmt_label2.setText("ID: " +incoming_appmt_id + " | " +
                        "Date: "+ mdy + " | " +"Time: " + time_of_day_string);
                break;
            }
        }
//        for (Appointment appointment : appointments_list) {
//        for (Appointment curr_appmt : appointments_list) {
//
//            LocalDateTime start_time = curr_appmt.get_start_hour();
//
//            int upcoming_id = curr_appmt.get_appmt_id();
//            if (has_appmt_in_15_min()) {
//                FifteenMinAlert.my_confirmation(
//                        BUNDLE.getString("attention"),
//                        "Appointment #" + curr_appmt.get_appmt_id() +
//                                " with type '" + curr_appmt.get_appmt_type() +
//                                "' is starting in less than 15 minutes!");
//
//                FifteenMinAlert.show_info_alert(BUNDLE.getString("attention"), BUNDLE.getString("nearFuture"));
//                incoming_appmt_label.setText("You have an appointment soon");
//
//                String day_string = String.valueOf(incoming_appmt_ldt).substring(0, String.valueOf(incoming_appmt_ldt).indexOf("T"));
//                String mdy = incoming_appmt_ldt.format(date_formatting);
//
//                String full_date_string = String.valueOf(incoming_appmt_ldt.format(full_date_format));
//                String time_of_day_string = full_date_string.substring(full_date_string.indexOf(" ") + 1);
//
//                title_15min.setText(incoming_appmt_title);
//                incoming_appmt_label2.setText("ID: " + incoming_appmt_id + " | " +
//                        "Date: " + mdy + " | " + "Time: " + time_of_day_string);
//
//                // appointments_table.getSelectionModel()
//                // .getSelectionModel()
//            }
//        }
//            if (start_time.isAfter(now) && start_time.isBefore(now.plusMinutes(15))) {
//
//                INCOMING_APPMT=1;
//
//                FifteenMinAlert.my_confirmation(
//                        BUNDLE.getString("attention"),
//                        "Your appointment #" + upcoming_id +
//                                " with type '"+ curr_appmt.get_appmt_type()+
//                                "' is starting in less than 15 minutes!" +System.lineSeparator() + "\nDate: " +start_time.toLocalDate() + System.lineSeparator() +  "\nTime: " + start_time.toLocalTime());
//
//                incoming_appmt_label.setText("You have an appointment soon");
//
//                String mdy = incoming_appmt_ldt.format(date_formatting);
//
//                String full_date_string=String.valueOf(incoming_appmt_ldt.format(full_date_format));
//                String time_of_day_string= full_date_string.substring(full_date_string.indexOf(" ") + 1);
//
//                title_15min.setText(incoming_appmt_title);
//                incoming_appmt_label2.setText("ID: " +incoming_appmt_id + " | " +
//                        "Date: "+ mdy + " | " +"Time: " + time_of_day_string);
//                break;
//            }

        init_appmt_table();
        init_customers_table();
        switch_calendar_view();

        if (INCOMING_APPMT == 0) {
            FifteenMinAlert.my_confirmation("Information","You have no appointments soon");
        }

    }

    /**
     * Init the  {@code CalendarView.fxml} view
     * @param user_p
     */
    public void initialize_calendar(User user_p) {

        this.user = user_p;
        //System.out.println("USER: " +user.get_user());
        //System.out.println("USER'S ID: " +user.get_user_id());

        //System.out.println("USER: " +user_p.get_user());
        //System.out.println("USER'S ID: " +user_p.get_user_id());

        username_text.setText(this.user.get_user());

        load_customers();
        load_appmts();
    }
    /**
     *
     * @param appmt
     * @return boolean
     * True means "Yes, it is outside business hours"
     * False means "No, it is inside business hours"
     * Also checks for weekends
     */
    public boolean outside_business_hours(Appointment appmt){

        LocalDateTime start =  appmt.get_start_hour();
        LocalDateTime end =appmt.get_end_hour();

        //System.out.println("NON-EST START TIME: " + start);
        //System.out.println("NON-EST END TIME: " + end);

        int hour = appmt.get_start_hour().getHour();
        int minute=appmt.get_start_hour().getMinute();

        LocalDateTime ldt = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, minute));
        LocalTime appmt_time_EST = ldt.atZone(ZoneId.of("America/New_York")).toLocalTime();
        LocalTime opens_at = LocalTime.of(8,0);
        LocalTime closes_at=LocalTime.of(22,0);

        if (appmt_time_EST.isBefore(opens_at) || appmt_time_EST.isAfter(closes_at)){
            return true;
        }

        boolean sat1=start.toLocalDate().getDayOfWeek() == DayOfWeek.SATURDAY;
        boolean sat2 = end.toLocalDate().getDayOfWeek() == DayOfWeek.SATURDAY;

        boolean sun1=start.toLocalDate().getDayOfWeek() == DayOfWeek.SUNDAY;
        boolean sun2=end.toLocalDate().getDayOfWeek() == DayOfWeek.SUNDAY;

        if (sat1 || sat2 || sun1|| sun2) {
            return true;
        }
        return false; // it is NOT outside business hours, good.
    }

    /**
     * Checks if there is an appointment within the next 15 minutes, and returns true/false depending on if that's the case.<br/>
     * @return {@code true} or {@code false}, depending on whether theres an appmt in 15 min for the user.<br/>
     * {@code initialize_calendar()} calls this function, and {@code LoginViewController.java} calls {@code initialize_calendar()}<br/>
     * 1st filter: {@code filter(appmt.userId==user.userId)}<br/>
     * Makes sure the appmt actually belongs to the guy who logged in.<br/>
     * Otherwise, it is just someone else's appointment.<br/>
     * 2nd filter: {@code filter(day==currDay)}<br/>
     * Make sure the appmts are on the same day.<br/>
     * An appointment that happened at 13:45 on 8 Nov doesn't matter if right now is 13:45 on 8 Dec<br/>
     * 3rd filter: {@code anyMatch(appmt.isBefore(now+15 min))}<br/>
     * Narrow it down further, the appmt has to be approaching within the next 15 minutes.<br/>
     * Otherwise, you have enough time before the appmt.<br/>
     * If any appmts pass all 3 of these filters, show the 'you have an appmt coming in 15 min' alert
     */
    public boolean has_appmt_in_15_min() {

        // Write code to provide an alert when there is an appointment within 15 minutes of the user’s log-in.
        // A custom message should be displayed in the user interface and include the
        // Appointment ID,
        //* Date
        //* Time
        // If the user does not have any appointments within 15 minutes of logging in, display a custom message in the user interface indicating there are
        // NO upcoming appointments.

        LocalDate current_day = LocalDate.now();
        LocalTime now_moment = LocalTime.now();

        Stream<Appointment> NEAR_FUTURE_STREAM = appointments_list.stream().filter(
                appmt -> appmt.get_user_id() == user.get_user_id()).filter(appmt-> {
            LocalDate local_day = appmt.get_start_hour().toLocalDate();
            return local_day.isEqual(current_day);
        }).filter(appmt-> {
            LocalTime appmt_time = appmt.get_start_hour().toLocalTime();
            return appmt_time.isBefore(LocalTime.now().plusMinutes(15));
        });

        Optional<Appointment> incoming_appmt = NEAR_FUTURE_STREAM.findFirst();
//        boolean UPCOMING_APPMT_DETECTED = NEAR_FUTURE_STREAM.findFirst().isPresent();

        if (incoming_appmt.isPresent()) {

//            Appointment incoming_appmt=NEAR_FUTURE_STREAM.findFirst().get();
            incoming_appmt_title = incoming_appmt.get().get_appmt_title();
            incoming_appmt_ldt=incoming_appmt.get().get_start_hour();

            incoming_appmt_id = incoming_appmt.get().get_appmt_id();

        }

//        Optional<Appointment> within_15_min_exists=
//                appointments_list.stream().filter(appmt -> appmt.get_user_id() == user.get_user_id())
//                        .filter(appmt -> appmt.get_start_hour().toLocalDate().isEqual(current_day))
//                        .filter(appmt -> appmt.get_start_hour().toLocalTime().isBefore(LocalTime.now().plusMinutes(15))).findAny();

//        appointments_table.getSelectionModel().select(within_15_min_exists.orElse(null));

        // returns a boolean
        return appointments_list.stream().filter(
                appmt -> appmt.get_user_id() == user.get_user_id()).filter(appmt-> {
            LocalDate local_day = appmt.get_start_hour().toLocalDate();
            return local_day.isEqual(current_day);
        }).anyMatch(appmt-> {
            LocalTime appmt_time= appmt.get_start_hour().toLocalTime();
            return appmt_time.isBefore(now_moment.plusMinutes(15));
        });
    }

    /**
     * This function tries to make the 'customers' table, the on the lower half, work correctly by using placeholder data<br/>
     * {@code customer_id_column.setCellValueFactory()} - Sets a placeholder value in the cell corresponding to the variable {@code this.customer_id} in {@code Customer.java} - The customer's id<br/>
     * {@code name_column.setCellValueFactory()} - Sets a placeholder value in the cell corresponding to the variable {@code this.name} in {@code Customer.java} - The customer's name<br/>
     * {@code address_column.setCellValueFactory()} - Sets a placeholder value in the cell corresponding to the variable {@code this.address} in {@code Customer.java} - Where the  customer lives<br/>
     * {@code zip_code_column.setCellValueFactory()} - Sets a placeholder value in the cell corresponding to the variable {@code this.zip_code} in {@code Customer.java} - The customer's zip code<br/>
     * {@code phone_num_column.setCellValueFactory()} - Sets a placeholder value in the cell corresponding to the variable {@code this.phone_num} in {@code Customer.java} - The customer's phone number<br/>
     * {@code division_column.setCellValueFactory()} - Sets a placeholder value in the cell corresponding to the variable {@code this.division} in {@code Customer.java} - The customer's subdivision<br/>
     * {@code country_column.setCellValueFactory()} - Sets a placeholder value in the cell corresponding to the variable {@code this.country} in {@code Customer.java} - The customer's home country
     */
    public void init_customers_table() {

        customers_table.setItems(customers_list);
        customer_id_column.setCellValueFactory(c -> c.getValue().get_customer_id_property().asObject());
        name_column.setCellValueFactory(c ->  c.getValue().get_customer_name_property());
        address_column.setCellValueFactory(c -> c.getValue().get_address_property());
        zip_code_column.setCellValueFactory(c ->c.getValue().get_zip_code_property());
        phone_num_column.setCellValueFactory(c -> c.getValue().get_phone_property());
        division_column.setCellValueFactory(c->c.getValue().get_division_property() );
        country_column.setCellValueFactory(c-> c.getValue().get_country_property());


    }

    /**
     * This function tries to make the 'appointments' table, the one on the upper half, work correctly by using placeholder data<br/>
     * {@code appmt_id.setCellValueFactory()} - Sets a placeholder value in the cell corresponding to the variable {@code this.id} in {@code Appointment.java} - The appointment's assigned id<br/>
     * {@code appmt_title.setCellValueFactory()} - Sets a placeholder value in the cell corresponding to the variable {@code this.title} in {@code Appointment.java} - The title of the appointment<br/>
     * {@code appmt_desc.setCellValueFactory()} - Sets a placeholder value in the cell corresponding to the variable {@code this.desc} in {@code Appointment.java} - Brief description of the appointment<br/>
     * {@code appmt_location.setCellValueFactory()} - Sets a placeholder value in the cell corresponding to the variable {@code this.location} in {@code Appointment.java} - The appointment's location<br/>
     * {@code appmt_is_with.setCellValueFactory()} - Sets a placeholder value in the cell corresponding to the variable {@code this.contact} in {@code Appointment.java} - The contact's name<br/>
     * {@code appmt_type.setCellValueFactory()} - Sets a placeholder value in the cell corresponding to the variable {@code this.type} in {@code Appointment.java} - Type of the appointment<br/>
     * {@code appmt_id2.setCellValueFactory()} - Sets a placeholder value in the cell corresponding to the variable {@code this.customer_id} in {@code Appointment.java} - Customer corresponding to the appointment
     */
    public void init_appmt_table() {

        appointments_table.setItems(filtered_appointments_list);

//        appmt_id.setCellValueFactory(c -> c.getValue().get_appmt_id_property());

        appmt_id.setCellValueFactory(c -> c.getValue().get_appmt_id_property().asObject());
//        appmt_id.setCellValueFactory(new PropertyValueFactory<>("id"));

        appmt_title.setCellValueFactory(c -> c.getValue().get_appmt_title_property());
//        appmt_title.setCellValueFactory(c -> c.getValue().get_appmt_title());
        appmt_desc.setCellValueFactory(c ->c.getValue().get_appmt_text_property());

        appmt_location.setCellValueFactory(c -> c.getValue().get_appmt_location_property());
//        appmt_location.setCellValueFactory(new PropertyValueFactory<>("location"));

        appmt_start.setCellValueFactory(c-> c.getValue().get_start_hour_property());
        appmt_end.setCellValueFactory(c-> c.getValue().get_end_hour_property());

        appmt_is_with.setCellValueFactory(c -> c.getValue().get_contact_name_property());
        appmt_type.setCellValueFactory(c -> c.getValue().get_appmt_type_property());

        appmt_id2.setCellValueFactory(c -> c.getValue().get_customer_code_property().asObject());
        appmt_user_id_col.setCellValueFactory(c-> c.getValue().get_user_id_property().asObject());


    }

    /**
     * Checks for any time conflicts, make sure new appmts can't happen if there is no time available because of another appmt.<br/>
     * {@code filter} function filters only those appointments that could have a scheduling conflict by being on the same day<br/>
     * {@code anyMatch} function uses {@code equals()} to make sure that if they're on the same day, the time is not the same. If it is the same, there is a conflict.<br/>
     * @param result Appointment object to check any conflicts against
     * @return true or false
     */
    public boolean appointment_conflict_exists(Appointment result) {
        return appointments_list.stream().filter(appmt -> {
            LocalDateTime appmt_start_time  = appmt.get_start_hour();
            LocalDateTime appmt_start_time_check = result.get_start_hour();

            LocalDateTime appmt_end_time = appmt.get_end_hour();
            LocalDateTime appmt_end_time_check = result.get_end_hour();

            boolean codes_match = appmt.get_customer_code() == result.get_customer_code();
            boolean start_days_match = appmt_start_time.toLocalDate().equals(appmt_start_time_check.toLocalDate());
            boolean end_days_match  = appmt_end_time.toLocalDate().equals(appmt_end_time_check.toLocalDate());

            return codes_match && (start_days_match || end_days_match);
        }).anyMatch(appmt -> {
            LocalDateTime appmt_start_datetime= appmt.get_start_hour();
            LocalDateTime appmt_end_datetime=  appmt.get_end_hour();

            boolean start_times_match= appmt_start_datetime.equals(result.get_start_hour());
            boolean end_times_match  = appmt_end_datetime.equals(result.get_end_hour());

            // there could be a conflict, with eg. only 15 minutes between the start of both appointments
            // if it's a lot of time, then no big deal
            boolean starts_could_overlap = appmt_start_datetime.isBefore(result.get_start_hour());
            boolean ends_could_overlap = appmt_end_datetime.isBefore(result.get_end_hour());

            return  (start_times_match || end_times_match || starts_could_overlap || ends_could_overlap);
        });
    }

    /**
     * Load the customers from the database.<br/>
     * {@code init_customers_table} is related to the GUI, this is related to database
     */
    public void load_customers() {
        ObservableList<Customer> customers_observable_list = customer_object.get_every();
        customers_list.addAll(customers_observable_list);
    }

    /**
     * Load the appmts from the database.<br/>
     * {@code `client_schedule`.`appointments`} is the table in the db<br/>
     * {@code init_customers_table} is about the gui, this one is getting the sample data
     */
    public void load_appmts() {
        ObservableList<Appointment> temp_list = appointment_class.get_every();
        // //System.out.println(temp_list);
        // System.err.println(temp_list);
        // ObservableList<Appointment> temp_list = FXCollections.observableArrayList(new Appointment(6,"t","d","loc","type", LocalDateTime.now(), LocalDateTime.now().plusMinutes(14), LocalDateTime.now(), "pers", LocalDateTime.now(),"last",4,"cu",9,"cont",90,"us"));

        appointments_list.addAll(temp_list);
    }

    // public void log_out() throws IOException {
    //     FXMLLoader loader = new FXMLLoader();
    //     loader.setLocation(getClass().getResource("LoginView.fxml"));
    //     Parent newRoot = loader.load();
    //     Stage new_window = new Stage();
    //     new_window.setScene(new Scene(newRoot));

    //     new_window.setTitle(BUNDLE.getString("login"));

    //     new_window.setResizable(true);
    //     new_window.initStyle(StageStyle.DECORATED);
    //     new_window.setOnCloseRequest(windowEvent -> Platform.exit());

    //     Stage currentStage = (Stage) logout_button.getScene().getWindow();
    //     currentStage.close();
    //     new_window.show();
    // }

}