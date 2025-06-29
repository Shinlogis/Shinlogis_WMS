package com.shinlogis.wms.common.util.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;


public class WeatherFetcher {
    private static final String API_KEY = "2b8d62f9473dfd517777f5dfc9f3d3db"; 

    public static WeatherData fetch(String city) throws IOException {
        String urlStr = "https://api.openweathermap.org/data/2.5/weather?q=" + city +
                        "&appid=" + API_KEY + "&units=metric";
        URI uri = URI.create(urlStr);
        URL url = uri.toURL(); 
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine, response = "";
        while ((inputLine = in.readLine()) != null) response += inputLine;
        in.close();

        JSONObject json = new JSONObject(response);
        double temp = json.getJSONObject("main").getDouble("temp");
        long sunrise = json.getJSONObject("sys").getLong("sunrise");
        long sunset = json.getJSONObject("sys").getLong("sunset");
        String icon = json.getJSONArray("weather").getJSONObject(0).getString("icon");

        return new WeatherData(temp, sunrise, sunset, icon);
    }

    public static String formatTime(long timestamp) {
        Date date = new Date(timestamp * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }
}
