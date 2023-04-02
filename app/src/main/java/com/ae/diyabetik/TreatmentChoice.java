package com.ae.diyabetik;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TreatmentChoice extends AppCompatActivity{

    private TextView textViewTitle;
    private CheckBox checkInsulin, checkPump, checkOralAntidiabetic, checkInsulinAntidiabetic;
    private Button buttonRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.treatment_choice);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewTitle = findViewById(R.id.textViewTitle);
        checkInsulin = findViewById(R.id.checkInsulin);
        checkPump = findViewById(R.id.checkPump);
        checkOralAntidiabetic = findViewById(R.id.checkOralAntidiabetic);
        checkInsulinAntidiabetic = findViewById(R.id.checkInsulinAntidiabetic);
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
