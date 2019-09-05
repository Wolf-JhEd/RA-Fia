package com.example.ra_fia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Inicio extends AppCompatActivity {

    private TextView tvBienvenido;
    private TextView tvLogo;
    private TextView tvNota;

    private Button mbtnLogin;
    private Button mbtnRegistro;

    private Typeface script;
    private Typeface script1;

    //VARIABLES DE FIREBASE
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        tvBienvenido = (TextView) findViewById(R.id.tvBienvenidos);
        tvLogo = (TextView) findViewById(R.id.tvRafia);
        tvNota = (TextView) findViewById(R.id.tvNotas);

        mAuth= FirebaseAuth.getInstance();

        mbtnLogin = (Button) findViewById(R.id.btnSendToLogin);
        mbtnRegistro = (Button) findViewById(R.id.btnSendToRegister);

        mbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Inicio.this,Login.class));
            }
        });

        mbtnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Inicio.this,Register.class));
            }
        });

        String fuente="fuentes/raphtalia.ttf";
        String fuente1="fuentes/trackers.ttf";

        this.script = Typeface.createFromAsset(getAssets(),fuente);
        this.script1 = Typeface.createFromAsset(getAssets(),fuente1);

        tvLogo.setTypeface(script);
        tvNota.setTypeface(script1);
        tvBienvenido.setTypeface(script);


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(Inicio.this, Maps.class));
            finish();
        }
    }
}
