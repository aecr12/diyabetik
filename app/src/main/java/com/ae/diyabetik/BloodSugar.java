package com.ae.diyabetik;

import java.util.ArrayList;

public class BloodSugar {
    private int bloodSugarValue;
    private String date;

    public BloodSugar(int bloodSugarValue, String date) {
        this.bloodSugarValue = bloodSugarValue;
        this.date = date;
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

    public class BloodSugarList {
        private ArrayList<BloodSugar> bloodSugarList;

        public BloodSugarList() {
            this.bloodSugarList = new ArrayList<>();
        }

        public void addBloodSugar(BloodSugar bloodSugar) {
            bloodSugarList.add(bloodSugar);
        }

        public ArrayList<BloodSugar> getBloodSugarList() {
            return bloodSugarList;
        }
    }
}
