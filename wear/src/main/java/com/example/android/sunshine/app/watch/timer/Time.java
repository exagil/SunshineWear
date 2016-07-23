package com.example.android.sunshine.app.watch.timer;

public class Time {
    private long timeInMillis;

    public Time(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || !(that instanceof Time)) return false;
        Time thatTime = (Time) that;
        return this.timeInMillis == thatTime.timeInMillis;
    }

    @Override
    public int hashCode() {
        return (int) (timeInMillis ^ (timeInMillis >>> 32));
    }
}
