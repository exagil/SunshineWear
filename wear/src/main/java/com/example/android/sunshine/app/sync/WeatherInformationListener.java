package com.example.android.sunshine.app.sync;

import android.graphics.Bitmap;

public interface WeatherInformationListener {
    void onTemperatureFetchSuccess(double high, double low);

    void onWeatherIconFetchSuccess(Bitmap icon);

    void onWeatherInformationFetchFailure();
}
