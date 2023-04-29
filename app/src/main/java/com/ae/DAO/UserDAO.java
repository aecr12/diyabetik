package com.ae.DAO;

import androidx.annotation.NonNull;

import com.ae.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserDAO implements IDAO<User> {
    private String userId;

    @Override
    public String create(User user) {
        DatabaseReference dbReference = database.getReference("users/" + uid);
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    return;
                } else {
                    DatabaseReference newDbReference = database.getReference("users/" + uid);
                    userId = newDbReference.getKey();
                    user.setId(userId);
                    newDbReference.setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return userId;
    }

    @Override
    public List<User> read(List<User> t, InformationCallback informationCallback) {
        return null;
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(String id) {

    }
}
