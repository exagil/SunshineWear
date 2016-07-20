package com.example.wear.timer;

import java.util.Calendar;
import java.util.TimeZone;

public class Time {
    private final Calendar calendar;

    public static Time now(TimeZone timeZone) {
        return new Time(System.currentTimeMillis(), timeZone);
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
