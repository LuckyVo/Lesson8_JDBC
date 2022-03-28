package geekbrains.Lesson8_JavaCore_JDBC;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;

public class UserInterface {

    private Controller controller = new Controller();
    private ControllerForecastForDate controllerForecastForDate = new ControllerForecastForDate();
    private ApplicationGlobalCity applicationGlobalCity = new ApplicationGlobalCity();

    public void runApplication(){

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите название города на английском языке:");
            String city = scanner.nextLine();
            setCity(city);

            System.out.println("Выполнить запрос погоды у метеостанции на следующее количество дней (максимальное количество 7 дней):");
            String limit = scanner.nextLine();
            setLimit(limit);

            try {
                validateUserInputLimit(limit);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            try {
                notifyController(limit);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            System.out.println("Если желаете завершить работу программы, то введите: выход/exit:");
            String exit = scanner.nextLine();
            checkIsExit(exit);

            System.out.println("Выберите дату для вывода прогноза погоды в пределах запроса(формат YYYY-MM-DD):");
            String forecastForDate = scanner.nextLine();
            setDate(forecastForDate);

            try {
                validateUserInputForecastForDate(forecastForDate);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            try {
                notifyControllerForecastForDate(forecastForDate);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }


        }
    }

    private void checkIsExit(String exit) {
        if (exit.toLowerCase().equals("выход") || exit.toLowerCase().equals("exit")) {
            System.out.println("Завершаю работу");
            System.exit(0);
        }
    }

    private void setCity(String city) {
        applicationGlobalCity.getInstance().setSelectedCity(city);
    }

    private void validateUserInputLimit(String userInput) throws IOException {
        if (userInput == null || userInput.length() != 1) {
            throw new IOException("Попробуйте ввести данные ещё раз от 1 до 7!");
        }

        if (Integer.parseInt(userInput) < 1 || Integer.parseInt(userInput) > 7) {
            throw new IOException("Попробуйте ввести данные ещё раз от 1 до 7!");
        }

        int answer = 0;
        try {
            answer = Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            throw new IOException("Стоит ввести целое число!");
        }
    }

    private void notifyController(String limit) throws IOException{
        controller.onUserInput(limit);
    }

    private void notifyControllerForecastForDate(String forecastForDate) throws IOException{
        controllerForecastForDate.onUserInput(forecastForDate);
    }

    public void setDate(String forecastForDate) { ForecastForDate.getInstance().setEnteredDate(forecastForDate); }

    public void setLimit(String limit) { WeatherLimit.getInstance().setWeatherLimit(limit); }

    private void validateUserInputForecastForDate (String forecastForDate) throws IOException {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd")
                .withResolverStyle(ResolverStyle.STRICT);

        try {
            dateFormatter.parse(forecastForDate);
        } catch (DateTimeParseException e) {
            throw new IOException("Ввели число не в том формате!");
        }

    }
}