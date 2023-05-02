package com.ae.Models;

public class Water {
    private String id;
    private int glassesCount;

    public Water() {
    }

    public Water(String id, int glassesCount) {
        this.id = id;
        this.glassesCount = glassesCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGlassesCount() {
        return glassesCount;
    }

    public void setGlassesCount(int glassesCount) {
        this.glassesCount = glassesCount;
    }
}
