package com.example.qam2sampleapp;


import java.io.IOException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ResourceBundle;

import Entities.User;
import Utilities.Database.UserObject;
import Utilities.FifteenMinAlert;
import Utilities.LogUtility;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * <h3>Login view </h3> <br/>
 * <b>Requirement A1: "create a login form" </b> <br/>
 * This is the controller for {@code LoginView.fxml} <br/>
 * When you run {@code HelloApplication.java}, this is the first screen that should appear
 */
public class LoginViewController {

    CalendarController cc=new CalendarController();

    /*
     * Basic greeting in english, french, or spanish
     */
    @FXML
    public Text header;

    /**Very rougly guesses the user's time zone.<br/>
     * I don't know if its quite accurate
     */
    @FXML
    public Label time_zone;

    /**Prompt text in the 'username' input field*/
    @FXML
    public TextField username_input_field;

    /**Prompt text in the 'password' input field*/
    @FXML
    public PasswordField password_input_field;

    @FXML
    public ImageView flag;

    /**
     * Green login button<br/>
     * When clicked with {@code onMouseClicked} it runs the {@code log_in()} fn
     * */
    @FXML
    public Button login_button;

    /**
     * 'cancel' or 'exit' button<br/>
     * Changes with the language too, french, spanish, and english
     * */
    @FXML
    public Button exit_button;

    /**
     * This changes with the user's language
     */
    @FXML
    public Text error_text;

    /**
     * More of a sublabel, like a subtitle.
     * */
    @FXML
    public Label label;

    /**
     * Simply for the exit button
     */
    @FXML
    public void exit_app() {
        Stage window = (Stage) exit_button.getScene().getWindow();
        window.close();
    }

    /** Guess the users locale with {@code Locale.getDefault()} */
    public final static Locale this_locale = Locale.getDefault();


    /**Internationalization */
    public final static ResourceBundle BUNDLE =ResourceBundle.getBundle("/MessageBundle", this_locale);

    /**
     * @throws IOException If an error occurs when trying to load the calendar view OR if the login credentials are wrong <br/>
     * {@code setOnCloseRequest()} function closes the {@code LoginView.fmxl} file and opens a new one, {@code CalendarView.fxml}<br/>
     * After logging in, it goes straight to {@code CalendarView.fxml} and shows you all the appmts<br/>
     * Calls {@code get_user_credentials(u,p} function which calls {@code User.java}
     */
    @FXML
    public void log_in() throws IOException {

        try {
            String field1_text =  username_input_field.getText();
            String field2_text  =  password_input_field.getText();

            User current_user = get_credentials(field1_text, field2_text);

            LogUtility.track_activity(field1_text, true);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/CalendarView.fxml"));
            Parent newRoot = loader.load();

            CalendarController controller = loader.getController();
            controller.initialize_calendar(current_user);

//            if (cc.has_appmt_in_15_min()) {
//                incoming_appmt_label.setText("You have an appointment soon");

//                String day_string= String.valueOf(incoming_appmt_ldt).substring(0, String.valueOf(incoming_appmt_ldt).indexOf("T"));
//                String mdy = incoming_appmt_ldt.format(date_formatting);

//                String full_date_string=String.valueOf(incoming_appmt_ldt.format(full_date_format));
//                String time_of_day_string= full_date_string.substring(full_date_string.indexOf(" ") + 1);

//                title_15min.setText(incoming_appmt_title);
//                incoming_appmt_label2.setText("ID: " +incoming_appmt_id + " | " +
//                        "Date: "+ mdy + " | " +"Time: " + time_of_day_string);

//                field1_text

//                FifteenMinAlert.my_confirmation(
//                        BUNDLE.getString("attention"),
//                        "Appointment #" + cc.incoming_appmt_id+
//                                " with type '"+ cc.incoming_appmt_type+
//                                "' is starting in less than 15 minutes!" + "\n"+"Day: " +cc.incoming_appmt_ldt.toLocalDate()+"\n" + "Time: " + cc.incoming_appmt_ldt.toLocalTime());
//            }

            Stage new_window = new Stage();
            new_window.setScene(new Scene(newRoot));
            new_window.setTitle("calendar");
            new_window.setResizable(true);
            new_window.initStyle(StageStyle.DECORATED);
            new_window.setOnCloseRequest(windowEvent -> Platform.exit());



            Stage currentStage = (Stage) login_button.getScene().getWindow();
            currentStage.close();
            new_window.show();
        }

        catch (NoSuchElementException e) {

            String wrong_input = username_input_field.getText();
            String bilingual_error_msg = BUNDLE.getString("loginFailed");

            error_text.setText(bilingual_error_msg);

            System.err.println("Login failed.");
            LogUtility.track_activity(wrong_input, false);
            FifteenMinAlert.show_error_alert("Login failed", "Please try again");
            // LogUtility.logActivity(field1_text, false);
        }

    }

    /**
     * Does the language-localized labels and the flag.<br/>
     * {@code switch(cntry)} tries to guess which country the user is from, and show their flag<br/>
     * {@code username_input_field.addEventHandler()} lets you log in by pressing enter from inside the username field<br/>
     * {@code password_input_field.addEventHandler()} lets you log in by pressing enter from inside the password field<br/>
     * {@code login_button.addEventHandler()} lets you login by pressing enter from inside the button itself
     * */
    @FXML
    public void initialize() {

        System.out.println("Hello!");

        String eng_or_french_title = BUNDLE.getString("Title");
        String subtitle=BUNDLE.getString("subtitle");
        String username_eng_or_french = BUNDLE.getString("usernameText");
        String password_eng_or_french = BUNDLE.getString("passwordText");
        String login_eng_or_french = BUNDLE.getString("login");
        String exit_eng_or_french = BUNDLE.getString("quit");

        ZoneId local_zone = ZoneId.systemDefault();

        error_text.setText("");
        login_button.setText(login_eng_or_french);
        exit_button.setText(exit_eng_or_french);

        time_zone.setText(local_zone.toString());
        label.setText(subtitle);
        header.setText(eng_or_french_title);

        username_input_field.setPromptText(username_eng_or_french);
        password_input_field.setPromptText(password_eng_or_french);

        String cntry=Locale.getDefault().getCountry();
        String flag_path = switch(cntry) {
            case "UK" -> "/flags/uk512.png";
            case "CA" -> "/flags/canada.png";
            case "FR" -> "/flags/france.png";
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
            System.err.println("Flag not found: " + flag_path);
            e.printStackTrace();
        }

        username_input_field.addEventHandler(KeyEvent.KEY_PRESSED, keypress-> {

            if (keypress.getCode() == KeyCode.ENTER) {

                try{
                    log_in();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });

        password_input_field.addEventHandler(KeyEvent.KEY_PRESSED, keypress-> {
            if (keypress.getCode() == KeyCode.ENTER) {
                try {
                    log_in();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        login_button.addEventHandler(KeyEvent.KEY_PRESSED, keypress-> {
            if (keypress.getCode() == KeyCode.ENTER) {
                try{
                    log_in();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Calls {@code UserObject.try_credentials()}<br/>
     * Checks if the credentials work.<br/>
     * @param username the username
     * @param password the password
     * @return the User object
     */
    public User get_credentials(String username, String password) throws NoSuchElementException {

        UserObject user_object = new UserObject();
        Optional<User> user = user_object.try_credentials(username, password);
        return user.orElseThrow();
    }
}