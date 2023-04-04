package com.ae.diyabetik;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class DiabetesManual extends AppCompatActivity {
    TextView textViewTitle;
    TextView textViewBookText;
    ImageButton buttonRead;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diabetes_manual);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewTitle=findViewById(R.id.textViewTitle);
        textViewBookText = findViewById(R.id.textViewBookText);
        buttonRead = findViewById(R.id.buttonRead);

        textViewBookText.setText("Beslenme; büyüme, gelişme, yaşamın\n" +
                "sürdürülmesi ve sağlığın korunması için besin\n" +
                "öğelerinin yeterli ve dengeli olarak alınmasıdır.\n" +
                "Beslenmek, hiçbir zaman karın doyurmak anlamına\n" +
                "gelmemelidir. Sağlıklı beslenme, besinleri yeterli ve\n" +
                "dengeli tüketmek ile mümkündür.\n");

        buttonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech(textViewBookText.getText().toString());
            }
        });

    }
    private void textToSpeech(String text) {
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.getDefault());
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(TAG, "This language is not supported");
                } else {
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            } else {
                Log.e(TAG, "Initialization failed");
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
}
