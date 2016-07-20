package com.example.wear.timer;

import java.util.Calendar;
import java.util.TimeZone;

public class Time {
    private final Calendar calendar;

    public static Time now() {
        return new Time(System.currentTimeMillis());
    }

    private Time(long currentTimeInMillis) {
        calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeInMillis(currentTimeInMillis);
    }

    public int hour() {
        return calendar.get(Calendar.HOUR);
    }

    public int minutes() {
        return calendar.get(Calendar.MINUTE);
    }

    public int seconds() {
        return calendar.get(Calendar.SECOND);
    }
}
