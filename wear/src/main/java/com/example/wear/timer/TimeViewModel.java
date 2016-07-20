package com.example.wear.timer;

public class TimeViewModel {
    private Time time;

    public TimeViewModel(Time time) {
        this.time = time;
    }

    public String second() {
        return String.valueOf(time.second());
    }

    public String minutes() {
        return String.valueOf(time.minute());
    }

    public String hour() {
        return String.valueOf(time.hour());
    }
}
