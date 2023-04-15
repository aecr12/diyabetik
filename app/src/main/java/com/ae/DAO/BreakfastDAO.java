package com.ae.DAO;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ae.Models.Breakfast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BreakfastDAO implements IDAO<Breakfast>{
    private String foodId;
    private ArrayList<Breakfast> breakfastArrayList;

    @Override
    public String create(Breakfast breakfast) {
        DatabaseReference dbReference = database.getReference("meals/breakfast_data/"+uid).push();
        foodId = dbReference.getKey();
        breakfast.setId(foodId);
        dbReference.setValue(breakfast);
        return foodId;
    }

    @Override
    public List<Breakfast> read(List<Breakfast> breakfastList, RecyclerView.Adapter adapter) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("breakfast_data").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    breakfastArrayList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        foodId = dataSnapshot.getKey();
                        Breakfast breakfast = dataSnapshot.getValue(Breakfast.class);
                        breakfast.setId(foodId);
                        breakfastArrayList.add(breakfast);
                    }
                    breakfastList.clear();
                    breakfastList.addAll(breakfastArrayList);
                    adapter.notifyDataSetChanged();
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
        DatabaseReference databaseReference = database.getReference().child("meals").child("breakfast_data").child(uid).child(breakfast.getId());
        databaseReference.setValue(breakfast);
    }

    @Override
    public void delete(String id) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("breakfast_data").child(uid).child(id);
        databaseReference.removeValue();
    }
}
