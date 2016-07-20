package com.example.wear.timer;

public class TimeViewModel {
    private Time time;

    public TimeViewModel(Time time) {
        this.time = time;
    }

    public String seconds() {
        return String.valueOf(time.seconds());
    }

    public String minutes() {
        return String.valueOf(time.minutes());
    }

    public String hour() {
        return String.valueOf(time.hour());
    }
}
