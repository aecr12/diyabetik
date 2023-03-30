package com.ae.diyabetik;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class MedicineTracker extends AppCompatActivity {

    private LinearLayout linearLayoutMedicineList;
    private EditText editTextMedicineName;
    private EditText editTextTakenTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_tracker_main);

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        linearLayoutMedicineList = findViewById(R.id.linearLayoutMedicineList);
        editTextMedicineName = findViewById(R.id.editTextMedicineName);
        editTextTakenTime = findViewById(R.id.editTextTakenTime);

        sharedPreferences = getSharedPreferences("ilacSharedPreferences", MODE_PRIVATE);
        Map<String, ?> medicineInformations = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : medicineInformations.entrySet()) {
            String ilacAdi = entry.getKey();
            String kullanimZamani = entry.getValue().toString();
            IlacKarti ilacKarti = new IlacKarti(this, ilacAdi, kullanimZamani,sharedPreferences);
            linearLayoutMedicineList.addView(ilacKarti);
        }

        Button buttonAdd = findViewById(R.id.buttonAdd);
        SharedPreferences finalSharedPreferences = sharedPreferences;
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String medicineName = editTextMedicineName.getText().toString();
                String takenTime = editTextTakenTime.getText().toString();

                if (!medicineName.isEmpty() && !takenTime.isEmpty()) {
                    IlacKarti ilacKarti = new IlacKarti(MedicineTracker.this, medicineName, takenTime, finalSharedPreferences);
                    linearLayoutMedicineList.addView(ilacKarti);
                    editTextMedicineName.setText("");
                    editTextTakenTime.setText("");

                }
            }
        });

    }

    private class IlacKarti extends LinearLayout {

        private TextView tvIlacAdi;
        private TextView tvKullanımZamani;
        private Button btnSil;

        private void removeSharedPreferencesData(SharedPreferences sharedPreferences) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(tvIlacAdi.getText().toString());
            editor.apply();
        }

        public void kaydet() {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("ilacSharedPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(tvIlacAdi.getText().toString(), tvKullanımZamani.getText().toString());
            editor.apply();
        }

        public void sil() {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("ilacSharedPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(tvIlacAdi.getText().toString());
            editor.apply();
            ((ViewGroup) getParent()).removeView(this);
        }

        public IlacKarti(final AppCompatActivity activity, String ilacAdi, String kullanımZamani, SharedPreferences sharedPreferences) {
            super(activity);

            tvIlacAdi = new TextView(activity);
            tvKullanımZamani = new TextView(activity);
            btnSil = new Button(activity);

            tvIlacAdi.setText(ilacAdi);
            tvKullanımZamani.setText(kullanımZamani);
            btnSil.setText("Sil");

            tvIlacAdi.setTextColor(Color.WHITE);
            tvKullanımZamani.setTextColor(Color.WHITE);
            setBackgroundColor(Color.parseColor("#00BFA5"));
            setGravity(Gravity.CENTER);


            int padding = dpToPx(16);
            setPadding(padding, padding, padding, padding);

            LayoutParams layoutParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8));
            setLayoutParams(layoutParams);



            btnSil.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((IlacKarti) v.getParent()).sil();
                }
            });



            LayoutParams textViewParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            textViewParams.weight = 1;
            textViewParams.gravity = Gravity.CENTER_VERTICAL;
            tvIlacAdi.setLayoutParams(textViewParams);
            tvKullanımZamani.setLayoutParams(textViewParams);

            LayoutParams buttonParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            buttonParams.gravity = Gravity.CENTER_VERTICAL;
            btnSil.setLayoutParams(buttonParams);

            addView(tvIlacAdi);
            addView(tvKullanımZamani);
            addView(btnSil);
            kaydet();
        }

        private int dpToPx(int dp) {
            float density = getContext().getResources().getDisplayMetrics().density;
            return Math.round(dp * density);
        }
    }

}