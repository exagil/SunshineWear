package com.example.android.app.sunshine;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.Executor;

public class WeatherUpdateExecutor implements Executor, ResultCallback<DataApi.DataItemResult> {
    private static final String KEY_SYSTEM_TIME = "com.example.wear.KEY_SYSTEM_TIME";
    private GoogleApiClient googleApiClient;

    public WeatherUpdateExecutor(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    public void run() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                PutDataMapRequest weatherUpdateDataMapRequest = PutDataMapRequest.create("/bla");
                DataMap weatherUpdateRequestDataMap = weatherUpdateDataMapRequest.getDataMap();
                weatherUpdateRequestDataMap.putLong(KEY_SYSTEM_TIME, System.currentTimeMillis());
                PutDataRequest weatherUpdateRequest = weatherUpdateDataMapRequest.asPutDataRequest();
                weatherUpdateRequest.setUrgent();
                PendingResult<DataApi.DataItemResult> wearableUpdatePendingResult = Wearable.DataApi.putDataItem(googleApiClient, weatherUpdateRequest);
                wearableUpdatePendingResult.setResultCallback(WeatherUpdateExecutor.this);
            }
        };
        this.execute(runnable);
    }

    @Override
    public void execute(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
        
    }
}
