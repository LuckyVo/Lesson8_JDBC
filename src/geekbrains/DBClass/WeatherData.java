package geekbrains.DBClass;

import geekbrains.Lesson8_JavaCore_JDBC.ForecastForDate;

public class WeatherData{

    private String city;
    private String localDate;
    private String text;
    private Integer temperature;


    public WeatherData(String city, String localDate, String text, Integer temperature) {
        this.city = city;
        this.localDate = localDate;
        this.text = text;
        this.temperature = temperature;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocalDate() {
        return localDate;
    }

    public void setLocalDate(String localDate) {
        this.localDate = localDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

}
