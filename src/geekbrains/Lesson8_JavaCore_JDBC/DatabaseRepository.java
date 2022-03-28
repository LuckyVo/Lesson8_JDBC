package geekbrains.Lesson8_JavaCore_JDBC;


import geekbrains.DBClass.WeatherData;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Репозиторий для работы
public interface DatabaseRepository {


    void createWeatherData() throws SQLException, IOException;

    void saveWeatherData(ArrayList<WeatherData> weatherData) throws SQLException, IOException;

    void getForecastForDate(String forecastForDate) throws IOException;


}
