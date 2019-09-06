package com.example.ra_fia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ra_fia.Entidades.Usuarios;
import com.example.ra_fia.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText textName;
    private EditText textEmail;
    private EditText textPassw;
    private  EditText txtContraseñaRepetida;

    private Button bottonRegUsuario;


    //VARIABLES DE FIREBASE
    FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textName=(EditText)findViewById(R.id.editRegName);
        textEmail=(EditText)findViewById(R.id.editRegEmail);
        textPassw=(EditText)findViewById(R.id.editRegPassword);
        txtContraseñaRepetida = (EditText) findViewById(R.id.idRegistroContraseñaRepetida);

        bottonRegUsuario=(Button)findViewById(R.id.btnRegUser);

        mAuth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();

        bottonRegUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String correo = textEmail.getText().toString();
                final String nombre = textName.getText().toString();
                if(isValidEmail(correo) && validarContraseña() && validarNombre(nombre)){
                    String contraseña = textPassw.getText().toString();
                    mAuth.createUserWithEmailAndPassword(correo, contraseña)
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(Register.this, "Se registro correctamente.", Toast.LENGTH_SHORT).show();
                                        Usuarios usuario = new Usuarios();
                                        usuario.setCorreo(correo);
                                        usuario.setNombre(nombre);
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        DatabaseReference reference = database.getReference("Usuarios/"+currentUser.getUid());
                                        reference.setValue(usuario);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Register.this, "Error al registrarse.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(Register.this, "Validaciones funcionando.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean validarContraseña(){
        String contraseña,contraseñaRepetida;
        contraseña = textPassw.getText().toString();
        contraseñaRepetida = txtContraseñaRepetida.getText().toString();
        if(contraseña.equals(contraseñaRepetida)){
            if(contraseña.length()>=6 && contraseña.length()<=16){
                return true;
            }else return false;
        }else return false;
    }

    public boolean validarNombre(String nombre){
        return !nombre.isEmpty();
    }


}