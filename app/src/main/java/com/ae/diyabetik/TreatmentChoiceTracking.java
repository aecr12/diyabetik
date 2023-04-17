package com.ae.diyabetik;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ae.DAO.InformationCallback;
import com.ae.DAO.TreatmentChoiceDAO;
import com.ae.Models.TreatmentChoice;

import java.util.ArrayList;
import java.util.List;

public class TreatmentChoiceTracking extends AppCompatActivity {

    private TextView textViewTitle;
    private CheckBox checkInsulin, checkPump, checkOralAntidiabetic, checkInsulinAntidiabetic;
    private Button buttonRegister;
    private Button buttonUpdate;
    private boolean insulinChecked, pumpChecked, oralAntidiabeticChecked, insulinAntidiabeticChecked;
    private List<TreatmentChoice> treatmentChoiceList;
    TreatmentChoiceDAO treatmentChoiceDAO = new TreatmentChoiceDAO();

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
        buttonUpdate = findViewById(R.id.buttonUpdate);
        treatmentChoiceList = new ArrayList<>();
        loadTreatmentChoice();
        checkInsulin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insulinChecked = checkInsulin.isChecked();
            }
        });
        checkPump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pumpChecked = checkPump.isChecked();
            }
        });
        checkOralAntidiabetic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oralAntidiabeticChecked = checkOralAntidiabetic.isChecked();
            }
        });
        checkInsulinAntidiabetic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insulinAntidiabeticChecked = checkInsulinAntidiabetic.isChecked();
            }
        });


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTreatmentChoice();
                Toast.makeText(TreatmentChoiceTracking.this, "Bilgileriniz başarıyla kaydedildi", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TreatmentChoiceTracking.this, MainActivity.class);
                startActivity(intent);
            }
        });
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTreatmentChoice(treatmentChoiceList.get(0));
                Toast.makeText(TreatmentChoiceTracking.this, "Bilgileriniz başarıyla güncellendi", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TreatmentChoiceTracking.this, MainActivity.class);
                startActivity(intent);
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

    private void saveTreatmentChoice() {
        TreatmentChoice treatmentChoice = new TreatmentChoice(null, insulinChecked, pumpChecked, oralAntidiabeticChecked, insulinAntidiabeticChecked);
        treatmentChoiceDAO.create(treatmentChoice);
        treatmentChoiceList.add(treatmentChoice);
    }

    private void loadTreatmentChoice() {
        treatmentChoiceDAO.read(treatmentChoiceList, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {
                insulinChecked = treatmentChoiceList.get(0).isInsulin();
                checkInsulin.setChecked(insulinChecked);
                pumpChecked = treatmentChoiceList.get(0).isPump();
                checkPump.setChecked(pumpChecked);
                oralAntidiabeticChecked = treatmentChoiceList.get(0).isOralAntidiabetic();
                checkOralAntidiabetic.setChecked(oralAntidiabeticChecked);
                insulinAntidiabeticChecked = treatmentChoiceList.get(0).isInsulinAntidiabetic();
                checkInsulinAntidiabetic.setChecked(insulinAntidiabeticChecked);

                buttonRegister.setVisibility(View.GONE);
                buttonUpdate.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateTreatmentChoice(TreatmentChoice treatmentChoiceToUpdate) {
        treatmentChoiceToUpdate.setId(treatmentChoiceList.get(0).getId());
        treatmentChoiceToUpdate.setInsulin(checkInsulin.isChecked());
        treatmentChoiceToUpdate.setPump(checkPump.isChecked());
        treatmentChoiceToUpdate.setOralAntidiabetic(checkOralAntidiabetic.isChecked());
        treatmentChoiceToUpdate.setInsulinAntidiabetic(checkInsulinAntidiabetic.isChecked());

        treatmentChoiceDAO.update(treatmentChoiceToUpdate);
    }
}
