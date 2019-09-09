package com.example.ra_fia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {

    private  final int DURACION_SPLASH = 3000;

    private TextView tvLogo;
    private TextView tvNota;

    private Typeface script;
    private Typeface script1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        tvLogo = (TextView) findViewById(R.id.tvRafia);
        tvNota = (TextView) findViewById(R.id.tvNotas);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            };
        }, DURACION_SPLASH);

        String fuente="fuentes/raphtalia.ttf";
        String fuente1="fuentes/trackers.ttf";

        this.script = Typeface.createFromAsset(getAssets(),fuente);
        this.script1 = Typeface.createFromAsset(getAssets(),fuente1);

        tvLogo.setTypeface(script);
        tvNota.setTypeface(script1);

    }
}
