package com.example.android.app.sunshine.timer;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.example.android.app.sunshine.SunshineWatchFaceService;

import java.util.TimeZone;

public class Timer extends Handler {
    private static SunshineWatchFaceService.SunshineWatchFaceEngine engine;
    private TimeZone timeZone = TimeZone.getDefault();

    public static Timer getInstance(@NonNull Integer updateInterval, SunshineWatchFaceService.SunshineWatchFaceEngine engine) {
        TimeHandlerCallback timeHandlerCallback = new TimeHandlerCallback(updateInterval, engine);
        Timer timer = new Timer(timeHandlerCallback, engine);
        timeHandlerCallback.setTimeHandler(timer);
        return timer;
    }

    private Timer(TimeHandlerCallback callback, SunshineWatchFaceService.SunshineWatchFaceEngine engine) {
        super(callback);
        this.engine = engine;
    }

    public void update() {
        this.removeMessages(TimerMessage.MESSAGE_UPDATE_TIME);
        this.removeMessages(TimerMessage.MESSAGE_UPDATE_WEATHER);

        if (engine.shouldTimerBeRunning()) {
            this.sendEmptyMessage(TimerMessage.MESSAGE_UPDATE_TIME);
            this.sendEmptyMessage(TimerMessage.MESSAGE_UPDATE_WEATHER);
        }
    }

    public Time getTime() {
        return Time.now(timeZone);
    }

    public void updateTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
}
