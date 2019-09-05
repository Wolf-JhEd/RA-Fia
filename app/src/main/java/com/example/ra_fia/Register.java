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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText meditexName;
    private EditText meditextEmail;
    private EditText meditexPassword;

    private Button mbuttonRegUser;

    //VARIABLES DE DATOS QUE SE REGISTRARA
    private String name="";
    private String email="";
    private String password="";

    //VARIABLES DE FIREBASE
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        meditexName=(EditText)findViewById(R.id.editRegName);
        meditextEmail=(EditText)findViewById(R.id.editRegEmail);
        meditexPassword=(EditText)findViewById(R.id.editRegPassword);

        mbuttonRegUser=(Button)findViewById(R.id.btRegUser);

        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        mbuttonRegUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = meditexName.getText().toString();
                email = meditextEmail.getText().toString();
                password = meditexPassword.getText().toString();

                if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                   if(password.length() >=6){
                       registerUser();
                   }else{
                       Toast.makeText(Register.this, "El password debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                   }
                }else{
                    Toast.makeText(Register.this, "Debes completar los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void registerUser(){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Map<String, Object> mapUser = new HashMap<>();
                    mapUser.put("name", name);
                    mapUser.put("email", email);
                    mapUser.put("password", password);

                    String id = mAuth.getCurrentUser().getUid();

                    mDatabase.child("Users").child(id).setValue(mapUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(Register.this, Maps.class));
                                finish();
                            }else{
                                Toast.makeText(Register.this, "No se pudo crear los datos correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }   else{
                    Toast.makeText(Register.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
