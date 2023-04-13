package com.ae.Models;

public class BloodSugar {
    private int id;
    private int userId;
    private int bloodSugarValue;
    private String date;

    public BloodSugar(int id, int userId, int bloodSugarValue, String date) {
        this.id = id;
        this.userId = userId;
        this.bloodSugarValue = bloodSugarValue;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
