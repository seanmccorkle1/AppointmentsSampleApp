package Utilities;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <h3>Requirement C: Tracking</h3> <br/>
 * This class {@code LogUtility} records each login attempt, and prints out whether it was successful or not.<br/>
 * It includes a timestamp in {@code login_activity.txt} which is in the root folder, right before 'src'.
 */
public class LogUtility {

    /**timestamp, 14-12-2024 for 14th of december, 2024*/
    public static final DateTimeFormatter seconds = DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss");
    /**
     * output it to a plaintext file named {@code login_activity.txt} in the root folder
     * */
    public static final String output_file = System.getProperty("user.dir") + "/login_activity.txt";

//    public static final String output_file = "C:\\Users\\LabUser\\Downloads\\QAM2SampleApp27Dec\\QAM2SampleApp27Dec\\login_activity.txt";
//    public static final String output_file =  "./login_activity.txt";

    /**
     * This is a function that logs the user's login successes/failures and writes it to {@code root\login_activity.txt}<br/>
     * Also converts months to the english form - Jan/Feb instead of 1/2<br/>
     * @param user_input What was typed in the username textbox
     * @param bool Whether the login attempt was successful or not
     */
    public static void track_activity(String user_input, boolean bool){

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(output_file, true))) {

            String now = LocalDateTime.now().format(seconds);
            //            String month=now.substring(now.indexOf("-") + 1,now.indexOf("-") + 3);
            //    String romanized_month="";

            String[] date_array= now.split("[-\\s:]");

            String month=date_array[1];
            System.out.println(month);

            switch (Integer.parseInt(month)){
                case 1: month="Jan"; break;
                case 2: month="Feb"; break;
                case 3: month="Mar"; break;
                case 4: month="Apr"; break;
                case 5: month="May"; break;
                case 6: month="Jun"; break;
                case 7: month="Jul"; break;
                case 8: month="Aug"; break;
                case 9: month="Sep"; break;
                case 10: month="Oct"; break;
                case 11: month="Nov"; break;
                case 12: month="Dec"; break;
                default: month = "Jan";
            }
            String day =date_array[0];
            String year =date_array[2];
            String time = date_array[3] + ":" + date_array[4] +":"+date_array[5];


            String first = String.format("[%s %s %s] [%s]", day, month, year, time);

            String second = String.format("'%s' logged in successfully:  ", user_input);

            if (bool==false){
                System.err.println("Login attempt failed");
                String fail=String.format("'%s'", user_input);

                writer.newLine();
                writer.write(first + "   " + "Login attempt failed with username " + fail);
                writer.newLine();
            }

            else {
                writer.newLine();
                writer.write(first + "  "+second+bool );
                writer.newLine();
            }
        }

        catch (IOException exception) {
            System.err.println("ERROR: " + exception.getMessage());
            System.out.println(exception.getMessage());
        }
    }
}