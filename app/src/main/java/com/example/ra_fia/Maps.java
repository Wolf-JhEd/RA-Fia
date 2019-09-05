package com.example.ra_fia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Maps extends AppCompatActivity {

    private Button mButtonSignOut;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mButtonSignOut = (Button) findViewById(R.id.btCierre);
        mAuth = FirebaseAuth.getInstance();

        mButtonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(Maps.this, Inicio.class));
                finish();
            }
        });

    }
}
