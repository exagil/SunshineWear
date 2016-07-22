package com.example.android.app.sunshine.timer;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.example.android.app.sunshine.SunshineWatchFaceService;

public class TimeHandlerCallback implements Handler.Callback {
    private Integer updateInterval;
    private SunshineWatchFaceService.SunshineWatchFaceEngine engine;
    private Handler handler;

    public TimeHandlerCallback(@NonNull Integer updateInterval,
                               SunshineWatchFaceService.SunshineWatchFaceEngine engine) {

        this.updateInterval = updateInterval;
        this.engine = engine;
    }

    @Override
    public boolean handleMessage(Message message) {
        engine.invalidate();
        if (!engine.shouldTimerBeRunning()) return false;
        switch (message.what) {
            case TimerMessage.MESSAGE_UPDATE_TIME:
                engine.triggerTimeUpdate(updateInterval, handler);
                break;
            case TimerMessage.MESSAGE_UPDATE_WEATHER:
                engine.triggerWeatherUpdate(updateInterval, handler);
        }
        return false;
    }

    public void setTimeHandler(Timer timer) {
        this.handler = timer;
    }
}
