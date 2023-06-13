package com.ae.DAO;

import androidx.annotation.NonNull;

import com.ae.Models.TreatmentChoice;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TreatmentChoiceDAO implements IDAO<TreatmentChoice>{
    String treatmentChoiceId;


    @Override
    public String create(TreatmentChoice treatmentChoice) {
        DatabaseReference dbReference = database.getReference("treatment_choices/" + uid);
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    return;
                } else {
                    DatabaseReference newDbReference = database.getReference("treatment_choices/" + uid).push();
                    treatmentChoiceId = newDbReference.getKey();
                    treatmentChoice.setId(treatmentChoiceId);
                    newDbReference.setValue(treatmentChoice);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return treatmentChoiceId;
    }

    @Override
    public List<TreatmentChoice> read(List<TreatmentChoice> treatmentChoiceList, InformationCallback informationCallback) {
        DatabaseReference databaseReference = database.getReference().child("treatment_choices").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        treatmentChoiceId = dataSnapshot.getKey();
                        TreatmentChoice treatmentChoice = dataSnapshot.getValue(TreatmentChoice.class);
                        treatmentChoice.setId(treatmentChoiceId);
                        treatmentChoiceList.add(treatmentChoice);
                    }
                    informationCallback.onInformationLoaded(treatmentChoiceList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return treatmentChoiceList;
    }


    @Override
    public void update(TreatmentChoice treatmentChoice) {
        DatabaseReference databaseReference = database.getReference().child("treatment_choices").child(uid).child(treatmentChoice.getId());
        databaseReference.setValue(treatmentChoice);
    }

    @Override
    public void delete(String id) {

    }
}
