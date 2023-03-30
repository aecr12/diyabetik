package com.ae.diyabetik;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BloodSugar extends AppCompatActivity {

    private ArrayList<BloodSugarEntry> bloodSugarEntries;
    private ArrayAdapter<BloodSugarEntry> bloodSugarListAdapter;
    private ListView bloodSugarList;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blood_sugar_tracker);

        bloodSugarEntries = new ArrayList<>();
        bloodSugarListAdapter = new ArrayAdapter<>(this, R.layout.list_item1, bloodSugarEntries);
        bloodSugarList = findViewById(R.id.bloodSugarList);
        bloodSugarList.setAdapter(bloodSugarListAdapter);

        bloodSugarList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        // Kartı silmek için sola kaydırma işlemi
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() < e2.getX()) {
                    return false;
                }
                int position = bloodSugarList.pointToPosition((int) e1.getX(), (int) e1.getY());
                if (position != AdapterView.INVALID_POSITION) {
                    bloodSugarEntries.remove(position);
                    bloodSugarListAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        bloodSugarList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        // Kartı güncellemek için basılı tutma işlevi
        bloodSugarList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BloodSugar.this);
                builder.setTitle("Güncelle");
                builder.setMessage("Yeni Değeri Giriniz");
                final EditText input = new EditText(getApplicationContext());

                builder.setView(input);
                builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String inputText = input.getText().toString();
                        if(!inputText.isEmpty()){
                            try{
                                double updatedInput = Double.parseDouble(inputText);
                                BloodSugarEntry bloodSugarEntry = bloodSugarEntries.get(position);
                                bloodSugarEntries.set(position, new BloodSugarEntry(updatedInput,bloodSugarEntry.dateTime));

                            }catch (NumberFormatException e){
                                Toast.makeText(BloodSugar.this, "Lütfen sayısal bir değer girin.", Toast.LENGTH_SHORT).show();
                            }
                            bloodSugarListAdapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
                bloodSugarListAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    // Ekle butonuna tıklanınca çağrılır
    public void addBloodSugarEntry(View view) {
        EditText bloodSugarValueEditText = findViewById(R.id.bloodSugarValue);
        String bloodSugarValueString = bloodSugarValueEditText.getText().toString().trim();

        // Girilen değerin boş olup olmadığını kontrol eder
        if (bloodSugarValueString.isEmpty()) {
            Toast.makeText(this, "Lütfen bir kan şekeri değeri girin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Girilen değerin sayısal bir değer olup olmadığını kontrol eder
        double bloodSugarValue;
        try {
            bloodSugarValue = Double.parseDouble(bloodSugarValueString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Lütfen geçerli bir sayı girin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Yeni bir BloodSugarEntry oluşturur ve listeye ekler
        Date currentDate = new Date();
        BloodSugarEntry bloodSugarEntry = new BloodSugarEntry(bloodSugarValue, currentDate);
        bloodSugarEntries.add(bloodSugarEntry);

        // Girilen değeri sıfırlar ve liste görünümünü günceller
        bloodSugarValueEditText.setText("");
        bloodSugarListAdapter.notifyDataSetChanged();
    }


    public class BloodSugarEntry {
        private double bloodSugarValue;
        private Date dateTime;

        public BloodSugarEntry(double bloodSugarValue, Date dateTime) {
            this.bloodSugarValue = bloodSugarValue;
            this.dateTime = dateTime;
        }

        public double getBloodSugarValue() {
            return bloodSugarValue;
        }

        public void setBloodSugarValue(double bloodSugarValue) {
            this.bloodSugarValue = bloodSugarValue;
        }

        public Date getDateTime() {
            return dateTime;
        }


        @Override
        public String toString() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            String dateString = dateFormat.format(dateTime);
            return String.format(Locale.getDefault(), "%.1f mg/dL\n%s", bloodSugarValue, dateString);
        }
    }
}
