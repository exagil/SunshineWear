package com.example.android.sunshine.app.sync;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

public class WeatherSyncService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        WeatherInformationListener {

    private final GoogleApiClient googleApiClient;
    private final SunshineClient sunshineClient;
    private final WeatherInformationListener weatherInformationListener;

    public static WeatherSyncService initialize(Context context, WeatherInformationListener weatherInformationListener) {
        return new WeatherSyncService(context, weatherInformationListener);
    }

    private WeatherSyncService(Context context, WeatherInformationListener weatherInformationListener) {
        this.weatherInformationListener = weatherInformationListener;
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
        sunshineClient = new SunshineClient(googleApiClient, this);
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.DataApi.addListener(googleApiClient, sunshineClient);
    }

    public void requestWeatherInformationFromHandheld() {
        sunshineClient.requestWeatherInformationFromHandheld();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onWeatherInformationFetchSuccess(double high, double low) {
        weatherInformationListener.onWeatherInformationFetchSuccess(high, low);
    }

    @Override
    public void onWeatherInformationFetchFailure() {
        weatherInformationListener.onWeatherInformationFetchFailure();
    }
}
