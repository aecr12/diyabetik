package com.ae.diyabetik;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class MedicineTracker extends AppCompatActivity {

    private ArrayList<MedicineTracker.MedicineEntry> medicineEntries;
    private ArrayAdapter<MedicineTracker.MedicineEntry> medicineListAdapter;
    private ListView medicineList;
    private Button addButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_tracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMedicineEntry(v);
            }
        });

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String formattedTime = formatter.format(currentTime);
        EditText dateEditText = findViewById(R.id.dateEditText);
        dateEditText.setText(formattedTime);

        medicineEntries = new ArrayList<>();
        medicineListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicineEntries);
        medicineList = findViewById(R.id.medicineList);
        medicineList.setAdapter(medicineListAdapter);

        medicineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                int position = medicineList.pointToPosition((int) e1.getX(), (int) e1.getY());
                if (position != AdapterView.INVALID_POSITION) {
                    medicineEntries.remove(position);
                    medicineListAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        medicineList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        // Kartı güncellemek için basılı tutma işlevi
        medicineList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MedicineTracker.this);
                builder.setTitle("Değeri Güncelle");

                final EditText input = new EditText(getApplicationContext());
                input.setHint("İlaç Adı");

                ScrollView scrollView = new ScrollView(MedicineTracker.this);
                LinearLayout layout = new LinearLayout(getApplicationContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                final DatePicker datePicker = new DatePicker(getApplicationContext());
                final TimePicker timePicker = new TimePicker(getApplicationContext());
                timePicker.setIs24HourView(true);

                // LinearLayout'a tarih/saat seçicilerini ekleyin
                layout.addView(input);
                layout.addView(datePicker);
                layout.addView(timePicker);
                scrollView.addView(layout);
                builder.setView(scrollView);

                builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String inputText = input.getText().toString();
                        if (!inputText.isEmpty()) {
                            try {
                                String updatedInput = inputText;
                                // DatePicker ve TimePicker'dan alınan tarih/saat bilgisini bir Date objesine dönüştürün
                                int day = datePicker.getDayOfMonth();
                                int month = datePicker.getMonth() - 1;
                                int year = datePicker.getYear();
                                int hour = timePicker.getHour();
                                int minute = timePicker.getMinute();
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, month, day, hour, minute);
                                Date updatedDateTime = calendar.getTime();

                                medicineEntries.set(position, new MedicineTracker.MedicineEntry(updatedInput, updatedDateTime));

                            } catch (Exception e) {
                                Toast.makeText(MedicineTracker.this, "Lütfen değerleri doğru giriniz.", Toast.LENGTH_SHORT).show();
                            }
                            medicineListAdapter.notifyDataSetChanged();
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
                return true;
            }
        });
    }

    // Ekle butonuna tıklanınca çağrılır
    public void addMedicineEntry(View view) {

        EditText medicineNameEditText = findViewById(R.id.medicineName);
        String medicineNameString = medicineNameEditText.getText().toString().trim();
        EditText dateEditText = findViewById(R.id.dateEditText);
        String dateString = dateEditText.getText().toString().trim();

        // Girilen değerin boş olup olmadığını kontrol eder
        if (medicineNameString.isEmpty() && dateString.isEmpty()) {
            Toast.makeText(this, "Lütfen bir ilaç adı girin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Girilen değerin string bir değer olup olmadığını kontrol eder
        String medicineName;
        try {
            medicineName = medicineNameString;
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Lütfen geçerli bir sayı girin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Girilen değerin date bir değer olup olmadığını kontrol eder
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        Date date;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            Toast.makeText(this, "Lütfen geçerli bir tarih girin", Toast.LENGTH_SHORT).show();
            return;
        }
        // Yeni bir MedicineEntry oluşturur ve listeye ekler
        MedicineTracker.MedicineEntry medicineEntry = new MedicineTracker.MedicineEntry(medicineName, date);
        medicineEntries.add(medicineEntry);

        // Girilen değeri sıfırlar ve liste görünümünü günceller
        medicineNameEditText.setText("");
        medicineListAdapter.notifyDataSetChanged();
    }


    public class MedicineEntry {
        private String medicineName; //medicine name
        private Date dateTime;

        public MedicineEntry(String medicineName, Date dateTime) {
            this.medicineName = medicineName;
            this.dateTime = dateTime;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
        }

        public Date getDateTime() {
            return dateTime;
        }

        public void setDateTime(Date dateTime) {
            this.dateTime = dateTime;
        }


        @Override
        public String toString() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            String dateString = dateFormat.format(dateTime);
            return String.format(Locale.getDefault(), "%s \n%s", medicineName, dateString);
        }
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