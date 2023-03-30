package com.ae.diyabetik;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Signup extends AppCompatActivity {
    ImageView imageViewLogo;
    EditText editTextFullname, editTextEmail, editTextPassword;
    Button buttonRegister;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        imageViewLogo = findViewById(R.id.imageViewLogo);
        editTextFullname = findViewById(R.id.editTextFullname);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
    }
}
