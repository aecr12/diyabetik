package com.ae.diyabetik;

import android.os.Bundle;
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

        textViewTitle = findViewById(R.id.textViewTitle);
        checkInsulin = findViewById(R.id.checkInsulin);
        checkPump = findViewById(R.id.checkPump);
        checkOralAntidiabetic = findViewById(R.id.checkOralAntidiabetic);
        checkInsulinAntidiabetic = findViewById(R.id.checkInsulinAntidiabetic);
        buttonRegister = findViewById(R.id.buttonRegister);
    }
}
