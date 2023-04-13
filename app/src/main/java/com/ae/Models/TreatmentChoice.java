package com.ae.Models;

public class TreatmentChoice {
    private int id;
    private int user_id;
    private String treatmentChoice;

    public TreatmentChoice(int id, int user_id, String treatmentChoice) {
        this.id = id;
        this.user_id = user_id;
        this.treatmentChoice = treatmentChoice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTreatmentChoice() {
        return treatmentChoice;
    }

    public void setTreatmentChoice(String treatmentChoice) {
        this.treatmentChoice = treatmentChoice;
    }
}
