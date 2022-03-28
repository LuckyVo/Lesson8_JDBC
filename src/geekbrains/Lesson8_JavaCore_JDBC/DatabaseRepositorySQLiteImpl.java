package geekbrains.Lesson8_JavaCore_JDBC;

import geekbrains.DBClass.WeatherData;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseRepositorySQLiteImpl implements DatabaseRepository {

    static Properties prop = new Properties();

    @Override
    public void createWeatherData() throws SQLException, IOException  {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        dropTable();
        createTableIfNotExists();
    }

    private Connection getConnection() throws SQLException, IOException {
        loadProperties();
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + prop.getProperty("DB_FILENAME"));
        return connection;
    }

    private void dropTable() throws SQLException, IOException {
        loadProperties();
        Connection connection = getConnection();
        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS " + prop.getProperty("WEATHER_TABLE"));
    }

    private void createTableIfNotExists() throws IOException {
        loadProperties();
        try (Connection connection = getConnection()) {
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS " + prop.getProperty("WEATHER_TABLE") +
                    " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "city TEXT NOT NULL, " +
                    "date_time TEXT NOT NULL, " +
                    "weather_text TEXT NOT NULL, " +
                    "temperature INTEGER NOT NULL " +
                    ");");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void saveWeatherData(ArrayList<WeatherData> weatherData) throws SQLException, IOException {
        loadProperties();
        try (Connection connection = getConnection();
             PreparedStatement saveWeather = connection.prepareStatement
                     ("INSERT INTO " + prop.getProperty("WEATHER_TABLE") +
                         " (city, date_time, weather_text, temperature) VALUES (?,?,?,?);")) {
            saveWeather.setString(1, String.valueOf(weatherData.get(1)));
            saveWeather.setString(2, String.valueOf(weatherData.get(2)));
            saveWeather.setString(3, String.valueOf(weatherData.get(3)));
            saveWeather.setInt(4, Integer.parseInt(String.valueOf(weatherData.get(4))));
            saveWeather.addBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new SQLException("Failure on saving weather object");
    }


    @Override
    public void getForecastForDate(String forecastForDate) throws IOException {
        loadProperties();
        try (Connection connection = getConnection();
             ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM " + prop.getProperty("WEATHER_TABLE"))) {
            while (resultSet.next()) {
                System.out.println(
                        resultSet.getString(1) + " | " +
                                resultSet.getString(2) + " | " +
                                resultSet.getString(3) + " | " +
                                resultSet.getDouble(4) + " | "
                );
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void loadProperties() throws IOException {
        try (FileInputStream configFile = new FileInputStream("src/resources/config.properties")) {
            prop.load(configFile);
        }
    }
}