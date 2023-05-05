package com.ae.DAO;

import androidx.annotation.NonNull;
import com.ae.Models.Tension;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TensionDAO implements IDAO<Tension>{
    private String tensionId;

    @Override
    public String create(Tension tension) {
        DatabaseReference dbReference = database.getReference("tension_data/"+uid).push();
        tensionId = dbReference.getKey();
        tension.setId(tensionId);
        dbReference.setValue(tension);
        return tensionId;
    }

    @Override
    public List<Tension> read(List<Tension> tensionList, InformationCallback informationCallback) {
        DatabaseReference databaseReference = database.getReference().child("tension_data").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        tensionId = dataSnapshot.getKey();
                        Tension tension = dataSnapshot.getValue(Tension.class);
                        tension.setId(tensionId);
                        tensionList.add(tension);
                    }
                    informationCallback.onInformationLoaded(tensionList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return tensionList;
    }


    @Override
    public void update(Tension tension) {
        DatabaseReference databaseReference = database.getReference().child("tension_data").child(uid).child(tension.getId());
        databaseReference.setValue(tension);
    }

    @Override
    public void delete(String id) {
        DatabaseReference databaseReference = database.getReference().child("tension_data").child(uid).child(id);
        databaseReference.removeValue();
    }
}
