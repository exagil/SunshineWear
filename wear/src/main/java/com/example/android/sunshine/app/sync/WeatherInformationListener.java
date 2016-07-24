package com.example.android.sunshine.app.sync;

public interface WeatherInformationListener {
    void onWeatherInformationFetchSuccess(double high, double low);

    void onWeatherInformationFetchFailure();
}
