package com.ae.diyabetik;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ae.Adapters.BloodSugarAdapter;
import com.ae.DAO.BloodSugarDAO;
import com.ae.DAO.InformationCallback;
import com.ae.Models.BloodSugar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BloodSugarTracker extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    // Gerekli bileşenlerin initialize edilmesi
    private EditText editTextBloodSugarValue,editTextDate;
    private ImageButton imageButtonCalendar;
    private Button buttonSave;
    private int year, month, day, hour, minute;
    private RecyclerView recyclerView;
    private BloodSugarAdapter bloodSugarAdapter;
    private ArrayList<BloodSugar> bloodSugarList;
    private BloodSugarDAO bloodSugarDAO = new BloodSugarDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blood_sugar_tracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextBloodSugarValue = findViewById(R.id.editTextBloodSugarValue);
        editTextDate = findViewById(R.id.editTextDate);
        editTextDate.setEnabled(false);
        imageButtonCalendar = findViewById(R.id.imageButtonCalendar);
        buttonSave = findViewById(R.id.buttonSave);

        // recyvler viewin initalize edilmesi ve verinin listeden yüklenmesi
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bloodSugarList = new ArrayList<>();
        bloodSugarAdapter = new BloodSugarAdapter(bloodSugarList, this);
        recyclerView.setAdapter(bloodSugarAdapter);

        // İlk yüklenmede kayıtlı verilerin view üzerinde gösterilmesi
        // aşağıda yazılan metotta daha detaylı açıklanacak
        loadBloodSugarData();

        // takvim iconu
        imageButtonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                showDateTimePicker();
            }
        });

        // kaydet butonu
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBloodSugar();
            }
        });
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void showDateTimePicker() {

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        year = i;
        month = i1;
        day = i2;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hour = i;
        minute = i1;

        String selectedDateTime = day + "/" + (month + 1) + "/" + year + " " + String.format("%02d", hour) + ":" + String.format("%02d", minute);
        editTextDate.setText(selectedDateTime);
    }

    // kaydet butonuna basınca tetiklencek fonksiyon
    // veriler uygun formatta girilmişse viewa ve db ye ekleniyor
    private void saveBloodSugar() {
        String bloodSugarValue = editTextBloodSugarValue.getText().toString();
        String dateTime = editTextDate.getText().toString();

        if (TextUtils.isEmpty(bloodSugarValue) || TextUtils.isEmpty(dateTime)) {
            Toast.makeText(this, "Lütfen kan şekeri değerini ve tarihi giriniz.", Toast.LENGTH_SHORT).show();
            return;
        }
        BloodSugar bloodSugar = new BloodSugar(null, Integer.parseInt(bloodSugarValue), dateTime);
        bloodSugarDAO.create(bloodSugar);
        bloodSugarList.add(bloodSugar);
        bloodSugarAdapter.notifyDataSetChanged();
        editTextBloodSugarValue.setText("");
        editTextDate.setText("");
    }

    // db de kayıtlı verilerin sayfa açıldığında görüntülenmesi
    private void loadBloodSugarData(){
        bloodSugarDAO.read(bloodSugarList, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {
                bloodSugarAdapter.notifyDataSetChanged();
            }

            @Override
            public void onInformationNotLoaded() {

            }
        });
    }

    // geri butonu için menünün inflate edilmesi
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // geri butonuna asınca önceki sayfaya gidiliyor
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
