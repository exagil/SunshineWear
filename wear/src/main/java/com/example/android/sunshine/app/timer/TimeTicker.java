package com.example.android.sunshine.app.timer;

public interface TimeTicker {
    void onTimeTick();

    boolean shouldTimerBeRunning();
}
