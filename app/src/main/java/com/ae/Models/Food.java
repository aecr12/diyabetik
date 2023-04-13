package com.ae.Models;

import java.util.ArrayList;

public class Food {
    private int foodId;
    private int userId;
    private String name;

    public Food(int foodId, int userId, String name) {
        this.foodId = foodId;
        this.userId = userId;
        this.name = name;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
