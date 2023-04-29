package com.ae.DAO;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ae.Models.Breakfast;
import com.ae.Models.Lunch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LunchDAO implements IDAO<Lunch> {

    String foodId;

    @Override
    public String create(Lunch lunch) {
        DatabaseReference dbReference = database.getReference("meals/lunch_data/"+uid).push();
        foodId = dbReference.getKey();
        lunch.setId(foodId);
        dbReference.setValue(lunch);
        return foodId;
    }

    @Override
    public List<Lunch> read(List<Lunch> lunchList, InformationCallback informationCallback) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("lunch_data").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        foodId = dataSnapshot.getKey();
                        Lunch lunch = dataSnapshot.getValue(Lunch.class);
                        lunch.setId(foodId);
                        lunchList.add(lunch);
                    }
                    informationCallback.onInformationLoaded(lunchList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return lunchList;
    }


    @Override
    public void update(Lunch lunch) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("lunch_data").child(uid).child(lunch.getId());
        databaseReference.setValue(lunch);
    }

    @Override
    public void delete(String id) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("lunch_data").child(uid).child(id);
        databaseReference.removeValue();
    }
}
