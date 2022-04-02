package geekbrains.Lesson8_JavaCore_JDBC;

import geekbrains.DBClass.WeatherData;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseRepositorySQLiteImpl implements DatabaseRepository {

    static Properties prop = new Properties();
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    String selectDateFromTable = "SELECT * FROM weather WHERE date_time = ?;";
    String selectAllTable = "SELECT * FROM weather;";
    String insertTable = "INSERT INTO weather (city, date_time, weather_text, temperature) VALUES (?,?,?,?);";
    String createTable = "CREATE TABLE IF NOT EXISTS weather (\n" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "city TEXT NOT NULL,\n" +
            "date_time TEXT NOT NULL,\n" +
            "weather_text TEXT NOT NULL,\n" +
            "temperature REAL NOT NULL\n" +
            ");";

    private Connection getConnection() throws SQLException, IOException {
        loadProperties();
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + prop.getProperty("DB_FILENAME"));
        return connection;
    }

    private void dropTable() throws SQLException, IOException {
        loadProperties();
        Connection connection = getConnection();
        connection.createStatement().executeUpdate("DROP TABLE IF EXISTS weather;");
    }

    private void closeConnection (){
        try {
            getConnection().close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTableIfNotExists() throws IOException, SQLException {
        dropTable();
        try (Connection connection = getConnection()) {
            connection.createStatement().execute(createTable);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean saveWeatherData(WeatherData weatherData) throws SQLException, IOException {
        try (Connection connection = getConnection();
             PreparedStatement saveWeather = connection.prepareStatement(insertTable)) {
            saveWeather.setString(1, weatherData.getCity());
            saveWeather.setString(2, weatherData.getLocalDate());
            saveWeather.setString(3, weatherData.getText());
            saveWeather.setLong(4, weatherData.getTemperature());
            return saveWeather.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new SQLException("Failure on saving weather object");
    }


    @Override
    public List<WeatherData> getForecastForDate(String forecastForDate) throws IOException, SQLException {
        PreparedStatement saveWeather = getConnection().prepareStatement(selectDateFromTable);
        saveWeather.setString(1, forecastForDate);
        ResultSet resultSet = saveWeather.executeQuery();
        List<WeatherData> weatherDataList = new ArrayList<>();
        while (resultSet.next()) {
            weatherDataList.add(new WeatherData(resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4),
                    resultSet.getLong(5)));
        }
        return weatherDataList;


    }

    public List<WeatherData> getAllWeatherFromDB() throws IOException, SQLException {
        ResultSet resultSet = getConnection().createStatement().executeQuery(selectAllTable);
        List<WeatherData> weatherDataList = new ArrayList<>();
        while (resultSet.next()) {
            weatherDataList.add(new WeatherData(resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4),
                    resultSet.getLong(5)));
        }
        return weatherDataList;
    }


    private static void loadProperties() throws IOException {
        try (FileInputStream configFile = new FileInputStream("src/resources/config.properties")) {
            prop.load(configFile);
        }
    }






}