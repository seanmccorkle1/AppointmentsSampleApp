package Utilities;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

/**
 * <h4>Alerts</h4> <br/>
 * Shows custom alerts as defined in requirement 'A3E' <br/>
 * Not only the 15 min alert, I picked a bad name for this one..
 */
public class FifteenMinAlert {

    /**
     * Find the user's native tongue
     */
    public final static Locale this_locale = Locale.getDefault();
    /**
     * Localize the language to the user's native tongue
     */
    public final static ResourceBundle BUNDLE =
            ResourceBundle.getBundle("/MessageBundle", this_locale);

    private static Alert confirm_alert;

    /**
     * headerCANCEL, headerDELETE, and headerEXIT are all defined in the *es.properties and *en.properties files
     */
    public enum ConfirmType {CANCEL, DELETE, EXIT};

    /**
     * @return boolean
     * Change the type of alert based on what they're doing with the resourcebundle<br/>
     * {exiting, canceling, or deleting} something<br/>
     * Return a boolean to make it act as a 'flag'
     */
    public static boolean confirmation(ConfirmType exit_cancel_delete) {

        confirm_alert = new Alert(Alert.AlertType.CONFIRMATION);
        String header = "header" + exit_cancel_delete;
        String msg = "body" + exit_cancel_delete;

        String type_of_header = BUNDLE.getString(header);
        String type_of_msg =  BUNDLE.getString(msg);

        confirm_alert.setHeaderText(type_of_header);
        confirm_alert.setContentText(type_of_msg);

        Optional<ButtonType> dialog = confirm_alert.showAndWait();

        return (dialog.isPresent() && dialog.get() == ButtonType.OK);
    }

    /**
     *
     * @param custom_title
     * @param custom_msg
     * @return boolean
     * This function simply displays a "confirm"-type message with text I put in for both the title  and body.<br/>
     * The {@code Label} is there because it helps with the newlines (\\n)
     */
    public static boolean my_confirmation(String custom_title, String custom_msg) {

        confirm_alert = new Alert(Alert.AlertType.CONFIRMATION);
        confirm_alert.setHeaderText(custom_title);

        Label area= new Label(custom_msg);
        area.setWrapText(true);

        confirm_alert.getDialogPane().setContent(area);

        Optional<ButtonType> dialog = confirm_alert.showAndWait();
        return (dialog.isPresent() && dialog.get() == ButtonType.OK);
    }


    /**
     * @param title Bigger-font title
     * @param body Main text area
     * This shows a {@code AlertType.ERROR} kind of alert
     */
    public static void show_error_alert(String title, String body ) {
        confirm_alert = new Alert(Alert.AlertType.ERROR);
        confirm_alert.setHeaderText(title);
        confirm_alert.setContentText(body);

        Label area= new Label(body);
        area.setWrapText(true); // word wrap

        confirm_alert.getDialogPane().setContent(area);
        confirm_alert.show();
    }

    /**
     * @param bilingual_title from resourcebundle
     * @param bilingual_body from resourcebundle
     * This shows a {@code AlertType.INFORMATION} kind of alert, with the exclamation mark<br/>
     * Localize it to the user's native language
     */
    public static void show_info_alert(String bilingual_title, String bilingual_body) {
        confirm_alert = new Alert(Alert.AlertType.INFORMATION);

        confirm_alert.setHeaderText(bilingual_title);
        confirm_alert.setContentText(bilingual_body);

        confirm_alert.show();
    }

}