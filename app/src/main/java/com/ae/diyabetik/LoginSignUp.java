package com.ae.diyabetik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginSignUp extends AppCompatActivity {
    TextView textViewSignUp;
    Button buttonLogin;
    Button buttonGoogleSignin;
    Button buttonAppleSigin;
    EditText editTextEmail;
    EditText editTextPassword;
    private String uid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                uid = user.getUid();

                                Intent intent = new Intent(LoginSignUp.this,MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginSignUp.this,"Giriş işlemi başarısız.",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
        buttonGoogleSignin = findViewById(R.id.buttonGoogleSignIn);
        buttonGoogleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        buttonAppleSigin = findViewById(R.id.buttonAppleSignIn);
        buttonAppleSigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        textViewSignUp = findViewById(R.id.textViewSignUp);
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginSignUp.this,
                        "Giriş işlemi başarılı. Ana sayfaya yönlendiriliyorsunuz",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginSignUp.this,Signup.class);
                startActivity(intent);
            }
        });
    }
}
