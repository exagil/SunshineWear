package com.example.android.sunshine.app.sync;

import com.example.android.sunshine.app.WeatherRequestKeys;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class SunshineClient implements DataApi.DataListener {
    public static final String BLANK_REQUEST_KEY = "";
    public static final int BLANK_REQUEST_VALUE = 0;
    private GoogleApiClient googleApiClient;
    private WeatherInformationListener weatherInformationListener;

    public SunshineClient(GoogleApiClient googleApiClient, WeatherInformationListener weatherInformationListener) {
        this.googleApiClient = googleApiClient;
        this.weatherInformationListener = weatherInformationListener;
        googleApiClient.connect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        if (dataEventBuffer == null) {
            weatherInformationListener.onWeatherInformationFetchFailure();
            return;
        }
        for (DataEvent dataEvent : dataEventBuffer) {
            if (doesHaveWeatherData(dataEvent)) {
                DataMap weatherDataMap = getWeatherDataMap(dataEvent);
                Double high = weatherDataMap.getDouble(WeatherRequestKeys.HIGH);
                Double low = weatherDataMap.getDouble(WeatherRequestKeys.LOW);
                weatherInformationListener.onWeatherInformationFetchSuccess(high, low);
                return;
            }
        }
    }

    public void requestWeatherInformationFromHandheld() {
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(WeatherRequestKeys.SYNC_PATH + System.currentTimeMillis());
        putDataMapRequest.getDataMap().putInt(BLANK_REQUEST_KEY, BLANK_REQUEST_VALUE);
        PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();
        putDataRequest.setUrgent();
        Wearable.DataApi.putDataItem(googleApiClient, putDataRequest);
    }

    private boolean doesHaveWeatherData(DataEvent dataEvent) {
        return dataEvent.getDataItem().getUri().toString().contains(WeatherRequestKeys.DATA_PATH);
    }

    private DataMap getWeatherDataMap(DataEvent dataEvent) {
        DataItem weatherDataItem = dataEvent.getDataItem();
        DataMapItem weatherDataMapItem = DataMapItem.fromDataItem(weatherDataItem);
        return weatherDataMapItem.getDataMap();
    }
}
