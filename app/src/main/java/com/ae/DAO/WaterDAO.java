package com.ae.DAO;

import androidx.annotation.NonNull;

import com.ae.Models.Water;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class WaterDAO implements IDAO<Water>{

    String id;

    @Override
    public String create(Water water) {
        DatabaseReference dbReference = database.getReference("water_count_data/" + uid);
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    return;
                } else {
                    DatabaseReference newDbReference = database.getReference("water_count_data/" + uid).push();
                    id = newDbReference.getKey();
                    water.setId(id);
                    newDbReference.setValue(water);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return water.getId();
    }

    @Override
    public List<Water> read(List<Water> waterCountList, InformationCallback informationCallback) {
        DatabaseReference databaseReference = database.getReference().child("water_count_data").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        id = dataSnapshot.getKey();
                        Water water = dataSnapshot.getValue(Water.class);
                        water.setId(id);
                        waterCountList.add(water);
                    }
                    informationCallback.onInformationLoaded(waterCountList);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return waterCountList;

    }

    @Override
    public void update(Water water) {
        DatabaseReference databaseReference = database.getReference().child("water_count_data").child(uid).child(water.getId());
        databaseReference.setValue(water);
    }

    @Override
    public void delete(String id) {

    }
}
