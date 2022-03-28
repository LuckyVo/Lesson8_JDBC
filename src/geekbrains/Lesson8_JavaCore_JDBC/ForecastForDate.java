package geekbrains.Lesson8_JavaCore_JDBC;

public class ForecastForDate {

    private static ForecastForDate INSTANCE;
    private String forecastForDate = null;

    public static ForecastForDate getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ForecastForDate();
        }

        return INSTANCE;
    }

    public String getEnteredDate() {
        return forecastForDate;
    }

    public void setEnteredDate(String forecastForDate) {
        this.forecastForDate = forecastForDate;
    }
}
