package com.example.ra_fia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.net.Uri;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Context;
import android.support.v4.app.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    ImageView ImgUserPhoto;
    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;
    Uri pickedImgUri ;

    private EditText textName;
    private EditText textEmail;
    private EditText textPassw;
    private EditText txtContraseñaRepetida;

    private Button bottonRegUsuario;
    private Button bottonRedirectLogin;

    private ProgressBar loadingProgress;
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

        loadingProgress = findViewById(R.id.regProgressBar);

        loadingProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();


        bottonRegUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottonRegUsuario.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);

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
                    bottonRegUsuario.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                    Toast.makeText(Register.this, "Completar todos los campos", Toast.LENGTH_SHORT).show();
                }

            }
        });

        bottonRedirectLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });

        ImgUserPhoto = findViewById(R.id.regUserPhoto) ;

        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 29) {

                    checkAndRequestForPermission();
                }
                else
                {
                    openGallery();
                }
            }
        });

    }

    //Final

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
                                startActivity(new Intent(Register.this, Home.class));
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

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(Register.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Register.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(Register.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(Register.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }
        else
            openGallery();

    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            ImgUserPhoto.setImageURI(pickedImgUri);
        }


    }

}