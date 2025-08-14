package Utilities.Database;

import Entities.Appointment;
import Entities.CustomReport;
import Entities.Report;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * <h3>Appointments interface</h3>  <br/>
 * interfaces with the client_schedule.appointments data
 */
public class AppointmentObject implements DataObject<Appointment>{

    /**
     * this will become the id<br/>
     * it uses a 'max' function to make it unique<br/>
     * because the appmt id is a primary key
     */
    public int c=2;

    /**
     *
     * @param results
     * @return Appointment, the one matching appmt
     */
    public static Appointment show_appointment(ResultSet results) throws SQLException {

        int appmt_id = results.getInt("Appointment_ID");
        int cust_id  = results.getInt("Customer_ID");
        int user_id  = results.getInt("User_ID");
        int cont_id  = results.getInt("Contact_ID");

        String appmt_title= results.getString("Title");
        String appmt_desc= results.getString("Description");
        String appmt_place=  results.getString("Location");
        String appmt_type = results.getString("Type");
//        String cupst =  results.getString("Customer_Name");
        String cont =  results.getString("Contact_Name");
//        String user = results.getString("User_Name");

        String appmt_created_person = results.getString("Created_By");
        String appmt_updated_person = results.getString("Last_Updated_By");

        LocalDateTime appmt_creation_time =
                results.getTimestamp("Create_Date").toLocalDateTime();
        LocalDateTime appmt_start_time =
                results.getTimestamp("Start").toLocalDateTime();
        LocalDateTime appmt_end_time  =
                results.getTimestamp("End").toLocalDateTime();

        LocalDateTime  appmt_last_updated_time   = results.getTimestamp("Last_Update").toLocalDateTime();
        return new Appointment(appmt_id, appmt_title,appmt_desc, appmt_place,appmt_type, appmt_start_time,appmt_end_time, appmt_creation_time,appmt_created_person, appmt_last_updated_time,appmt_updated_person,cust_id,cont_id,user_id,cont);

    }

    public int get_max_id() {
        try (Connection db_connect = MySQLConnector.open_sql_connection()) {
//            String query = "SELECT COUNT(*) FROM client_schedule.appointments";
            String query = "SELECT MAX(Appointment_ID) AS highest_id FROM client_schedule.appointments";

            try (PreparedStatement stmt = db_connect.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                //System.out.println("rs: " + rs);

                if (rs.next()) {
//                    //System.out.println("rs.next(): " + rs.next());
//                    int number_of_records= rs.getInt(1);
                    int highest_id = rs.getInt("highest_id");
                    //System.out.println("HIGHEST:  " + highest_id);
                    return highest_id;
                }
                else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @param id
     * @return
     *
     */
    @Override
    public Optional<Appointment> fetch(int id) {

        try (Connection db_connect = MySQLConnector.open_sql_connection()){

            Statement statement = db_connect.createStatement();

            String col1= "appointments.*, ";
            String col2= "User_Name, ";
            String col3="Contact_Name, ";
            String col4= "Customer_Name ";

            String table1= "client_schedule.appointments, ";
            String table2= "client_schedule.customers, ";
            String table3="client_schedule.contacts, ";
            String table4= "client_schedule.users ";

            String cond1= "customers.Customer_ID=appointments.Customer_ID ";
            String cond2= "contacts.Contact_ID=appointments.Contact_ID ";
            String cond3= "users.User_ID=appointments.User_ID ";
            String cond4= "appointments.Appointment_ID=";
            // String cond4= "appointments.Appointments_ID=";

            ResultSet results = statement.executeQuery(
                    "SELECT " + col1 +col2+col3+col4 +
                            "FROM "+table1+table2+table3+table4+
                            "WHERE " +cond1+"AND "+ cond2+"AND " + cond3+
                            "AND "+cond4+id);

            if (results.next()) {
                return Optional.of(show_appointment(results));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public ObservableList<Appointment> get_every() {

        try (Connection db_connect = MySQLConnector.open_sql_connection()){

            Statement statement = db_connect.createStatement();

            String col1 = "appointments.*, ";
            String col2 = "User_Name, ";
            String col3= "Contact_Name, ";
            String col4= "Customer_Name ";

            String table1 ="client_schedule.appointments, ";
            String table2 = "client_schedule.customers, ";
            String table3= "client_schedule.contacts, ";
            String table4="client_schedule.users ";

            String cond1= "customers.Customer_ID=appointments.Customer_ID ";
            String cond2= "contacts.Contact_ID=appointments.Contact_ID ";
            String cond3= "users.User_ID=appointments.User_ID";

            ResultSet results = statement.executeQuery("SELECT " +col1+col2+col3+col4+"FROM "+table1+table2+table3+table4+"WHERE " +cond1+ "AND "+cond2+"AND "+cond3);

            ObservableList<Appointment> appointments =
                    FXCollections.observableArrayList();

            while (results.next()) {
                appointments.add(show_appointment(results));
            }

            return appointments;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }

    /**
     * Inserts the user-created appmt into {@code appointments} table
     * @param appmt
     * @return flag, a boolean
     */
    @Override
    public boolean insert(Appointment appmt) {

        int max=get_max_id();
        //System.out.println("max: " + max);

        try (Connection db_connect = MySQLConnector.open_sql_connection()){

            PreparedStatement prepared_sql_statement =
                    db_connect.prepareStatement(
                            "INSERT INTO client_schedule.appointments VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            String my_title = appmt.get_appmt_title();
            String my_desc =appmt.get_appmt_text();
            String my_place =appmt.get_appmt_location();
            String my_type =appmt.get_appmt_type();

            Timestamp my_start_time= Timestamp.valueOf(appmt.get_start_hour());
            Timestamp my_end_time= Timestamp.valueOf(appmt.get_end_hour());
            Timestamp my_creation_time= Timestamp.valueOf(appmt.get_creation_time());
            String my_created_by = appmt.get_author_of_appointment();
            Timestamp my_updated_time= Timestamp.valueOf(appmt.get_most_recently_updated_time());

            String my_updated_person = appmt.get_most_recently_updated_person();
            int my_cust_id=appmt.get_customer_code();
            int my_user_id=appmt.get_user_id();
            int my_contact_id=appmt.show_contact_id();

            c = max;
            c++;

            LocalDate their_day=appmt.get_start_hour().toLocalDate();
            LocalTime their_time=appmt.get_start_hour().toLocalTime();
//            ZoneId their_zone = ZoneId.systemDefault();

//            ZonedDateTime start_zdt=appmt.get_start_hour().atZone(their_zone);
//            Zone11dDateTime end_zdt=appmt.get_end_hour().atZone(their_zone);

//            Instant utc_moment_start =start_zdt.toInstant();
//            Instant utc_moment_end =end_zdt.toInstant();
//            System.out.println("UTC START: "  + utc_moment_start);
//            System.out.println("UTC END: " + utc_moment_end );

//            Timestamp my_start_time = Timestamp.from(utc_moment_start);
//            Timestamp my_end_time = Timestamp.from(utc_moment_end);
//            Timestamp my_updated_time = Timestamp.from(Instant.now());
//            Timestamp my_creation_time=Timestamp.from(LocalDateTime.now().atZone(their_zone).toInstant());

            prepared_sql_statement.setInt(1, c);
            prepared_sql_statement.setString(2, my_title);
            prepared_sql_statement.setString(3, my_desc);
            prepared_sql_statement.setString(4, my_place);
            prepared_sql_statement.setString(5, my_type);
            prepared_sql_statement.setTimestamp(6, my_start_time);
            prepared_sql_statement.setTimestamp(7, my_end_time);
            prepared_sql_statement.setTimestamp(8, my_creation_time);
            prepared_sql_statement.setString(9, my_created_by);
            prepared_sql_statement.setTimestamp(10, my_updated_time);
            prepared_sql_statement.setString(11, my_updated_person);
            prepared_sql_statement.setInt(12, my_cust_id);
            prepared_sql_statement.setInt(13, my_user_id);
            prepared_sql_statement.setInt(14, my_contact_id);


            int flag = prepared_sql_statement.executeUpdate();

            try (ResultSet keys= prepared_sql_statement.getGeneratedKeys()) {
                if (keys.next()) {
                    int auto_generated_id = keys.getInt(1);
                    appmt.set_appmt_id(auto_generated_id);
                }
            } catch (SQLException e){
                e.printStackTrace();
            }

            if (flag > 0){
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * @param appmt
     * @return boolean, as a flag
     * This puts the appmt in the db itself
     */
    @Override
    public boolean update(Appointment appmt) {

        try (Connection db_connect = MySQLConnector.open_sql_connection()) {


            String my_title = appmt.get_appmt_title();
            String my_desc =appmt.get_appmt_text();
            String my_place =appmt.get_appmt_location();
            String my_type =appmt.get_appmt_type();

            Timestamp my_start_time= Timestamp.valueOf(appmt.get_start_hour());
            Timestamp my_end_time= Timestamp.valueOf(appmt.get_end_hour());
            Timestamp my_creation_time= Timestamp.valueOf(appmt.get_creation_time());

            String my_created_by = appmt.get_author_of_appointment();
            Timestamp my_updated_time= Timestamp.valueOf(appmt.get_most_recently_updated_time());

            String my_updated_person = appmt.get_most_recently_updated_person();

            int my_cust_id = appmt.get_customer_code();
            int my_contact_id = appmt.show_contact_id();
            int my_user_id = appmt.get_user_id();

            int my_appmt_id = appmt.get_appmt_id();
            int my_appmt_id2=appmt.get_appmt_id_property().get();
            int my_appmt_id3=appmt.get_appmt_id_property().getValue();


            PreparedStatement update_statement  =db_connect.prepareStatement(
                    "UPDATE client_schedule.appointments SET Title = ?, Description = ?, Location = ?, Type = ?, " +
                            "Start = ?, End = ?, Customer_ID = ?, Contact_ID = ?, User_ID = ? WHERE Appointment_ID = ?");


            update_statement.setString(1, my_title);
            update_statement.setString(2, my_desc);
            update_statement.setString(3, my_place);
            update_statement.setString(4, my_type);
            update_statement.setTimestamp(5, my_start_time);
            update_statement.setTimestamp(6, my_end_time);
            update_statement.setInt(7, my_cust_id);
            update_statement.setInt(8, my_contact_id);

            if (my_user_id==-1){
                update_statement.setInt(9, 2);
            }
            else {
                update_statement.setInt(9, my_user_id);
            }

            if (my_appmt_id == -1) {
                update_statement.setInt(10, 12);
            }
            else {
                update_statement.setInt(10, my_appmt_id);
            }


            int result = update_statement.executeUpdate();

            //System.out.println("appmt_id getValue(): "+ appmt.get_appmt_id_property().getValue());

            if (result > 0) {
                //System.out.println("SUCCESS UPDATE");
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Delete the appmt from the GUI and the DB.
     * This one deletes it from the DB
     * @param id, id of appmt to delete
     * @return 0 or 1
     */
    @Override
    public boolean delete(int id) {

        try (Connection db_connect = MySQLConnector.open_sql_connection()) {

            Statement sql_statement = db_connect.createStatement();

            String table="client_schedule.appointments ";
            String cond="Appointment_ID=";
            // String cond="Appointments_ID=";

            //System.out.println("id to delete: " + id);
            int flag = sql_statement.executeUpdate("DELETE FROM "+ table + "WHERE "+cond+ id);

            if (flag >0) {
                //System.out.println("delete successful");
                return true;      // successful delete
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean delete_all_appointments_of_a_customer(int id_of_cust) {

        try (Connection db_connect = MySQLConnector.open_sql_connection()) {

            Statement sql_statement = db_connect.createStatement();

            String table = "client_schedule.appointments ";
            String condition= "Customer_ID=";

            int r= sql_statement.executeUpdate("DELETE FROM " + table + "WHERE " +condition +id_of_cust);

            if (r > 0) {
                return true;
            }

        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * one of the report tables on {@code ThreeReportsView.fxml}, the one that groups upcoming appmts by:
     * 1. month
     * 2. type
     * @return ArrayList which the caller then uses
     */
    public ArrayList group_by_month() {

        try (Connection db_connect = MySQLConnector.open_sql_connection()) {

//            //System.out.println("first_mini_table() try");

            Statement sql_statement = db_connect.createStatement();

            String col1,col2,col3, from, group_by;

            col1 = "month(start) AS month, ";
            col2="type, ";
            col3= "count(*) AS total ";

            from = "client_schedule.appointments ";

            ResultSet query_results = sql_statement.executeQuery("SELECT "+col1+
                    col2+col3+"FROM " +from +  "GROUP BY month, type");

            ArrayList<Report> first_report_list = new ArrayList<>();

            //System.out.println(query_results);

            while (query_results.next()) {
                long date = query_results.getLong("month");
                String type =  query_results.getString("type");
                int tot=  query_results.getInt("total");

//                //System.out.println("MONTH:  " + date);
//                //System.out.println(type);
//                //System.out.println(tot);

                Report current_row = new Report(date,type,tot);
                first_report_list.add(current_row);
            }

            return first_report_list;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     * one of the other report tables on {@code ThreeReportsView.fxml},
     * the one that groups upcoming appmts by year
     */
    public ArrayList group_by_year() {

        try (Connection db_connect = MySQLConnector.open_sql_connection()) {

            Statement statement = db_connect.createStatement();


            // Sum the # of appmts. made for each contact
            String select,select2,select3;

            select ="SELECT year(start) AS year, ";
            select2 = "contacts.Contact_Name AS contact, ";
            select3 = "COUNT(*) AS total ";
            String table="appointments ";

            String cond="appointments.Contact_ID=contacts.Contact_ID ";

            ResultSet results = statement.executeQuery(
                    select+select2+select3+ "FROM "+table+
                            "JOIN contacts ON " + cond+ "GROUP BY year, contact");

            ArrayList<Report> second_report_list = new ArrayList<>();

            while (results.next()) {
                long yr_1=  results.getLong("year");
                String cont_2=  results.getString("contact");
                int tot_3=  results.getInt("total");

                Report row = new Report(yr_1, cont_2, tot_3);

                second_report_list.add(row);
            }

            return second_report_list;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     * one of the report tables, the one that groups upcoming appmts by {@code Country}
     * @return ArrayList which the caller then uses
     */
    public ArrayList group_by_country(){
        try (Connection db_connect = MySQLConnector.open_sql_connection()) {
            Statement sql_statement = db_connect.createStatement();

            ResultSet results = sql_statement.executeQuery(
                    "SELECT first_level_divisions.Division AS state, " +
                            "Customer_Name AS name, COUNT(*) AS total FROM customers JOIN first_level_divisions ON " +
                            "first_level_divisions.Division_ID=customers.Division_ID GROUP BY state");
            ArrayList<CustomReport> custom_report_list = new ArrayList<>();

            while (results.next()) {
                String state =results.getString("state");
                String name=  results.getString("name");
                int amt =  results.getInt("total");

                custom_report_list.add(new CustomReport(state, name, amt));
            }
            return custom_report_list;
        }
        catch (SQLException error){
            error.printStackTrace();
        }
        return new ArrayList<>();
    }
}

//    public boolean my_try() {
//
//        try (Connection db_connect = MySQLConnector.open_sql_connection()) {
//
//            PreparedStatement st = db_connect.prepareStatement(
//                    "UPDATE appointments " +
//                            "SET Title = ?, Description = ?, Location = ?, Type = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?, " +
//                            "Start = ?, End = ? WHERE Appointment_ID = ?");
//
//
//            st.setString(1, "Party"); // title
//            st.setString(2, "The title"); //description
//            st.setString(3, "Solola department"); // location
//            st.setString(4, "Fun"); // type
//            st.setInt(5, 2);  // customer id
//            st.setInt(6, 2); // user id
//            st.setInt(7, 1); // contact id
//
//            st.setTimestamp(8, Timestamp.valueOf(LocalDateTime.of(2025, 2, 12, 18, 15, 0)));
//            st.setTimestamp(9, Timestamp.valueOf(LocalDateTime.of(2025, 2, 12, 18, 30, 0)));
//            st.setInt(10, 24); // Appointment_ID param
//
//            //System.out.println("correct timestamp: "+Timestamp.valueOf(LocalDateTime.of(2025, 2, 12, 18, 15, 0)));
//
//            int rows_updated= st.executeUpdate();
//            //System.out.println(rows_updated + " ROWS UPDATED.");
//
//            if (rows_updated > 0){
//                return true;
//            }
//        }
//        catch (SQLException e){
//            //System.out.println("catch happened");
//            e.printStackTrace();
//        }
//        return false;
//    }
