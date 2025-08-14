package com.example.qam2sampleapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.stage.StageStyle;
import Utilities.Database.MySQLConnector;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mysql.cj.jdbc.Driver;
//import com.mysql.cj.jdbc.*;
//import mysql-connector-j-8.3.0.jar;

/**
 * <h3>Main class</h3><br/>
 * Run the GUI from here.<br/>
 * Login credentials are mine.
 */
public class Go extends Application {
    private static final String URL = "jdbc:mysql://localhost:3306/client_schedule";
    private static final String USER = "sqlUser";
    private static final String PASSWORD = "Passw0rd!";

    @Override
    public void start(Stage main_stage) throws Exception{
        FXMLLoader my_loader=new FXMLLoader(
                Go.class.getResource(
                        "/LoginView.fxml"));

        Scene my_scene= new Scene(my_loader.load());
        main_stage.setScene(my_scene);

        main_stage.setResizable(false);
        main_stage.initStyle(StageStyle.UNDECORATED);
        main_stage.show();
    }

    public static void main(String[] args) {

        System.out.println("main dir for login_activity.txt: "  +System.getProperty("user.dir"));
        String query = "SELECT * FROM client_schedule.appointments";
        try {

            Connection connect = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement db_query = connect.prepareStatement(query);
            ResultSet sql_result = db_query.executeQuery();
//            while (sql_result.next()){
//                System.out.println(sql_result.getTimestamp(6));
//                System.out.println(sql_result.getTimestamp(7));
//                System.out.println(sql_result.getTimestamp(8));
//                System.out.println(sql_result.getTimestamp(10));
//            }


        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Connection current_connection = MySQLConnector.open_sql_connection();
            // null check comes first
            if ( current_connection != null && !current_connection.isClosed() ) {
                System.out.println("Connection is working");
            }

            else System.out.println("Connection failed.");
        }

        catch (Exception e) {e.printStackTrace();}
        finally {MySQLConnector.cancel_sql_connection();}

        launch(args);
        MySQLConnector.cancel_sql_connection();
    }
}