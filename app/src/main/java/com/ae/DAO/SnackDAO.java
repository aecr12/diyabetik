package com.ae.DAO;

import androidx.annotation.NonNull;

import com.ae.Models.Breakfast;
import com.ae.Models.Snack;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class SnackDAO implements IDAO<Snack> {

    String foodId;

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String currentDateTime = dateFormat.format(calendar.getTime());
    @Override
    public String create(Snack snack) {
        DatabaseReference dbReference = database.getReference("meals/snack_data/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).push();
        foodId = dbReference.getKey();
        snack.setId(foodId);
        dbReference.setValue(snack);
        return foodId;
    }

    @Override
    public List<Snack> read(List<Snack> snackList, InformationCallback informationCallback) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("snack_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        foodId = dataSnapshot.getKey();
                        Snack snack = dataSnapshot.getValue(Snack.class);
                        snack.setId(foodId);
                        snackList.add(snack);
                    }
                    informationCallback.onInformationLoaded(snackList);
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
        DatabaseReference databaseReference = database.getReference().child("meals").child("snack_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snack.getId());
        databaseReference.setValue(snack);
    }

    @Override
    public void delete(String id) {
        DatabaseReference databaseReference = database.getReference().child("meals").child("snack_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(id);
        databaseReference.removeValue();
    }

    public List<Snack> readByCurrentDate(List<Snack> snackListFilteredByCurrentDate, InformationCallback informationCallback){
        DatabaseReference databaseReference = database.getReference().child("meals").child("snack_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        foodId = dataSnapshot.getKey();
                        Snack snack = dataSnapshot.getValue(Snack.class);
                        snack.setId(foodId);

                        if (snack.getDate().equals(currentDateTime)){

                            snackListFilteredByCurrentDate.add(snack);
                        }
                    }
                    informationCallback.onInformationLoaded(snackListFilteredByCurrentDate);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return snackListFilteredByCurrentDate;
    }
}
