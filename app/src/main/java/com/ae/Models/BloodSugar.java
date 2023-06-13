package com.ae.Models;


public class BloodSugar {
    private String id;
    private int bloodSugarValue;
    private String date;

    public BloodSugar() {
    }

    public BloodSugar(String id, int bloodSugarValue, String date) {
        this.id = id;
        this.bloodSugarValue = bloodSugarValue;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBloodSugarValue() {
        return bloodSugarValue;
    }

    public void setBloodSugarValue(int bloodSugarValue) {
        this.bloodSugarValue = bloodSugarValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
