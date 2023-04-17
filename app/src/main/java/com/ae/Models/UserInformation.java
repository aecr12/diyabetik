package com.ae.Models;

public class UserInformation {
    private String id;
    private String height;
    private String weight;
    private String waist;
    private String bloodSugarValue;
    private String hbA1cPercent;
    private String sistolik;
    private String diyastolik;

    public UserInformation() {
    }

    public UserInformation(String id, String height, String weight, String waist, String bloodSugarValue, String hbA1cPercent, String sistolik, String diyastolik) {
        this.id = id;
        this.height = height;
        this.weight = weight;
        this.waist = waist;
        this.bloodSugarValue = bloodSugarValue;
        this.hbA1cPercent = hbA1cPercent;
        this.sistolik = sistolik;
        this.diyastolik = diyastolik;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public String getBloodSugarValue() {
        return bloodSugarValue;
    }

    public void setBloodSugarValue(String bloodSugarValue) {
        this.bloodSugarValue = bloodSugarValue;
    }

    public String getHbA1cPercent() {
        return hbA1cPercent;
    }

    public void setHbA1cPercent(String hbA1cPercent) {
        this.hbA1cPercent = hbA1cPercent;
    }

    public String getSistolik() {
        return sistolik;
    }

    public void setSistolik(String sistolik) {
        this.sistolik = sistolik;
    }

    public String getDiyastolik() {
        return diyastolik;
    }

    public void setDiyastolik(String diyastolik) {
        this.diyastolik = diyastolik;
    }
}
