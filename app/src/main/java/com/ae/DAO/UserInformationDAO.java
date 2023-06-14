package com.ae.DAO;

import androidx.annotation.NonNull;

import com.ae.Models.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserInformationDAO implements IDAO<UserInformation> {

    String userInformationId;

    @Override
    public String create(UserInformation userInformation) {
        DatabaseReference dbReference = database.getReference("user_informations/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    return;
                } else {
                    DatabaseReference newDbReference = database.getReference("user_informations/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).push();
                    userInformationId = newDbReference.getKey();
                    userInformation.setId(userInformationId);
                    newDbReference.setValue(userInformation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return userInformation.getId();
    }

    @Override
    public List<UserInformation> read(List<UserInformation> userInformationList, InformationCallback informationCallback) {
        DatabaseReference databaseReference = database.getReference().child("user_informations").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if (databaseReference!=null){
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            userInformationId = dataSnapshot.getKey();
                            UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                            userInformation.setId(userInformationId);
                            userInformationList.add(userInformation);
                        }
                        informationCallback.onInformationLoaded(userInformationList);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return userInformationList;
    }


    @Override
    public void update(UserInformation userInformation) {
        DatabaseReference databaseReference = database.getReference().child("user_informations").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(userInformation.getId());
        databaseReference.setValue(userInformation);
    }

    @Override
    public void delete(String id) {

    }
}
