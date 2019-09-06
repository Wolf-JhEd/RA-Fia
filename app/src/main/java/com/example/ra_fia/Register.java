package com.example.ra_fia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText textName;
    private EditText textEmail;
    private EditText textPassw;
    private EditText txtContraseñaRepetida;

    private Button bottonRegUsuario;
    private Button bottonRedirectLogin;

    //VARIABLES DE DATOS QUE SE REGISTRARA
    private String nombre = "";
    private String correo = "";
    private String contraseña = "";
    private String contraseñaRepetida = "";

    //VARIABLES DE FIREBASE
    FirebaseAuth mAuth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textName = (EditText) findViewById(R.id.editRegName);
        textEmail = (EditText) findViewById(R.id.editRegEmail);
        textPassw = (EditText) findViewById(R.id.editRegPassword);
        txtContraseñaRepetida = (EditText) findViewById(R.id.idRegistroContraseñaRepetida);

        bottonRegUsuario = (Button) findViewById(R.id.btnRegUser);
        bottonRedirectLogin = (Button) findViewById(R.id.btnRedirectLogin);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();


        bottonRegUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = textName.getText().toString();
                correo = textEmail.getText().toString();
                contraseña = textPassw.getText().toString();
                contraseñaRepetida = txtContraseñaRepetida.getText().toString();

                if (!nombre.isEmpty() && !correo.isEmpty() && !contraseña.isEmpty() && !contraseñaRepetida.isEmpty()) {
                    if (contraseña.equals(contraseñaRepetida)) {
                        if (contraseña.length() >= 6 && contraseña.length() <= 16) {
                            registerUser();
                        } else {
                            Toast.makeText(Register.this, "La contraseña debe tener un max de 6 caracteres", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Register.this, "La contraseña no coincide", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Register.this, "Completar todos los campos", Toast.LENGTH_SHORT).show();
                }

            }
        });

        bottonRedirectLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

    }

    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("name", nombre);
                    userMap.put("email", correo);
                    userMap.put("password", contraseña);

                    String id = mAuth.getCurrentUser().getUid();

                    database.child("Users").child(id).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()) {
                                startActivity(new Intent(Register.this, Maps.class));
                                Toast.makeText(Register.this, "Registro Correctamente", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(Register.this, "No se pudo registrar correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(Register.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}