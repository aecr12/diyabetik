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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ae.DAO.UserDAO;
import com.ae.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class Signup extends AppCompatActivity {
    ImageView imageViewLogo;
    EditText editTextFullname, editTextEmail, editTextPassword;
    Button buttonRegister;
    List<User> userList;
    UserDAO userDAO = new UserDAO();
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        imageViewLogo = findViewById(R.id.imageViewLogo);
        editTextFullname = findViewById(R.id.editTextFullname);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        userList = new ArrayList<>();

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
                User user = new User(null,adSoyad, mail, password);

                mAuth.createUserWithEmailAndPassword(user.getMail(), user.getPassword())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                if (firebaseUser != null) {
                                    firebaseUser.sendEmailVerification()
                                            .addOnCompleteListener(emailVerificationTask -> {
                                                if (emailVerificationTask.isSuccessful()) {
                                                    // Doğrulama e-postası başarıyla gönderildi
                                                    Toast.makeText(Signup.this, "Kaydınız başarıyla gerçekleşti. Lütfen e-postanızı kontrol ederek hesabınızı doğrulayın.", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(Signup.this, LoginSignUp.class);
                                                    startActivity(intent);
                                                } else {
                                                    // Doğrulama e-postası gönderilemedi
                                                    Toast.makeText(Signup.this, "Doğrulama e-postası gönderilemedi. Lütfen daha sonra tekrar deneyin.", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                } else {
                                    // Kullanıcı bulunamadı
                                    Toast.makeText(Signup.this, "Kullanıcı oluşturulurken bir hata oluştu. Lütfen daha sonra tekrar deneyin.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                // Kullanıcı oluşturulamadı
                                Toast.makeText(Signup.this, "Kaydınız gerçekleştirilemedi. Lütfen daha sonra tekrar deneyin.", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

    }
}