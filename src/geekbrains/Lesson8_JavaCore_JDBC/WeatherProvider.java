package geekbrains.Lesson8_JavaCore_JDBC;


import geekbrains.DBClass.WeatherData;
import geekbrains.ENUM.*;

import java.io.IOException;
import java.util.ArrayList;


public interface WeatherProvider {

    ArrayList<WeatherData> getWeather(Periods periods) throws IOException;

//    WeatherData getAllFromDb() throws IOException;

}
