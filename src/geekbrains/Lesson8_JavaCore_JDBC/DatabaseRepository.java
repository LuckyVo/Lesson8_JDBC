package geekbrains.Lesson8_JavaCore_JDBC;


import geekbrains.DBClass.WeatherData;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseRepository {


    void createTableIfNotExists() throws SQLException, IOException;

    boolean saveWeatherData(WeatherData weatherData) throws SQLException, IOException;

    List<WeatherData> getForecastForDate(String date) throws IOException, SQLException;

    List<WeatherData> getAllWeatherFromDB() throws IOException, SQLException;
}
