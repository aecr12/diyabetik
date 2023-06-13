package com.ae.diyabetik;

public class BloodSugarCalculation {

    private float bloodSugar;
    private float hba1c;

    private final float constHba1c = (float) 6.5;

    public BloodSugarCalculation(float bloodSugar) {
        this.bloodSugar = bloodSugar;
    }
    public float calculateHba1c(float bloodSugar){
        hba1c = (float) ((bloodSugar + 46.7) / 28.7);
        return hba1c;
    }
    public float getConstHba1c() {
        return constHba1c;
    }

}
