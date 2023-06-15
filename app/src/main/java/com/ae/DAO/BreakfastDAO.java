package com.ae.DAO;

import androidx.annotation.NonNull;

import com.ae.Models.Breakfast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class BreakfastDAO implements IDAO<Breakfast> {

    private String foodId;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String currentDateTime = dateFormat.format(calendar.getTime());

    @Override
    public String create(Breakfast breakfast) {
        DatabaseReference dbReference = database.getReference("meals/breakfast_data/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).push();
        foodId = dbReference.getKey();
        breakfast.setId(foodId);
        dbReference.setValue(breakfast);
        return foodId;
    }

    @Override
    public List<Breakfast> read(List<Breakfast> breakfastList, InformationCallback informationCallback) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("breakfast_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        foodId = dataSnapshot.getKey();
                        Breakfast breakfast = dataSnapshot.getValue(Breakfast.class);
                        breakfast.setId(foodId);
                        breakfastList.add(breakfast);
                    }
                    informationCallback.onInformationLoaded(breakfastList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return breakfastList;
    }


    @Override
    public void update(Breakfast breakfast) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("breakfast_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(breakfast.getId());
        databaseReference.setValue(breakfast);
    }

    @Override
    public void delete(String id) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("breakfast_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(id);
        databaseReference.removeValue();
    }

    public List<Breakfast> readByCurrentDate(List<Breakfast> breakfastListFilteredByCurrentDate, InformationCallback informationCallback){
        DatabaseReference databaseReference = database.getReference().child("meals").child("breakfast_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        foodId = dataSnapshot.getKey();
                        Breakfast breakfast = dataSnapshot.getValue(Breakfast.class);
                        breakfast.setId(foodId);

                        if (breakfast.getDate().equals(currentDateTime)){

                            breakfastListFilteredByCurrentDate.add(breakfast);
                        }
                    }
                    informationCallback.onInformationLoaded(breakfastListFilteredByCurrentDate);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return breakfastListFilteredByCurrentDate;
    }
}