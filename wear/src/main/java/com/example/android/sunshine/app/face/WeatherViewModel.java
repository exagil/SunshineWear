package com.example.android.sunshine.app.face;

import android.graphics.Bitmap;

public class WeatherViewModel {
    public final Double lowTemperature;
    public final Double highTemperature;
    public final Bitmap weatherIcon;

    public WeatherViewModel(Double lowTemperature, Double highTemperature, Bitmap weatherIcon) {
        this.lowTemperature = lowTemperature;
        this.highTemperature = highTemperature;
        this.weatherIcon = weatherIcon;
    }

    public boolean shouldNotDisplay() {
        return lowTemperature.equals(Double.NaN) || highTemperature.equals(Double.NaN);
    }
}
