package com.ae.diyabetik;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class BloodSugarWithHBA1CDiagram extends AppCompatActivity {


    private BloodSugarCalculation bloodSugarCalculation;
    // grafik işlemi için barchart
    private BarChart barChart;
    // intentten gelecek değer
    private float selectedValue;
    // kullanıcıya geri bildirim verilecek textview
    private TextView textViewFeedback;
    // kullanıcının raporu indirebilmesi için buton
    private ImageButton imageButtonPDF;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bloodsugar_with_hba1c_diagram);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textViewFeedback = findViewById(R.id.textViewFeedback);
        imageButtonPDF=findViewById(R.id.imageButtonPDF);
        ArrayList<BarEntry> barArrayList = new ArrayList();
        // chartın oluşturulması
        barChart = findViewById(R.id.chart);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setGranularity(1f); // Değerlerin arasındaki mesafe
        leftAxis.setAxisMinimum(0f); // Minimum değer
        leftAxis.setAxisMaximum(20f); // Maksimum değer
        leftAxis.setDrawGridLines(true);
        leftAxis.setLabelCount(21);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawLabels(false);
        // x eksenindeki eksen değerlerinin silinmesi
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);

        // intentten gelen kan şekeri değeri alınıyor
        selectedValue = (float) getIntent().getFloatExtra("selectedValue", 0);
        float bloodSugar;
        bloodSugar = (float) selectedValue;

        bloodSugarCalculation = new BloodSugarCalculation(bloodSugar);
        float constHba1c = bloodSugarCalculation.getConstHba1c();
        float hba1c = bloodSugarCalculation.calculateHba1c(bloodSugar);


        barArrayList.add(new BarEntry(0,constHba1c));
        barArrayList.add(new BarEntry(1,hba1c));
        BarDataSet barDataSet = new BarDataSet(barArrayList,"Değerler");

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth((float) 0.5);

        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet.setValueTextColors(Collections.singletonList(Color.BLACK));
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(true);

        if(hba1c>constHba1c){
            textViewFeedback.setText("HbA1c değeriniz ideal değerden %"+calculateDevationFromThreshold(constHba1c,hba1c)+" fazla.");
        }else{
            textViewFeedback.setText("HbA1c değeriniz olması gereken aralıkta.");
        }

        imageButtonPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPDF();
            }
        });
    }

    public float calculateDevationFromThreshold(float constHba1c, float hba1c){
        float surplus = hba1c-constHba1c;
        return ((surplus/hba1c)*100);
    }

    public void createPDF() {
        // PDF'nin oluşturulması
        PdfDocument document = new PdfDocument();

        // PDF belgesine sayfanın eklenmesi
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(
                barChart.getWidth(),
                barChart.getHeight(),
                1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        barChart.draw(page.getCanvas());
        document.finishPage(page);

        // PDF belgesinin kaydedilmesi
        File file = new File(getExternalFilesDir(null), "blood_sugar_chart.pdf");
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            document.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(this, "PDF dosyası başarıyla kaydedildi", Toast.LENGTH_SHORT).show();

            // PDF dosyasının indirilmesi
            Uri fileUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, "PDF Dosyasını Aç"));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "PDF dosyası kaydedilemedi", Toast.LENGTH_SHORT).show();
        }

        document.close();
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

