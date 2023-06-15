package com.ae.DAO;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public interface IDAO<T> {

    // Ortak veritabanı işlemeri için interface
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    String create(T t);

    List<T> read(List<T> t, InformationCallback informationCallback);

    void update(T t);

    void delete(String id);

}
