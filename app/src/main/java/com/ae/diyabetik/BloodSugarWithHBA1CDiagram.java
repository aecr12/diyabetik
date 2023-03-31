package com.ae.diyabetik;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class BloodSugarWithHBA1CDiagram extends AppCompatActivity {
    private Double selectedValue;
    private Double bloodSugar;
    private GraphView graphView;
    private LineGraphSeries<DataPoint> series;
    private BloodSugarCalculation bloodSugarCalculation;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bloodsugar_with_hba1c_diagram);

        graphView = findViewById(R.id.graph);
        selectedValue = getIntent().getDoubleExtra("selectedValue", 0.0);
        bloodSugar = selectedValue;
        bloodSugarCalculation = new BloodSugarCalculation(bloodSugar);

        series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, bloodSugarCalculation.getBloodSugar()),
                new DataPoint(1, bloodSugarCalculation.getHba1c()),
                new DataPoint(2,bloodSugarCalculation.calculateTargetBloodSugar())
        });
        System.out.println(bloodSugarCalculation.getHba1c());
        series.setColor(Color.BLUE);
        series.setThickness(5);
        graphView.addSeries(series);

        graphView.setTitleColor(R.color.colorBlack);
        graphView.setTitleTextSize(10);
        graphView.setBackgroundColor(getResources().getColor(R.color.white));
        graphView.getViewport().setBorderColor(R.color.colorBlack);
        graphView.getViewport().setMaxX(10);
        graphView.getViewport().setMaxY(15);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMinY(0);
        graphView.getGridLabelRenderer().setHorizontalAxisTitle("Kan Şekeri (mg/dL)");
        graphView.getGridLabelRenderer().setVerticalAxisTitle("HbA1c (%)");
    }
}
