package com.ae.Models;

public class UserInformation {
    private String id;
    private String height;
    private String weight;
    private String waist;
    private String hbA1cPercent;

    public UserInformation() {
    }

    public UserInformation(String id, String height, String weight, String waist, String hbA1cPercent) {
        this.id = id;
        this.height = height;
        this.weight = weight;
        this.waist = waist;
        this.hbA1cPercent = hbA1cPercent;
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

    public String getHbA1cPercent() {
        return hbA1cPercent;
    }

    public void setHbA1cPercent(String hbA1cPercent) {
        this.hbA1cPercent = hbA1cPercent;
    }

}
