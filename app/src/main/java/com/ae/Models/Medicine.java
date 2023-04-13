package com.ae.Models;

public class Medicine {

    private String id;
    private String medicineName;
    private String takenDate;

    public Medicine() {
    }

    public Medicine(String id, String medicineName, String takenDate) {
        this.id = id;
        this.medicineName = medicineName;
        this.takenDate = takenDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getTakenDate() {
        return takenDate;
    }

    public void setTakenDate(String takenDate) {
        this.takenDate = takenDate;
    }
}
