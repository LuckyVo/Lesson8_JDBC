package geekbrains.Lesson8_JavaCore_JDBC;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class ReverseGeocode {
    static Properties prop = new Properties();
    OkHttpClient client = new OkHttpClient();
    ObjectMapper objectMapper = new ObjectMapper();


    public String[] getCity()  throws IOException {
        loadProperties();
        String selectedCity = ApplicationGlobalCity.getInstance().getSelectedCity();

        HttpUrl mapQuestURL = new HttpUrl.Builder()
                .scheme("http")
                .host(prop.getProperty("BASE_HOST_MAP_QUEST"))
                .addPathSegment(prop.getProperty("GEOCODING_MAP_QUEST"))
                .addPathSegment(prop.getProperty("API_VERSION_MAP_QUEST"))
                .addPathSegment(prop.getProperty("ADDRESS"))
                .addQueryParameter("key", prop.getProperty("MAP_QUEST_DEVELOPER_API_KEY"))
                .addQueryParameter("location", selectedCity)
                .build();

        Request requestToMapQuestAPI = new Request.Builder()
                .addHeader("accept", "application/json")
                .url(mapQuestURL)
                .get()
                .build();

        Response response = client.newCall(requestToMapQuestAPI).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Невозможно прочесть информацию о городе. " +
                    "Код ответа сервера = " + response.code() + " тело ответа = " + response.body().string());
        }

        String jsonResponse = Objects.requireNonNull(response.body().string());

        if (objectMapper.readTree(jsonResponse).size() > 0) {
            JsonNode lat = objectMapper.readTree(jsonResponse).at("/results/0/locations/0/latLng/lat");
            JsonNode lon = objectMapper.readTree(jsonResponse).at("/results/0/locations/0/latLng/lng");
            return new String[]{String.valueOf(lat),String.valueOf(lon)};
        } else throw new IOException("Server returns 0 cities");

    }

    private static void loadProperties() throws IOException {
        try(FileInputStream configFile = new FileInputStream("src/resources/config.properties")){
            prop.load(configFile);
        }
    }
}
