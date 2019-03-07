package de.proneucon.weatherapp;

import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText city;
    private ImageView image;
    private TextView temperatur, description;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!istNetzwerkVerfuegbar()) {
            finish();
        }
        setContentView(R.layout.activity_main);

        city = findViewById(R.id.et_stadt);
        image = findViewById(R.id.iv_image);
        temperatur = findViewById(R.id.tv_temperatur);
        description = findViewById(R.id.tv_beschreibung);

        button = findViewById(R.id.btn_anzeigen);

        button.setOnClickListener( (v) -> new Thread(() -> {
            WeatherData weather;
            Bitmap bitmapWeather;
            try {
                weather = WeatherUtils.getWeather(city.getText().toString());
                bitmapWeather = WeatherUtils.getImage(weather);
                runOnUiThread(() -> {
                    city.setText(weather.name);
                    image.setImageBitmap(bitmapWeather);
                    description.setText(weather.description);
                    Double temp = weather.temp - 273.15;
                    temperatur.setText(getString(R.string.temp_template, temp.intValue()));
                });
            } catch (Exception e) {
                Log.e(TAG, "getWeather()", e);
            }
        }).start());
        city.setOnEditorActionListener(
                (textView, i, keyEvent) -> {
                    button.performClick();
                    return true;
                });
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("city" , city);
//    }

    private boolean istNetzwerkVerfuegbar() {
        ConnectivityManager mgr =  null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            mgr = getSystemService(ConnectivityManager.class);
        }
        NetworkInfo info = mgr == null ? null : mgr.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
}