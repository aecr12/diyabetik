package com.ae.diyabetik;

import java.util.ArrayList;

public class Food {
    private String name;
    private ArrayList<Food> foodArrayList;

    public Food(String name) {
        this.name = name;
        foodArrayList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Food> getFoodArrayList() {
        return foodArrayList;
    }

    public void setFoodArrayList(ArrayList<Food> foodArrayList) {
        this.foodArrayList = foodArrayList;
    }
}