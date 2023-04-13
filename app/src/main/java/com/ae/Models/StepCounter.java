package com.ae.Models;

import java.util.Date;

public class StepCounter {
    private int id;
    private int userId;
    private String date;
    private String stepCount;
    private String calorie;
    private String distance;
    private String elapsedTime;

    public StepCounter(int id, int userId, String date, String stepCount, String calorie, String distance, String elapsedTime) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.stepCount = stepCount;
        this.calorie = calorie;
        this.distance = distance;
        this.elapsedTime = elapsedTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStepCount() {
        return stepCount;
    }

    public void setStepCount(String stepCount) {
        this.stepCount = stepCount;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}
