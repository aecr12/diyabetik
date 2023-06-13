package com.ae.DAO;

import androidx.annotation.NonNull;
import com.ae.Models.StepCounter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class StepCounterDAO implements IDAO<StepCounter> {

    private String stepCounterId;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    String currentDate = dateFormat.format(new Date());

    @Override
    public String create(StepCounter stepCounter) {
        DatabaseReference databaseReference = database.getReference().child("step_counter_data").child(uid).child(currentDate);
        Query query = databaseReference.orderByChild("stepCount").equalTo(stepCounter.getStepCount());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                } else {
                    DatabaseReference newDbReference = databaseReference.push();
                    stepCounterId = newDbReference.getKey();
                    stepCounter.setId(stepCounterId);
                    newDbReference.setValue(stepCounter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return stepCounterId;
    }


    @Override
    public List<StepCounter> read(List<StepCounter> stepCounterList, InformationCallback informationCallback) {
        DatabaseReference databaseReference = database.getReference().child("step_counter_data").child(uid).child(currentDate);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        stepCounterId = dataSnapshot.getKey();
                        StepCounter stepCounter = dataSnapshot.getValue(StepCounter.class);
                        stepCounter.setId(stepCounterId);
                        stepCounterList.add(stepCounter);
                    }
                    informationCallback.onInformationLoaded(stepCounterList);
                }else {
                    informationCallback.onInformationNotLoaded();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return stepCounterList;
    }

    @Override
    public void update(StepCounter stepCounter) {
        DatabaseReference databaseReference = database.getReference().child("step_counter_data").child(uid).child(currentDate).child(stepCounter.getId());
        databaseReference.setValue(stepCounter);
    }


    @Override
    public void delete(String id) {

    }
    public List<StepCounter> getByDate(List<StepCounter> stepCounterList, String dateString, InformationCallback informationCallback){
        DatabaseReference databaseReference = database.getReference().child("step_counter_data").child(uid).child(dateString);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        stepCounterId = dataSnapshot.getKey();
                        StepCounter stepCounter = dataSnapshot.getValue(StepCounter.class);
                        stepCounter.setId(stepCounterId);
                        stepCounterList.add(stepCounter);
                    }
                    informationCallback.onInformationLoaded(stepCounterList);
                }else {
                    informationCallback.onInformationNotLoaded();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return stepCounterList;
    }
}
