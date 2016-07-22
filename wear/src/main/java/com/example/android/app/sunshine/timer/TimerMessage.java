package com.example.android.app.sunshine.timer;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({TimerMessage.MESSAGE_UPDATE_TIME, TimerMessage.MESSAGE_UPDATE_WEATHER})
public @interface TimerMessage {
    int MESSAGE_UPDATE_TIME = 0;
    int MESSAGE_UPDATE_WEATHER = 1;
}
