package com.ae.diyabetik;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ae.DAO.UserDAO;
import com.ae.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class Signup extends AppCompatActivity {
    private ImageView imageViewLogo;
    private EditText editTextFullname, editTextEmail, editTextPassword;
    private Button buttonRegister;
    private List<User> userList;
    private UserDAO userDAO = new UserDAO();
    private FirebaseAuth mAuth;

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        imageViewLogo = findViewById(R.id.imageViewLogo);
        editTextFullname = findViewById(R.id.editTextFullname);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        userList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        // register butonuna basınca bilgiler doğru girildiyse yapılcak işlemler
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(editTextEmail) || isEmpty(editTextFullname) || isEmpty(editTextPassword)) {
                    Toast.makeText(Signup.this, "Lütfen bütün alanları eksiksiz doldurun.", Toast.LENGTH_LONG).show();
                }
                if (isEmail(editTextEmail) == false) {
                    editTextEmail.setError("Geçerli bir mail adresi giriniz");
                    return;
                }
                String adSoyad = editTextFullname.getText().toString();
                String mail = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userDAO.create(new User(mAuth.getCurrentUser().getUid(), adSoyad, mail));
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Signup.this, "Kayıt başarıyla gerçekleşti. Doğrulama mailini onayladıktan sonra giriş yapabilirsiniz.", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(Signup.this, LoginSignUp.class));
                                        finish();
                                    } else {
                                        Toast.makeText(Signup.this, "Bir hata oluştu...", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(Signup.this, "Bir hata oluştu", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }
}