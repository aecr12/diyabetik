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

public class BloodSugarDAO implements IDAO<BloodSugar>{

    private String bloodSugarId;
    private ArrayList<BloodSugar> bloodSugarArrayList;

    @Override
    public String create(BloodSugar bloodSugar) {
        DatabaseReference dbReference = database.getReference("blood_sugar_data/"+uid).push();
        bloodSugarId = dbReference.getKey();
        bloodSugar.setId(bloodSugarId);
        dbReference.setValue(bloodSugar);
        return bloodSugarId;
    }

    @Override
    public List<BloodSugar> read(List<BloodSugar> bloodSugarList, RecyclerView.Adapter adapter) {
        DatabaseReference databaseReference = database.getReference().child("blood_sugar_data").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    bloodSugarArrayList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        bloodSugarId = dataSnapshot.getKey();
                        BloodSugar bloodSugar = dataSnapshot.getValue(BloodSugar.class);
                        bloodSugar.setId(bloodSugarId);
                        bloodSugarArrayList.add(bloodSugar);
                    }
                    bloodSugarList.clear();
                    bloodSugarList.addAll(bloodSugarArrayList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return bloodSugarList;
    }

    @Override
    public void update(BloodSugar bloodSugar) {
        DatabaseReference databaseReference = database.getReference().child("blood_sugar_data").child(uid).child(bloodSugar.getId());
        databaseReference.setValue(bloodSugar);
    }

    @Override
    public void delete(String id) {
        DatabaseReference databaseReference = database.getReference().child("blood_sugar_data").child(uid).child(id);
        databaseReference.removeValue();
    }
}
