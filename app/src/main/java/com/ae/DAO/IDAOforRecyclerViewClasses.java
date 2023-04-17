package com.ae.DAO;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public interface IDAOforRecyclerViewClasses<T>{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    String create(T t);
    List<T> read(List<T> t, RecyclerView.Adapter adapter);
    void update(T t);
    void delete(String id);
}
