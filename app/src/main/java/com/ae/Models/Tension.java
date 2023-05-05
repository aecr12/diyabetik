package com.ae.Models;

public class Tension {
    private String id;
    private int systolic;
    private int diastolic;
    private String date;

    public Tension() {
    }

    public Tension(String id, int systolic, int diastolic, String date) {
        this.id = id;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
