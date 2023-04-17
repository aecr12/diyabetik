package com.ae.DAO;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ae.Models.Dinner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DinnerDAO implements IDAOforRecyclerViewClasses<Dinner> {

    String foodId;
    ArrayList<Dinner> dinnerArrayList;

    @Override
    public String create(Dinner dinner) {
        DatabaseReference dbReference = database.getReference("meals/dinner_data/"+uid).push();
        foodId = dbReference.getKey();
        dinner.setId(foodId);
        dbReference.setValue(dinner);
        return foodId;
    }

    @Override
    public List<Dinner> read(List<Dinner> dinnerList, RecyclerView.Adapter adapter) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("dinner_data").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dinnerArrayList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        foodId = dataSnapshot.getKey();
                        Dinner dinner = dataSnapshot.getValue(Dinner.class);
                        dinner.setId(foodId);
                        dinnerArrayList.add(dinner);
                    }
                    dinnerList.clear();
                    dinnerList.addAll(dinnerArrayList);
                    adapter.notifyDataSetChanged();
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
        DatabaseReference databaseReference = database.getReference().child("meals").child("dinner_data").child(uid).child(dinner.getId());
        databaseReference.setValue(dinner);
    }

    @Override
    public void delete(String id) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("dinner_data").child(uid).child(id);
        databaseReference.removeValue();
    }

}
