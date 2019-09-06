package com.example.ra_fia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class Login extends AppCompatActivity {

    private EditText edEmail;
    private EditText edPassword;

    private Button mbutonLogin;

    private TextView textRedirectRegister;

    //VARIABLES DE DATOS QUE SE REGISTRARA
    private String email="";
    private String password="";

    //VARIABLES DE FIREBASE
    private FirebaseAuth mAuth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edEmail=(EditText)findViewById(R.id.editextEmail);
        edPassword=(EditText)findViewById(R.id.editexPassw);

        mbutonLogin=(Button)findViewById(R.id.btInicio);

        textRedirectRegister=(TextView)findViewById(R.id.tvRegister);

        mAuth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        mbutonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=edEmail.getText().toString();
                password=edPassword.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()){
                    loginUser();
                }else{
                    Toast.makeText(Login.this, "Complete los  campos correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textRedirectRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }

    private void loginUser(){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(Login.this, Maps.class));
                    getUserInfo();
                    finish();
                }else{
                    Toast.makeText(Login.this, "No se puedo iniciar sesion, compruebe los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getUserInfo(){

        String id=mAuth.getCurrentUser().getUid();
        database.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name=dataSnapshot.child("name").getValue().toString();

                    Toast.makeText(Login.this, "Bienvenido  "+ name, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
