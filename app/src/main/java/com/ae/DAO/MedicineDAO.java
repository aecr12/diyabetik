package com.ae.DAO;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ae.Models.BloodSugar;
import com.ae.Models.Medicine;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MedicineDAO implements IDAO<Medicine> {

    private String medicineId;
    private ArrayList<Medicine> medicineArrayList;

    @Override
    public String create(Medicine medicine) {
        DatabaseReference dbReference = database.getReference("medications/"+uid).push();
        medicineId = dbReference.getKey();
        medicine.setId(medicineId);
        dbReference.setValue(medicine);
        return medicineId;
    }

    @Override
    public List<Medicine> read(List<Medicine> medicineList, InformationCallback informationCallback) {
        DatabaseReference databaseReference = database.getReference().child("medications").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        medicineId = dataSnapshot.getKey();
                        Medicine medicine = dataSnapshot.getValue(Medicine.class);
                        medicine.setId(medicineId);
                        medicineList.add(medicine);
                    }
                    informationCallback.onInformationLoaded(medicineList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return medicineList;
    }

    @Override
    public void update(Medicine medicine) {
        DatabaseReference databaseReference = database.getReference().child("medications").child(uid).child(medicine.getId());
        databaseReference.setValue(medicine);
    }

    @Override
    public void delete(String id) {
        DatabaseReference databaseReference = database.getReference().child("medications").child(uid).child(id);
        databaseReference.removeValue();
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }
}
