package com.example.android.sunshine.app.face;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

public class WeatherViewModel {
    public static final String FORMAT_PADDING_LEFT_ZEROS = "%02d";
    public static final String DEGREES = "°";
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

    public String highTemperature() {
        return formatTemperature(this.highTemperature);
    }

    public String lowTemperature() {
        return formatTemperature(this.lowTemperature);
    }

    @NonNull
    private String formatTemperature(Double originalTemperature) {
        int originalTemperatureWithoutDecimals = originalTemperature.intValue();
        String formattedTemperature = String.format(FORMAT_PADDING_LEFT_ZEROS, originalTemperatureWithoutDecimals);
        return formattedTemperature + DEGREES;
    }
}
