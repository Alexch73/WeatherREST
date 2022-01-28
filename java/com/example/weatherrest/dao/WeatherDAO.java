package com.example.weatherrest.dao;

import com.example.weatherrest.models.Weather;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


@Component
public class WeatherDAO {
//подключение к базе

    private static Connection connection;

    static {
        Properties properties = new Properties();
        try {


            properties.load(new FileInputStream("D:\\java\\WeatherRest\\src\\main\\resources\\database.properties"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        final  String URL=properties.getProperty("url");
    final String USERNAME=properties.getProperty("username");
    final String PASSWORD=properties.getProperty("password");
       final String DRIVER=properties.getProperty("jdbc.drivers");
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection= DriverManager.getConnection(URL,USERNAME,PASSWORD);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public WeatherDAO()  {
    }


    // получения погоды
    public Weather show() throws IOException {
        java.util.Date dateNow = new Date();

        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
        Weather weather = new Weather();

        //поиск необходимых данных по соотвествующим неизменным тегам
        java.net.URL url = new URL("https://yandex.ru/");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
        String inputLine;
        int index, indexEnd;
        while ((inputLine = in.readLine()) != null) {
            index = inputLine.lastIndexOf("weather__content'><a aria-label=");
            indexEnd = inputLine.lastIndexOf("\" class=\"home-link home-link_black_yes weather__grade");
            if (index != -1) {
                inputLine = inputLine.substring(index + 33, indexEnd);

                break;
            }
        }

        try {
            //првоерка наличия  записи в бд на текущую дату
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM Weather_history";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                if ((resultSet.getString("weather_date")).equals(formatForDateNow.format(dateNow))) {
                    weather.setDate(resultSet.getString("weather_date"));
                    weather.setValue(resultSet.getString("weather_value"));
                    break;

                }
            }
            // Если запись не найдена, добавление новой записи в бд
            if (weather.getDate()==null) {
                SQL = "INSERT INTO Weather_history VALUES ('" + formatForDateNow.format(dateNow) + "','" + inputLine + "')";
                statement.executeUpdate(SQL);
                weather.setDate(formatForDateNow.format(dateNow));
                weather.setValue(inputLine);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return weather;
    }
}
