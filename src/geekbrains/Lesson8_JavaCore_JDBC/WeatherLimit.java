package geekbrains.Lesson8_JavaCore_JDBC;

public class WeatherLimit {
    private static WeatherLimit INSTANCE;
    private String weatherLimit = null;


    public static WeatherLimit getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new WeatherLimit();
        }
        return INSTANCE;
    }

    public String getWeatherLimit() {return weatherLimit; }
    public void setWeatherLimit(String weatherLimit) {
        this.weatherLimit = weatherLimit;
    }

}
