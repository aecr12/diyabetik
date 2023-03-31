package com.ae.diyabetik;

public class BloodSugarCalculation {

    private double bloodSugar;
    private double hba1c;
    private double targetBloodSugar;

    public BloodSugarCalculation(double bloodSugar) {
        this.bloodSugar = bloodSugar;
        this.hba1c = (bloodSugar + 46.7) / 28.7;
    }

    public Double calculateTargetBloodSugar() {
        targetBloodSugar = hba1c*10.929+36.421;
        return targetBloodSugar;
    }

    public double getBloodSugar() {
        return bloodSugar;
    }

    public double getHba1c() {
        return hba1c;
    }

    public double getIdealBloodSugar(double hba1cGoal) {
        return (hba1cGoal * 28.7) - 46.7;
    }

}
