package com.ae.diyabetik;

import java.util.ArrayList;

public class Medicine {

    private String medicineName;
    private String takenDate;

    public Medicine(String medicineName, String takenDate) {
        this.medicineName = medicineName;
        this.takenDate = takenDate;
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

    public class MedicineList {
        private ArrayList<Medicine> medicineArrayList;

        public MedicineList(){
            this.medicineArrayList = new ArrayList<>();
        }
        public ArrayList<Medicine> getMedicineArrayList(){
            return medicineArrayList;
        }
        public void addMedicine(Medicine medicine){
            medicineArrayList.add(medicine);
        }
    }
}
