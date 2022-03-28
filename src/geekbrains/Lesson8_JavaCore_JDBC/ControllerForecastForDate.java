package geekbrains.Lesson8_JavaCore_JDBC;


import geekbrains.DBClass.WeatherData;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;


public class ControllerForecastForDate {

    DatabaseRepository databaseRepository = new DatabaseRepositorySQLiteImpl();

    public void onUserInput(String forecastForDate){
        createWeatherData();
        saveWeatherData();
        getForecastForDate(forecastForDate);
    }


    public void createWeatherData(){
        try {
            databaseRepository.createWeatherData();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void saveWeatherData(){
        try {

            databaseRepository.saveWeatherData(YandexWeatherProvider.getWeather);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void getForecastForDate(String forecastForDate){
        try {
            databaseRepository.getForecastForDate(forecastForDate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
