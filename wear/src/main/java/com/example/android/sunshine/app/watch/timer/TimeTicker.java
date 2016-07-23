package com.example.android.sunshine.app.watch.timer;

public interface TimeTicker {
    void onTimeTick();

    boolean shouldTimerBeRunning();
}
