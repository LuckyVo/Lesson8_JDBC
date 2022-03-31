package geekbrains.Lesson8_JavaCore_JDBC;

import geekbrains.ENUM.*;

import java.io.IOException;

public interface WeatherProvider {

    void getWeather(Periods periods) throws IOException;


}
