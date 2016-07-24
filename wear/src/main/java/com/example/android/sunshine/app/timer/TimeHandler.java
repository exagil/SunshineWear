package com.example.android.sunshine.app.timer;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.TimeZone;

class TimeHandler extends Handler {
    private static int MESSAGE_UPDATE_TIME = 0;
    private static TimeTicker timeTicker;
    private TimeZone timeZone = TimeZone.getDefault();

    public static TimeHandler getInstance(@NonNull Integer updateIntervalInMilliseconds, TimeTicker timeTicker) {
        TimeHandlerCallback timeHandlerCallback = new TimeHandlerCallback(updateIntervalInMilliseconds, MESSAGE_UPDATE_TIME, timeTicker);
        TimeHandler timeHandler = new TimeHandler(timeHandlerCallback, timeTicker);
        timeHandlerCallback.setTimeHandler(timeHandler);
        return timeHandler;
    }

    private TimeHandler(TimeHandlerCallback callback, TimeTicker timeTicker) {
        super(callback);
        this.timeTicker = timeTicker;
    }

    public void update() {
        this.removeMessages(MESSAGE_UPDATE_TIME);
        if (timeTicker.shouldTimerBeRunning())
            this.sendEmptyMessage(MESSAGE_UPDATE_TIME);
    }

    public Time getTime() {
        return Time.now(timeZone);
    }

    public void updateTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
}
