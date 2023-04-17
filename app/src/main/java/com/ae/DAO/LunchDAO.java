package com.ae.DAO;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ae.Models.Lunch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LunchDAO implements IDAOforRecyclerViewClasses<Lunch> {

    String foodId;
    ArrayList<Lunch> lunchArrayList;

    @Override
    public String create(Lunch lunch) {
        DatabaseReference dbReference = database.getReference("meals/lunch_data/"+uid).push();
        foodId = dbReference.getKey();
        lunch.setId(foodId);
        dbReference.setValue(lunch);
        return foodId;
    }

    @Override
    public List<Lunch> read(List<Lunch> lunchList, RecyclerView.Adapter adapter) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("lunch_data").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    lunchArrayList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        foodId = dataSnapshot.getKey();
                        Lunch lunch = dataSnapshot.getValue(Lunch.class);
                        lunch.setId(foodId);
                        lunchArrayList.add(lunch);
                    }
                    lunchList.clear();
                    lunchList.addAll(lunchArrayList);
                    adapter.notifyDataSetChanged();
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
