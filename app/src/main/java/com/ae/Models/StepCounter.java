package com.ae.Models;

import java.util.Date;

public class StepCounter {
    private String id;
    private int stepCount;

    public StepCounter() {
    }

    public StepCounter(String id, int stepCount) {
        this.id = id;
        this.stepCount = stepCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }
}
