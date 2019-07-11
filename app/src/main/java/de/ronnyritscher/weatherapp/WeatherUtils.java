package de.ronnyritscher.weatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

class WeatherUtils {

    private static final String TAG = WeatherUtils.class.getSimpleName();
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q={0}&lang=de&appid={1}";
    private static final String WEATHER_KEY = "9e8e38421677a2c55f9463ebf6469662";

    // Für die JSON-DATEN
    private static final String NAME = "name";
    private static final String WEATHER = "weather";
    private static final String DESCRIPTION = "description";
    private static final String ICON = "icon";
    private static final String MAIN = "main";
    private static final String TEMP = "temp";


    //JSON-String -> Daten vom Server laden
    public static String getFromServer(String url) throws IOException {
        //TEST LOGD
        Log.d(TAG, "getFromServer: " + url);

        StringBuilder stringBuilder = new StringBuilder();
        java.net.URL _url = new URL(url);
        //öffnet die Connection der URL
        HttpURLConnection httpURLConnection = (HttpURLConnection) _url.openConnection();
        final int responseCode = httpURLConnection.getResponseCode();

        //Prüfen ob die Connection ok ist
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Erzeugt den InputStream
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            // solange die Zeile des BR nicht leer ist
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            //schließen des BR
            bufferedReader.close();
        }
        //schließen der Connection
        httpURLConnection.disconnect();

        //DEBUG: Inhalt des stringBuilders
        Log.d(TAG, "getFromServer: " + stringBuilder.toString());

        return stringBuilder.toString();
    }


    public static WeatherData getWeather(String city) throws JSONException, IOException {
        Log.d(TAG, "getWetter: ");

        //löschen vorhandener Daten zur Weiterverarbeitungen
        String name = null;
        String description = null;
        String icon = null;
        Double temp = null;

        //JSON-Objekt wird benötigt um JSON-Daten zu verarbeiten
        // MessageFormat()
        JSONObject jsonObject = new JSONObject(getFromServer(MessageFormat.format(WEATHER_URL, city, WEATHER_KEY)));


        //DATEN zur weiterverarbeitung von dem besorgten JSON-Objekt
        //1. EBENE
        if (jsonObject.has(NAME)) {
            name = jsonObject.getString(NAME);
        }
        //WENN in einem Array die Daten liegen, erst das JSON-Array besorgen
        //1.EBENE
        if (jsonObject.has(WEATHER)) {
            //2.EBENE
            JSONArray jsonArray = jsonObject.getJSONArray(WEATHER);
            if (jsonArray.length() > 0) {
                //Weiteres JsonObjekt für das Array WEATHER
                JSONObject jsonWeather = jsonArray.getJSONObject(0);
                // DATEN in der 2. Ebene
                if (jsonWeather.has(DESCRIPTION)) {
                    description = jsonWeather.getString(DESCRIPTION);
                }
                if (jsonWeather.has(ICON)) {
                    icon = jsonWeather.getString(ICON);

                }
            }
            //1.EBENE
            if (jsonObject.has(MAIN)) {
                JSONObject jsonMain = jsonObject.getJSONObject(MAIN);
                if (jsonMain.has(TEMP)) {
                    temp = jsonMain.getDouble(TEMP);
                }
            }


        }
        return new WeatherData(name, description, icon, temp);
    }

    public static Bitmap getImage(WeatherData w) throws IOException {
        URL req = new URL("http://openweathermap.org/img/w/" +
                w.icon + ".png");
        HttpURLConnection c = (HttpURLConnection) req.openConnection();
        Bitmap bmp = BitmapFactory.decodeStream(c.getInputStream());
        c.disconnect();
        return bmp;

    }
}