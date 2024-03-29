package com.ae.DAO;

import androidx.annotation.NonNull;

import com.ae.Models.Breakfast;
import com.ae.Models.Dinner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DinnerDAO implements IDAO<Dinner> {

    String foodId;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String currentDateTime = dateFormat.format(calendar.getTime());
    @Override
    public String create(Dinner dinner) {
        DatabaseReference dbReference = database.getReference("meals/dinner_data/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).push();
        foodId = dbReference.getKey();
        dinner.setId(foodId);
        dbReference.setValue(dinner);
        return foodId;
    }

    @Override
    public List<Dinner> read(List<Dinner> dinnerList, InformationCallback informationCallback) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("dinner_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        foodId = dataSnapshot.getKey();
                        Dinner dinner = dataSnapshot.getValue(Dinner.class);
                        dinner.setId(foodId);
                        dinnerList.add(dinner);
                    }
                    informationCallback.onInformationLoaded(dinnerList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return dinnerList;
    }


    @Override
    public void update(Dinner dinner) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("dinner_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(dinner.getId());
        databaseReference.setValue(dinner);
    }

    @Override
    public void delete(String id) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("dinner_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(id);
        databaseReference.removeValue();
    }

    public List<Dinner> readByCurrentDate(List<Dinner> dinnerListFilteredByCurrentDate, InformationCallback informationCallback) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("dinner_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        foodId = dataSnapshot.getKey();
                        Dinner dinner = dataSnapshot.getValue(Dinner.class);
                        dinner.setId(foodId);
                        if (dinner.getDate().equals(currentDateTime)) {
                            dinnerListFilteredByCurrentDate.add(dinner);
                        }
                    }
                    informationCallback.onInformationLoaded(dinnerListFilteredByCurrentDate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return dinnerListFilteredByCurrentDate;
    }

}
