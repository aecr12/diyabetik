package com.ae.diyabetik;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
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

import com.ae.DAO.MedicineDAO;
import com.ae.Models.Medicine;
import java.util.ArrayList;
import java.util.Calendar;

public class MedicineTracker extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private EditText editTextMedicineName;
    private EditText editTextTakenTime;
    private ImageButton imageButtonCalendar;
    private Button buttonSave;
    private int year, month, day, hour, minute;
    private RecyclerView recyclerView;
    private MedicineAdapter medicineAdapter;
    private ArrayList<Medicine> medicineList;

    MedicineDAO medicineDAO = new MedicineDAO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_tracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextMedicineName = findViewById(R.id.editTextMedicineName);
        editTextTakenTime = findViewById(R.id.editTextTakenTime);
        editTextTakenTime.setEnabled(false);
        imageButtonCalendar = findViewById(R.id.imageButtonCalendar);
        buttonSave = findViewById(R.id.buttonSave);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicineList = new ArrayList<>();
        medicineAdapter = new MedicineAdapter(medicineList,this);
        recyclerView.setAdapter(medicineAdapter);

        loadMedicineData();

        imageButtonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker();
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMedicine();
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
        editTextTakenTime.setText(selectedDateTime);
    }

    private void saveMedicine() {
        String medicineName = editTextMedicineName.getText().toString();
        String takenTime = editTextTakenTime.getText().toString();

        if (TextUtils.isEmpty(medicineName) || TextUtils.isEmpty(takenTime)) {
            Toast.makeText(this, "Lütfen ilaç adı ve tarihi giriniz.", Toast.LENGTH_SHORT).show();
            return;
        }
        Medicine medicine = new Medicine(null, medicineName, takenTime); // id null olarak ayarlandı
        medicineDAO.create(medicine);
        medicineList.add(medicine);
        medicineAdapter.notifyDataSetChanged();

        editTextMedicineName.setText("");
        editTextTakenTime.setText("");
    }

    private void loadMedicineData() {
        medicineDAO.read(medicineList,medicineAdapter);

    }

}