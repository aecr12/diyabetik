package com.ae.diyabetik;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ae.DAO.UserDAO;
import com.ae.Models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class LoginSignUp extends AppCompatActivity {
    private TextView textViewSignUp;
    private Button buttonLogin, buttonGoogleSignin;
    private EditText editTextEmail, editTextPassword;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = firebaseDatabase.getReference("users");
    private UserDAO userDAO = new UserDAO();
    private String providerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        mAuth = FirebaseAuth.getInstance();

        // google girişi için gerekli sign in bilgileri
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("811713593939-3t5k4k822u98lld2fb0n7iccjolp45pf.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // giriş istemcisinin initalize edilmesi
        googleSignInClient = GoogleSignIn.getClient(LoginSignUp.this, googleSignInOptions);

        // login butonuna basınca firebase email-password ile giriş apisi tetikleniyor
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null) {
                    List<? extends UserInfo> providerData = mAuth.getCurrentUser().getProviderData();
                    for (UserInfo userInfo : providerData) {
                        mAuth.getCurrentUser().reload();
                        String providerId = userInfo.getProviderId();
                        // email onaylandıysa kullanıcı sisteme dahil ediliyor
                        if (providerId.equals(EmailAuthProvider.PROVIDER_ID) && mAuth.getCurrentUser().isEmailVerified() == true) {
                            String email = editTextEmail.getText().toString();
                            String password = editTextPassword.getText().toString();
                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            mAuth.getCurrentUser();
                                            Intent intent = new Intent(LoginSignUp.this, MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(LoginSignUp.this, "Giriş işlemi başarısız.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(LoginSignUp.this, "Lütfen mail adresinizi doğrulayın", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginSignUp.this, "Girdiğiniz bilgilere ilişkin Kullanıcı bulunamadı", Toast.LENGTH_LONG).show();
                }

            }
        });

        // google giriş butonuna dokunulunca google sign in cliente yönlendirme
        buttonGoogleSignin = findViewById(R.id.buttonGoogleSignIn);
        buttonGoogleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, 100);
            }
        });

        // henüz kayııt yoksa, kayıt sayfasına yönlendirme
        textViewSignUp = findViewById(R.id.textViewSignUp);
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginSignUp.this,
                        "Kayıt sayfasına yönlendiriliyorsunuz", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginSignUp.this, Signup.class);
                startActivity(intent);
                finish();
            }
        });

        // giriş yapmış kullanıcının tekrar giriş yapmaması için kontrol
        if (mAuth.getCurrentUser()!=null){
            List<? extends UserInfo> providerData = mAuth.getCurrentUser().getProviderData();
            for (UserInfo userInfo : providerData) {
                providerId = userInfo.getProviderId();
            }
            mAuth.getCurrentUser().reload();
            if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified() == true && providerId.equals(EmailAuthProvider.PROVIDER_ID)) {
                startActivity(new Intent(LoginSignUp.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            } else if (mAuth.getCurrentUser() != null && providerId.equals(GoogleAuthProvider.PROVIDER_ID)) {
                startActivity(new Intent(LoginSignUp.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }

    }

    // google giriş işlemlerinin yapıldığı result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (signInAccountTask.isSuccessful()) {
                String s = "Google sign in successful";
                displayToast(s);
                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    if (googleSignInAccount != null) {
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User user = new User();
                                    String name = googleSignInAccount.getDisplayName();
                                    user.setId(task.getResult().getUser().getUid());
                                    user.setMail(task.getResult().getUser().getEmail());
                                    user.setAdSoyad(name);
                                    userDAO.create(user);
                                    startActivity(new Intent(LoginSignUp.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                } else {
                                    displayToast("Doğrulama başarısız :" + task.getException().getMessage());
                                }
                            }
                        });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


