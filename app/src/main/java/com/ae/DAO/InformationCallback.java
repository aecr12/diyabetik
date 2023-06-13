package com.ae.DAO;

import java.util.List;

public interface InformationCallback<T> {
    // veritabınında read fonksiyonu çalıştığında, eğer bilgiler listeye
    // başarılı bir şekilde yüklenirse onInformationLoaded callback çalışacak
    // böylece veri henüz veritabanından yüklenmeden listede gösterilmeye çalışıldığı durumların önüne geçilecek
    void onInformationLoaded(List<T> informationList);
    void onInformationNotLoaded();
}
