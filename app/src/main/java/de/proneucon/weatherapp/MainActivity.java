package de.proneucon.weatherapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //MEMBERS
    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText et_stadt;
    private Button btn_anzeigen;
    private ImageView iv_image;
    private TextView tv_temparatur , tv_beschreibung;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Prüfen ob das Netzwerk verfügbar ist
        if(!IstNetwerkVerfügbar()){
            Toast.makeText(this, getString(R.string.kein_Netzwerk), Toast.LENGTH_SHORT).show();
            finish();
        }

        //Referenzen initialisieren
        et_stadt = findViewById(R.id.et_stadt);
        iv_image = findViewById(R.id.iv_image);
        tv_temparatur = findViewById(R.id.tv_temperatur);
        tv_beschreibung = findViewById(R.id.tv_beschreibung);
        btn_anzeigen = findViewById(R.id.btn_anzeigen);



    }

    //--------------------------------------------------------
    private boolean IstNetwerkVerfügbar() {
        ConnectivityManager mgr = null;

        // Verzweigung für die API-Version  (M->Marschmellow Android6)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // nur für neuere SDK-Versionen
            mgr = getSystemService(ConnectivityManager.class);
        }else{
            // auch für ältere Versionen nutzbar  *Cast ist hier notwendig
            mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        // Gibt ne Info wenn es ein aktives Netzwerk gibt
        NetworkInfo info = mgr.getActiveNetworkInfo();
        // True wenn info connectet und nicht leer ist
        return info!=null && info.isConnected();
    }

    //Prüfen ob das Netzwerk verfügbar ist


}
