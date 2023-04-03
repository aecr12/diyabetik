package com.ae.diyabetik;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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

import com.google.android.material.textview.MaterialTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BloodSugar extends AppCompatActivity {

    private ArrayList<BloodSugarEntry> bloodSugarEntries;
    private ArrayAdapter<BloodSugarEntry> bloodSugarListAdapter;
    private ListView bloodSugarList;
    private Button addButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blood_sugar_tracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bloodSugarEntries = new ArrayList<>();
        bloodSugarListAdapter = new ArrayAdapter<>(this, R.layout.list_item1, bloodSugarEntries);
        bloodSugarList = findViewById(R.id.bloodSugarList);
        bloodSugarList.setAdapter(bloodSugarListAdapter);

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBloodSugarEntry(v);
            }
        });

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String formattedTime = formatter.format(currentTime);
        EditText dateEditText = findViewById(R.id.dateEditText);
        dateEditText.setText(formattedTime);

        bloodSugarList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                float selectedValue = bloodSugarEntries.get(position).getBloodSugarValue();
                Intent intent = new Intent(BloodSugar.this,BloodSugarWithHBA1CDiagram.class);
                intent.putExtra("selectedValue",selectedValue);
                System.out.println("BloodSugar sınıfı selected value" + selectedValue);
                startActivity(intent);
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
                builder.setTitle("Değeri Güncelle");
                // EditText'leri ve tarih/saat seçicilerini içerecek bir View nesnesi oluşturun
                ScrollView scrollView = new ScrollView(BloodSugar.this);
                LinearLayout layout = new LinearLayout(getApplicationContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText input = new EditText(getApplicationContext());
                input.setHint("Kan şekeri değerini girin");

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
                        if(!inputText.isEmpty()){
                            try{
                                float updatedInput = Float.parseFloat(inputText);

                                // DatePicker ve TimePicker'dan alınan tarih/saat bilgisini bir Date objesine dönüştürün
                                int day = datePicker.getDayOfMonth();
                                int month = datePicker.getMonth()-1;
                                int year = datePicker.getYear();
                                int hour = timePicker.getHour();
                                int minute = timePicker.getMinute();
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, month, day, hour, minute);
                                Date updatedDateTime = calendar.getTime();

                                bloodSugarEntries.set(position, new BloodSugarEntry(updatedInput, updatedDateTime));

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
                return true;
            }
        });
    }

    // Ekle butonuna tıklanınca çağrılır
    public void addBloodSugarEntry(View view) {

        EditText bloodSugarValueEditText = findViewById(R.id.bloodSugarValueEditText);
        String bloodSugarValueString = bloodSugarValueEditText.getText().toString().trim();
        EditText dateEditText = findViewById(R.id.dateEditText);
        String dateString = dateEditText.getText().toString().trim();

        // Girilen değerin boş olup olmadığını kontrol eder
        if (bloodSugarValueString.isEmpty() && dateString.isEmpty()) {
            Toast.makeText(this, "Lütfen bir kan şekeri değeri girin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Girilen değerin sayısal bir değer olup olmadığını kontrol eder
        float bloodSugarValue;
        try {
            bloodSugarValue = Float.parseFloat(bloodSugarValueString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Lütfen geçerli bir sayı girin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Girilen değerin date bir değer olup olmadığını kontrol eder
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        Date date;
        try{
            date=dateFormat.parse(dateString);
            System.out.println(date.getClass());
        }catch (ParseException e){

            Toast.makeText(this, "Lütfen geçerli bir tarih girin", Toast.LENGTH_SHORT).show();
            return;
        }


        // Yeni bir BloodSugarEntry oluşturur ve listeye ekler
        BloodSugarEntry bloodSugarEntry = new BloodSugarEntry(bloodSugarValue, date);
        bloodSugarEntries.add(bloodSugarEntry);

        // Girilen değeri sıfırlar ve liste görünümünü günceller
        bloodSugarValueEditText.setText("");
        bloodSugarListAdapter.notifyDataSetChanged();
    }


    public class BloodSugarEntry {
        private float bloodSugarValue;
        private Date dateTime;

        public BloodSugarEntry(Float bloodSugarValue, Date dateTime) {
            this.bloodSugarValue = bloodSugarValue;
            this.dateTime = dateTime;
        }

        public float getBloodSugarValue() {
            return bloodSugarValue;
        }

        public void setBloodSugarValue(float bloodSugarValue) {
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
