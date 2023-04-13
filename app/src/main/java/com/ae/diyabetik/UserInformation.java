package com.ae.diyabetik;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserInformation extends AppCompatActivity {

    private TextView textViewTitle;
    private EditText editTextHeight, editTextWeight, editTextWaist, editTextBloodSugarValue, editTextHbA1cPercent, editTextSistolik, editTextDiyastolik;
    private Button buttonRegister;

    private boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private void isLegalHeight(EditText text){
        CharSequence str = text.getText().toString();
        if(Integer.parseInt(String.valueOf(str))>250){
            text.setError("Geçerli bir boy bilgisi giriniz.");
            return;
        }
    }

    private void isLegalWeight(EditText text){
        CharSequence str = text.getText().toString();
        if(Integer.parseInt(String.valueOf(str))>400){
            text.setError("Geçerli bir kilo bilgisi giriniz.");
            return;
        }
    }
    private void isLegalWaist(EditText text){
        CharSequence str = text.getText().toString();
        if(Integer.parseInt(String.valueOf(str))>250){
            text.setError("Geçerli bir değer giriniz.");
            return;
        }
    }

    private void isLegalDiyastolik(EditText text){
        CharSequence str = text.getText().toString();
        if(Integer.parseInt(String.valueOf(str))>200){
            text.setError("Geçerli bir değer giriniz.");
            return;
        }
    }

    private void isLegalSistolik(EditText text){
        CharSequence str = text.getText().toString();
        if(Integer.parseInt(String.valueOf(str))>200){
            text.setError("Geçerli bir değer giriniz.");
            return;
        }
    }

    private void isLegalBloodSugarValue(EditText text){
        CharSequence str = text.getText().toString();
        if(Integer.parseInt(String.valueOf(str))>1000){
            text.setError("Geçerli bir değer giriniz.");
            return;
        }
    }

    private void isLegalHbA1cPercent(EditText text){
        CharSequence str = text.getText().toString();
        if(Integer.parseInt(String.valueOf(str))>100){
            text.setError("Geçerli bir değer giriniz.");
            return;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewTitle = findViewById(R.id.textViewTitle);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextWaist = findViewById(R.id.editTextWaist);
        editTextBloodSugarValue = findViewById(R.id.editTextBloodSugarValue);
        editTextHbA1cPercent = findViewById(R.id.editTextHbA1cPercent);
        editTextSistolik = findViewById(R.id.editTextSistolik);
        editTextDiyastolik = findViewById(R.id.editTextDiyastolik);
        buttonRegister = findViewById(R.id.buttonRegister);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty(editTextHeight) ||isEmpty(editTextWeight) || isEmpty(editTextWaist) || isEmpty(editTextDiyastolik) || isEmpty(editTextSistolik) || isEmpty(editTextBloodSugarValue)|| isEmpty(editTextHbA1cPercent) ){
                    Toast.makeText(UserInformation.this, "Tüm alanları eksiksiz doldurunuz.", Toast.LENGTH_LONG).show();
                    return;
                }
                isLegalHeight(editTextHeight);
                isLegalWeight(editTextWeight);
                isLegalWaist(editTextWaist);
                isLegalDiyastolik(editTextDiyastolik);
                isLegalSistolik(editTextSistolik);
                isLegalBloodSugarValue(editTextBloodSugarValue);
                isLegalHbA1cPercent(editTextHbA1cPercent);
            }
        });
    }

    // geri butonu için menünün inflate edilmesi
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
