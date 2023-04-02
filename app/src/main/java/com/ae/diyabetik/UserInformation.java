package com.ae.diyabetik;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserInformation extends AppCompatActivity {

    private TextView textViewTitle;
    private EditText editTextHeight, editTextWeight, editTextWaist, editTextBloodSugar, editTextHbA1c, editTextSistolik, editTextDiyastolik;
    private Button buttonRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewTitle = findViewById(R.id.textViewTitle);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextWaist = findViewById(R.id.editTextWaist);
        editTextBloodSugar = findViewById(R.id.editTextBloodSugar);
        editTextHbA1c = findViewById(R.id.editTextHbA1c);
        editTextSistolik = findViewById(R.id.editTextSistolik);
        editTextDiyastolik = findViewById(R.id.editTextDiyastolik);
        buttonRegister = findViewById(R.id.buttonRegister);
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
