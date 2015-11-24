package com.example.johnj_000.weather;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by johnj_000 on 23/11/2015.
 */
public class WeatherHttpClient {
    private static String BASE_URL ="http://api.openweathermap.org/data/2.5/weather?APPID=";
    private static String OPEN_WEATHER_MAP_API_KEY = "0b41c617fd53a47f4eb15f0267b3796c";
    public String getWeatherData(String location){
        HttpURLConnection conn = null;
        InputStream is = null;
        URL url = null;
        try {
            url = new URL(BASE_URL+OPEN_WEATHER_MAP_API_KEY+"&q="location);

        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        try{
           //Abrir Conexión con el servidor Remoto
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            //Leer respuesta
            StringBuffer buffer = new StringBuffer();
            is=conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while((line=br.readLine()) != null)
                buffer.append(line + "\r\n");
            is.close();
            conn.disconnect();
            return buffer.toString();
        }
        catch(Throwable t){
            t.printStackTrace();
        }
        finally{
            try{ is.close();}catch(Throwable t){}
            try{ conn.disconnect();}catch (Throwable t){}
        }
        return null;

    }
}
