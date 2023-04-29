package com.ae.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StepCountReceiver extends BroadcastReceiver {

    private StepCountListener listener;
    private int stepCount;
    public StepCountReceiver(StepCountListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("step_count_changed")) {
                stepCount = intent.getIntExtra("step_count", 0);
                listener.onStepCountChanged(stepCount);
            }
        }
    }
    public interface StepCountListener {
        void onStepCountChanged(int stepCount);
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }
}
