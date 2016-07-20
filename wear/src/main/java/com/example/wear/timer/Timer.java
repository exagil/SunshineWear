package com.example.wear.timer;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.example.wear.SunshineWatchFaceService;

public class Timer extends Handler {
    private static int MESSAGE_UPDATE_TIME = 0;

    public static Timer getInstance(@NonNull Integer updateInterval, SunshineWatchFaceService.SunshineWatchFaceEngine engine) {
        TimeHandlerCallback timeHandlerCallback = new TimeHandlerCallback(updateInterval, MESSAGE_UPDATE_TIME, engine);
        Timer timer = new Timer(timeHandlerCallback);
        timeHandlerCallback.setTimeHandler(timer);
        return timer;
    }

    private Timer(TimeHandlerCallback callback) {
        super(callback);
    }

    public void begin() {
        this.sendEmptyMessage(MESSAGE_UPDATE_TIME);
    }

    public Time getTime() {
        return Time.now();
    }
}
