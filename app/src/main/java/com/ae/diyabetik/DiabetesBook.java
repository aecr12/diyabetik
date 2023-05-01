package com.ae.diyabetik;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Locale;


public class DiabetesBook extends AppCompatActivity {

    private PDFView pdfView;
    private File file;
    private TextToSpeech textToSpeech;
    private ImageView imageViewMegaphone;
    private String text = "";
    private boolean isTextToSpeechRunning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diabetes_book);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageViewMegaphone = findViewById(R.id.imageViewMegaphone);
        pdfView = findViewById(R.id.pdfView);
        displayFromAsset("diyabet.pdf");
        PDDocument pd;
        BufferedWriter wr;
        PDFBoxResourceLoader.init(this);
        try {
            pd = PDDocument.load(getAssets().open("diyabet.pdf"));
            file = new File(getExternalFilesDir(null), "dText.txt");
            wr = new BufferedWriter(new FileWriter(file));
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(3); //Start extracting from page 3
            stripper.setEndPage(5); //Extract till page 5
            stripper.writeText(pd, wr);
            if (pd != null) {
                pd.close();
            }
            // I use close() to flush the stream.
            wr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                text += line + "\n";
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageViewMegaphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTextToSpeechRunning) {
                    textToSpeech.shutdown();
                    isTextToSpeechRunning = false;
                } else {
                    textToSpeech(text);
                    isTextToSpeechRunning = true;
                }
            }
        });

    }

    private void textToSpeech(String text) {
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.getDefault());
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                } else {
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            } else {

            }
        });
    }

    private void displayFromAsset(String assetFileName) {
        pdfView.fromAsset(assetFileName)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textToSpeech.shutdown();
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
