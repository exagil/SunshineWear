package com.example.android.sunshine.app.sync;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.WearableListenerService;

public class WearableWeatherSyncService extends WearableListenerService {
    public static final String REQUEST_PATH_SYNC_WEATHER = "/sync/weather";

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        super.onDataChanged(dataEventBuffer);

        for (DataEvent dataEvent : dataEventBuffer) {
            if (isRequestForWeatherSync(dataEvent))
                SunshineSyncAdapter.syncImmediately(getApplicationContext());
        }
    }

    private boolean isRequestForWeatherSync(DataEvent dataEvent) {
        return dataEvent.getDataItem().getUri().toString().contains(REQUEST_PATH_SYNC_WEATHER);
    }
}
