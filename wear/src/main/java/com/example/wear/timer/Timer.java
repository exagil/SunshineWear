package com.example.wear.timer;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.example.wear.SunshineWatchFaceService;

import java.util.TimeZone;

public class Timer extends Handler {
    private static int MESSAGE_UPDATE_TIME = 0;
    private static SunshineWatchFaceService.SunshineWatchFaceEngine engine;
    private TimeZone timeZone = TimeZone.getDefault();

    public static Timer getInstance(@NonNull Integer updateInterval, SunshineWatchFaceService.SunshineWatchFaceEngine engine) {
        TimeHandlerCallback timeHandlerCallback = new TimeHandlerCallback(updateInterval, MESSAGE_UPDATE_TIME, engine);
        Timer timer = new Timer(timeHandlerCallback, engine);
        timeHandlerCallback.setTimeHandler(timer);
        return timer;
    }

    private Timer(TimeHandlerCallback callback, SunshineWatchFaceService.SunshineWatchFaceEngine engine) {
        super(callback);
        this.engine = engine;
    }

    public void update() {
        this.removeMessages(MESSAGE_UPDATE_TIME);
        if (engine.shouldTimerBeRunning())
            this.sendEmptyMessage(MESSAGE_UPDATE_TIME);
    }

    public Time getTime() {
        return Time.now(timeZone);
    }

    public void updateTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
}
