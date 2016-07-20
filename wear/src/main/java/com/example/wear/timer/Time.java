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
}
