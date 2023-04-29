package com.ae.DAO;

import com.ae.Models.UserInformation;

import java.util.List;

public interface InformationCallback<T> {
    void onInformationLoaded(List<T> informationList);
    void onInformationNotLoaded();
}
