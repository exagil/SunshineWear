package com.example.android.sunshine.app.watch.timer;

import java.util.Calendar;
import java.util.TimeZone;

public class Time {
    public static long currentTimeInMillis;
    private final Calendar calendar;

    public static Time now(TimeZone timeZone) {
        currentTimeInMillis = System.currentTimeMillis();
        return new Time(currentTimeInMillis, timeZone);
    }

    private Time(long currentTimeInMillis, TimeZone timeZone) {
        calendar = Calendar.getInstance(timeZone);
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
