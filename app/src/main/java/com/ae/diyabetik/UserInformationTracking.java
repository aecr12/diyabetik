package com.ae.diyabetik;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ae.DAO.InformationCallback;
import com.ae.DAO.UserInformationDAO;
import com.ae.Models.UserInformation;

import java.util.ArrayList;
import java.util.List;

public class UserInformationTracking extends AppCompatActivity {

    private TextView textViewTitle;
    private EditText editTextHeight, editTextWeight, editTextWaist, editTextHbA1cPercent;
    private Button buttonRegister;
    private Button buttonUpdate;
    private List<UserInformation> userInformationList;
    UserInformationDAO userInformationDAO = new UserInformationDAO();

    private boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private void isLegalHeight(EditText text) {
        CharSequence str = text.getText().toString();
        if (Integer.parseInt(String.valueOf(str)) > 250) {
            text.setError("Geçerli bir boy bilgisi giriniz.");
            return;
        }
    }

    private void isLegalWeight(EditText text) {
        CharSequence str = text.getText().toString();
        if (Integer.parseInt(String.valueOf(str)) > 400) {
            text.setError("Geçerli bir kilo bilgisi giriniz.");
            return;
        }
    }

    private void isLegalWaist(EditText text) {
        CharSequence str = text.getText().toString();
        if (Integer.parseInt(String.valueOf(str)) > 250) {
            text.setError("Geçerli bir değer giriniz.");
            return;
        }
    }

    private void isLegalDiyastolik(EditText text) {
        CharSequence str = text.getText().toString();
        if (Integer.parseInt(String.valueOf(str)) > 200) {
            text.setError("Geçerli bir değer giriniz.");
            return;
        }
    }

    private void isLegalSistolik(EditText text) {
        CharSequence str = text.getText().toString();
        if (Integer.parseInt(String.valueOf(str)) > 200) {
            text.setError("Geçerli bir değer giriniz.");
            return;
        }
    }

    private void isLegalBloodSugarValue(EditText text) {
        CharSequence str = text.getText().toString();
        if (Integer.parseInt(String.valueOf(str)) > 1000) {
            text.setError("Geçerli bir değer giriniz.");
            return;
        }
    }

    private void isLegalHbA1cPercent(EditText text) {
        CharSequence str = text.getText().toString();
        if (Integer.parseInt(String.valueOf(str)) > 100) {
            text.setError("Geçerli bir değer giriniz.");
            return;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userInformationList = new ArrayList<>();
        textViewTitle = findViewById(R.id.textViewTitle);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextWaist = findViewById(R.id.editTextWaist);
        editTextHbA1cPercent = findViewById(R.id.editTextHbA1cPercent);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        loadUserInformation();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(editTextHeight) || isEmpty(editTextWeight) || isEmpty(editTextWaist)  || isEmpty(editTextHbA1cPercent)) {
                    Toast.makeText(UserInformationTracking.this, "Tüm alanları eksiksiz doldurunuz.", Toast.LENGTH_LONG).show();
                    return;
                }
                isLegalHeight(editTextHeight);
                isLegalWeight(editTextWeight);
                isLegalWaist(editTextWaist);
                isLegalHbA1cPercent(editTextHbA1cPercent);
                saveUserInformation();
                Toast.makeText(UserInformationTracking.this, "Bilgileriniz başarıyla kaydedildi", Toast.LENGTH_LONG);
                Intent intent = new Intent(UserInformationTracking.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInformation(userInformationList.get(0));
                Toast.makeText(UserInformationTracking.this, "Bilgileriniz başarıyla güncellendi", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserInformationTracking.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    private void saveUserInformation() {
        String height = editTextHeight.getText().toString();
        String weight = editTextWeight.getText().toString();
        String waist = editTextWaist.getText().toString();
        String hbA1c = editTextHbA1cPercent.getText().toString();

        UserInformation userInformation = new UserInformation(null, height, weight, waist, hbA1c);
        userInformationDAO.create(userInformation);
        userInformationList.add(userInformation);
        Toast.makeText(UserInformationTracking.this, "Bilgileriniz başarıyla kaydedildi!", Toast.LENGTH_SHORT).show();

    }


    private void loadUserInformation() {
        userInformationDAO.read(userInformationList, new InformationCallback() {

            @Override
            public void onInformationLoaded(List informationList) {
                editTextHeight.setText(userInformationList.get(0).getHeight());
                editTextWeight.setText(userInformationList.get(0).getWeight());
                editTextWaist.setText(userInformationList.get(0).getWaist());
                editTextHbA1cPercent.setText(userInformationList.get(0).getHbA1cPercent());

                if (editTextHeight.getText().toString().trim().length()==0 || editTextWeight.getText().toString().trim().length()==0 || editTextWaist.getText().toString().trim().length()==0 || editTextHbA1cPercent.getText().toString().trim().length()==0) {
                    buttonRegister.setVisibility(View.VISIBLE);
                    buttonUpdate.setVisibility(View.GONE);
                } else {

                    buttonRegister.setVisibility(View.GONE);
                    buttonUpdate.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onInformationNotLoaded() {

            }
        });
    }

    private void updateUserInformation(UserInformation userInformationToUpdate) {
        userInformationToUpdate.setId(userInformationList.get(0).getId());
        userInformationToUpdate.setHeight(editTextHeight.getText().toString());
        userInformationToUpdate.setWeight(editTextWeight.getText().toString());
        userInformationToUpdate.setWaist(editTextWaist.getText().toString());
        userInformationToUpdate.setHbA1cPercent(editTextHbA1cPercent.getText().toString());
        userInformationDAO.update(userInformationToUpdate);
    }

}
