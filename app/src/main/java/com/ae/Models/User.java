package com.ae.Models;

public class User {

    private String adSoyad;
    private String mail;
    private String id;

    public User() {
    }

    public User(String id, String adSoyad, String mail) {
        this.id = id;
        this.adSoyad = adSoyad;
        this.mail = mail;
    }


    public String getAdSoyad() {
        return adSoyad;
    }

    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
