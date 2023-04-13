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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class BloodSugarTracker extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private EditText editTextBloodSugarValue;
    private EditText editTextDate;
    private ImageButton imageButtonCalendar;
    private Button buttonSave;
    private int year, month, day, hour, minute;
    private RecyclerView recyclerView;
    private BloodSugarAdapter bloodSugarAdapter;
    private ArrayList<BloodSugar> bloodSugarList;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    String uid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blood_sugar_tracker);

        editTextBloodSugarValue = findViewById(R.id.editTextBloodSugarValue);
        editTextDate = findViewById(R.id.editTextDate);
        editTextDate.setEnabled(false);
        imageButtonCalendar = findViewById(R.id.imageButtonCalendar);
        buttonSave = findViewById(R.id.buttonSave);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bloodSugarList = new ArrayList<>();
        bloodSugarAdapter = new BloodSugarAdapter(bloodSugarList, this);
        recyclerView.setAdapter(bloodSugarAdapter);

        loadBloodSugarData();

        imageButtonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker();
            }
        });

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

    private void saveBloodSugar() {
        String bloodSugarValue = editTextBloodSugarValue.getText().toString();
        String dateTime = editTextDate.getText().toString();

        if (TextUtils.isEmpty(bloodSugarValue) || TextUtils.isEmpty(dateTime)) {
            Toast.makeText(this, "Lütfen kan şekeri değerini ve tarihi giriniz.", Toast.LENGTH_SHORT).show();
            return;
        }

        BloodSugar bloodSugar = new BloodSugar(null, Integer.parseInt(bloodSugarValue), dateTime);
        DatabaseReference bloodSugarRef = database.getReference("blood_sugar_data/" + uid).push(); // push() metodu kullanılarak yeni bir referans elde edilir.
        String bloodSugarId = bloodSugarRef.getKey(); // push() metodu ile oluşan yeni referansın key'i alınır ve medicineId'ye atanır.

        bloodSugar.setId(bloodSugarId);
        bloodSugarList.add(bloodSugar);
        bloodSugarRef.setValue(bloodSugar);
        bloodSugarAdapter.notifyDataSetChanged();

        editTextBloodSugarValue.setText("");
        editTextDate.setText("");
    }

    private void loadBloodSugarData(){
        DatabaseReference bloodSugarRef = FirebaseDatabase.getInstance().getReference().child("blood_sugar_data").child(uid);
        bloodSugarRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<BloodSugar> bloodSugars = new ArrayList<>();
                    for (DataSnapshot bloodSugarSnapshot : snapshot.getChildren()) {
                        String bloodSugarId = bloodSugarSnapshot.getKey(); // Push Id'yi al
                        BloodSugar bloodSugar = bloodSugarSnapshot.getValue(BloodSugar.class); // Verileri Medicine nesnesine çevir
                        bloodSugar.setId(bloodSugarId); // Push Id'yi Medicine nesnesine ekle
                        bloodSugars.add(bloodSugar);
                    }
                    bloodSugarList.clear();
                    bloodSugarList.addAll(bloodSugars);
                    bloodSugarAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
