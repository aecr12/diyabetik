package com.ae.diyabetik;

import java.util.ArrayList;

public class Food {
    private String name;
    private String portion;
    private ArrayList<Food> foodArrayList;
    public Food(String name, String portion) {
        this.name = name;
        this.portion = portion;
        foodArrayList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getPortion() {
        return portion;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPortion(String portion) {
        this.portion = portion;
    }

    public ArrayList<Food> getFoodArrayList() {
        return foodArrayList;
    }

    public void setFoodArrayList(ArrayList<Food> foodArrayList) {
        this.foodArrayList = foodArrayList;
    }
}