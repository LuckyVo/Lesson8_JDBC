package geekbrains.Lesson8_JavaCore_JDBC;

import com.fasterxml.jackson.databind.ObjectMapper;
import geekbrains.DBClass.WeatherData;
import geekbrains.ENUM.Periods;
import geekbrains.YandexWeather.Forecast;
import geekbrains.YandexWeather.WeatherResponse;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class YandexWeatherProvider implements WeatherProvider {

    static Properties prop = new Properties();
    OkHttpClient client = new OkHttpClient();
    ObjectMapper objectMapper = new ObjectMapper();
    ReverseGeocode reverseGeocode = new ReverseGeocode();
    DatabaseRepository databaseRepository = new DatabaseRepositorySQLiteImpl();

    @Override
    public void getWeather(Periods periods) throws IOException{
        loadProperties();
        String[] valueLatLon = reverseGeocode.getCity();
        String latValue = valueLatLon[0];
        String lonValue = valueLatLon[1];
        String limit = WeatherLimit.getInstance().getWeatherLimit();

        if (periods.equals(Periods.CUSTOM)) {
            HttpUrl yandexWeatherForecast = new HttpUrl.Builder()
                    .scheme("https")
                    .host(prop.getProperty("BASE_HOST_YANDEX"))
                    .addPathSegment(prop.getProperty("API_VERSION_YANDEX"))
                    .addPathSegment(prop.getProperty("FORECAST_YANDEX"))
                    .addQueryParameter("lat", latValue)
                    .addQueryParameter("lon", lonValue)
                    .addQueryParameter("lang", prop.getProperty("LANG_YANDEX"))
                    .addQueryParameter("limit", limit)
                    .addQueryParameter("hours", prop.getProperty("HOURS_YANDEX"))
                    .addQueryParameter("extra", prop.getProperty("EXTRA_YANDEX"))
                    .build();

            Request requestToYandexAPI = new Request.Builder()
                    .addHeader("accept", "application/json")
                    .addHeader("X-Yandex-API-Key", prop.getProperty("YANDEX_API_KEY"))
                    .url(yandexWeatherForecast)
                    .get()
                    .build();

            String jsonResponse = Objects.requireNonNull(client.newCall(requestToYandexAPI).execute().body().string());
            StringReader yandexWeatherJSON = new StringReader(jsonResponse);
            WeatherResponse weatherResponse = objectMapper.readValue(yandexWeatherJSON, WeatherResponse.class);


            try {
                databaseRepository.createTableIfNotExists();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String City = weatherResponse.getGeoObject().getLocality().getName();
            for (Forecast forecast : weatherResponse.getForecasts()) {
                WeatherData weatherData = new WeatherData(
                        City,
                        forecast.getDate(),
                        forecast.getParts().getDay().getCondition().getCondition(),
                        forecast.getParts().getDay().getTempAvg());

                try {
                    databaseRepository.saveWeatherData(weatherData);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if (periods.equals(Periods.DB_DATE)) {
            try {
                getDataFromDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (periods.equals(Periods.DB_ALL)) {
            try {
                getAllFromDb();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public List<WeatherData> getAllFromDb() throws SQLException, IOException  {
        List<WeatherData> weatherDataList = databaseRepository.getAllWeatherFromDB();
        printlnResult(weatherDataList);
        return weatherDataList;
    }

    public void getDataFromDB() throws SQLException, IOException {
        String date = ForecastForDate.getInstance().getEnteredDate();
        List<WeatherData> weatherDataList = databaseRepository.getForecastForDate(date);
        printlnResult(weatherDataList);
    }

    public void printlnResult(List<WeatherData> weatherDataList) {
        if (weatherDataList.size() == 0){
            System.out.println("Данные отсутствуют в базе");
        }else {
            for (WeatherData weatherData : weatherDataList) {
                String valveRes = "В городе " +
                        weatherData.getCity() +
                        " на дату " +
                        weatherData.getLocalDate() +
                        " ожидается " +
                        weatherData.getText() +
                        ", температура " +
                        weatherData.getTemperature();
                System.out.println(valveRes);
            }
        }
    }

    private static void loadProperties() throws IOException {
        try(FileInputStream configFile = new FileInputStream("src/resources/config.properties")){
            prop.load(configFile);
        }
    }

}
