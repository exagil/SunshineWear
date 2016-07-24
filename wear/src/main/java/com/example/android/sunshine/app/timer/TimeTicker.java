package com.example.android.sunshine.app.timer;

public interface TimeTicker {
    void onTimeUpdate();

    boolean shouldTimerBeRunning();
}
