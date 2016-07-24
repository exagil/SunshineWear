package com.example.android.sunshine.app.timer;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

class TimeHandlerCallback implements Handler.Callback {
    private Integer updateInterval;
    private int messageKey;
    private TimeTicker timeTicker;
    private Handler handler;

    public TimeHandlerCallback(@NonNull Integer updateInterval,
                               int messageKey,
                               TimeTicker timeTicker) {

        this.updateInterval = updateInterval;
        this.messageKey = messageKey;
        this.timeTicker = timeTicker;
    }

    @Override
    public boolean handleMessage(Message message) {
        if (message.what == this.messageKey) {
            timeTicker.onTimeUpdate();
            if (timeTicker.shouldTimerBeRunning()) {
                long currentTimeInMillis = System.currentTimeMillis();
                long timeInMillisToUpdateIn = updateInterval - (currentTimeInMillis % updateInterval);
                handler.sendEmptyMessageDelayed(this.messageKey, timeInMillisToUpdateIn);
            }
        }
        return false;
    }

    public void setTimeHandler(TimeHandler timeHandler) {
        this.handler = timeHandler;
    }
}
