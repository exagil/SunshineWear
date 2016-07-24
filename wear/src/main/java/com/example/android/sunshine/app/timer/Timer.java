package com.example.android.sunshine.app.timer;

import java.util.TimeZone;

public class Timer {
    private static final Integer UPDATE_INTERVAL_IN_MILLISECONDS = 500;
    private final TimeHandler timeHandler;

    public Timer(TimeTicker timeTicker) {
        timeHandler = TimeHandler.getInstance(UPDATE_INTERVAL_IN_MILLISECONDS, timeTicker);
    }

    public void update() {
        timeHandler.update();
    }

    public Time getTime() {
        return timeHandler.getTime();
    }

    public void updateTimeZone(TimeZone timeZone) {
        timeHandler.updateTimeZone(timeZone);
    }
}
