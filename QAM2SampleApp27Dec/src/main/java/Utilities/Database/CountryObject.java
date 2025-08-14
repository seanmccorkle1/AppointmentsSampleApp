package Utilities.Database;

import Entities.Country;
import java.sql.*;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** {@code countries} table
 * */
public class CountryObject implements DataObject<Country> {

    /**
     * @param country_results data
     * @return Country
     * name and id are the only two ones defined (and needed) in {@code Country.java}
     * */
    public Country show_country(ResultSet country_results) throws SQLException {
        int country_code = country_results.getInt("Country_ID");
        String country_name = country_results.getString("Country");

        return new Country(country_code, country_name);
    }

    /**
     * @param country_code
     * @return Country, the country that matches the id
     * */
    @Override
    public Optional<Country> fetch(int country_code) {

        try (Connection db_connect = MySQLConnector.open_sql_connection()) {
            Statement sql_statement = db_connect.createStatement();

            String col1= "Country_ID, ";
            String col2 = "Country ";
            String table = "countries ";
            String cond = "Country_ID=";

            ResultSet sql_results  = sql_statement.executeQuery("SELECT " + col1+col2+"FROM " + table+"WHERE "+ cond + country_code );

            if (sql_results.next()) {
                return Optional.of(show_country(sql_results));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
    /**
     * @return all 3 countries, UK,US,CAN
     */
    @Override
    public ObservableList<Country> get_every() {

        try (Connection db_connect = MySQLConnector.open_sql_connection()) {

            Statement sql_statement = db_connect.createStatement();
            String col1 = "Country_ID, ";
            String col2 = "Country ";
            String table = "countries";

            ResultSet all_countries_sql_result =
                    sql_statement.executeQuery("SELECT " +col1+col2 + "FROM " + table);

            ObservableList<Country> countries_list = FXCollections.observableArrayList();

            while (all_countries_sql_result.next()) {
                Country cntry = show_country(all_countries_sql_result);
                countries_list.add(cntry);
            }

            return countries_list;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public boolean insert(Country c) {
        return false;
    }

    @Override
    public boolean update(Country c) {
        return false;
    }
}