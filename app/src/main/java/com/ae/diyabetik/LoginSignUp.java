package com.ae.diyabetik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginSignUp extends AppCompatActivity {
    Button buttonLogin;
    Button buttonGoogleSignin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginSignUp.this, UserInformation.class);
                startActivity(intent);
            }
        });
        buttonGoogleSignin = findViewById(R.id.buttonGoogleSignIn);
        buttonGoogleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginSignUp.this, TreatmentChoice.class);
                startActivity(intent);
            }
        });
    }
}
