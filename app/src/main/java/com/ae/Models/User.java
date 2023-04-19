package com.ae.Models;

public class User {

    private String adSoyad;
    private String mail;
    private String password;
    private String id;

    public User() {
    }

    public User(String id, String adSoyad, String mail, String password) {
        this.id = id;
        this.adSoyad = adSoyad;
        this.mail = mail;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
