package com.ae.DAO;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ae.Models.Snack;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SnackDAO implements IDAOforRecyclerViewClasses<Snack> {

    String foodId;
    ArrayList<Snack> snackArrayList;

    @Override
    public String create(Snack snack) {
        DatabaseReference dbReference = database.getReference("meals/snack_data/"+uid).push();
        foodId = dbReference.getKey();
        snack.setId(foodId);
        dbReference.setValue(snack);
        return foodId;
    }

    @Override
    public List<Snack> read(List<Snack> snackList, RecyclerView.Adapter adapter) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("snack_data").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    snackArrayList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        foodId = dataSnapshot.getKey();
                        Snack snack = dataSnapshot.getValue(Snack.class);
                        snack.setId(foodId);
                        snackArrayList.add(snack);
                    }
                    snackList.clear();
                    snackList.addAll(snackArrayList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return snackList;
    }

    @Override
    public void update(Snack snack) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("snack_data").child(uid).child(snack.getId());
        databaseReference.setValue(snack);
    }

    @Override
    public void delete(String id) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("snack_data").child(uid).child(id);
        databaseReference.removeValue();
    }
}
