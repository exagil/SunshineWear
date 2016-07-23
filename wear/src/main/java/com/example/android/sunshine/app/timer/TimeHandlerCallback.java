package com.example.android.sunshine.app.timer;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.example.android.sunshine.app.SunshineWatchFaceService;

public class TimeHandlerCallback implements Handler.Callback {
    private Integer updateInterval;
    private int messageKey;
    private SunshineWatchFaceService.SunshineWatchFaceEngine engine;
    private Handler handler;

    public TimeHandlerCallback(@NonNull Integer updateInterval,
                               int messageKey,
                               SunshineWatchFaceService.SunshineWatchFaceEngine engine) {

        this.updateInterval = updateInterval;
        this.messageKey = messageKey;
        this.engine = engine;
    }

    @Override
    public boolean handleMessage(Message message) {
        if (message.what == this.messageKey) {
            engine.invalidate();
            if (engine.shouldTimerBeRunning()) {
                long currentTimeInMillis = System.currentTimeMillis();
                long timeInMillisToUpdateIn = updateInterval - (currentTimeInMillis % updateInterval);
                handler.sendEmptyMessageDelayed(this.messageKey, timeInMillisToUpdateIn);
            }
        }
        return false;
    }

    public void setTimeHandler(Timer timer) {
        this.handler = timer;
    }
}
