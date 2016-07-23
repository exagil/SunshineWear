package com.example.android.sunshine.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.android.sunshine.app.timer.Time;
import com.example.android.sunshine.app.timer.TimeViewModel;
import com.example.android.sunshine.app.timer.Timer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.util.TimeZone;

public class SunshineWatchFaceService extends CanvasWatchFaceService {
    @Override
    public Engine onCreateEngine() {
        return new SunshineWatchFaceEngine();
    }

    public class SunshineWatchFaceEngine extends CanvasWatchFaceService.Engine implements
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
            DataApi.DataListener {

        public static final int TIME_UPDATE_INTERVAL = 500;
        private Timer timer;
        private boolean hasRegisteredTimeZoneChangedReceiver;
        private TimeZoneReceiver timeZoneReceiver;
        private GoogleApiClient googleApiClient;
        private Double high = Double.NaN;
        private Double low = Double.NaN;
        private Bitmap weatherIcon;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            this.timer = Timer.getInstance(TIME_UPDATE_INTERVAL, this);
            timeZoneReceiver = new TimeZoneReceiver();
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Wearable.API)
                    .build();
            googleApiClient.connect();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            invalidate();
            timer.update();
            requestWeatherInfoFromHandheld();
        }

        @Override
        public void setWatchFaceStyle(WatchFaceStyle watchFaceStyle) {
            super.setWatchFaceStyle(watchFaceStyle);
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            super.onDraw(canvas, bounds);
            Time time = timer.getTime();
            TimeViewModel timeViewModel = new TimeViewModel(time);

            int height = canvas.getHeight();
            int width = canvas.getWidth();
            float centerY = height / 2f;
            float centerX = width / 2f;

            drawWatchface(canvas, timeViewModel, centerY, centerX);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                registerTimeZoneChangedReceiver();
                timer.update();
                requestWeatherInfoFromHandheld();
            } else
                unregisterTimeZoneChangedReceiver();
        }

        private void unregisterTimeZoneChangedReceiver() {
            if (!hasRegisteredTimeZoneChangedReceiver)
                return;
            hasRegisteredTimeZoneChangedReceiver = false;
            SunshineWatchFaceService.this.unregisterReceiver(timeZoneReceiver);
        }

        private void registerTimeZoneChangedReceiver() {
            if (hasRegisteredTimeZoneChangedReceiver)
                return;
            hasRegisteredTimeZoneChangedReceiver = true;
            IntentFilter timeZoneChangedIntentFilter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            SunshineWatchFaceService.this.registerReceiver(timeZoneReceiver, timeZoneChangedIntentFilter);
        }

        public boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            Wearable.DataApi.addListener(googleApiClient, this);
            requestWeatherInfoFromHandheld();
        }

        private void requestWeatherInfoFromHandheld() {
            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(WeatherRequestKeys.SYNC_PATH + System.currentTimeMillis());
            putDataMapRequest.getDataMap().putInt("", 0);
            PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();
            putDataRequest.setUrgent();
            Wearable.DataApi.putDataItem(googleApiClient, putDataRequest);
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

        @Override
        public void onDataChanged(DataEventBuffer dataEventBuffer) {
            if (dataEventBuffer == null) return;
            for (DataEvent dataEvent : dataEventBuffer) {
                if (doesHaveWeatherData(dataEvent)) {
                    DataMap weatherDataMap = getWeatherDataMap(dataEvent);
                    high = weatherDataMap.getDouble(WeatherRequestKeys.HIGH);
                    low = weatherDataMap.getDouble(WeatherRequestKeys.LOW);
                    fetchWeatherIconAsynchronously(weatherDataMap);
                }
            }
            invalidate();
        }

        private void fetchWeatherIconAsynchronously(DataMap weatherDataMap) {
            Asset iconAsset = weatherDataMap.getAsset(WeatherRequestKeys.ICON);
            if (googleApiClient.isConnected()) {
                PendingResult<DataApi.GetFdForAssetResult> fileDescriptorForIconAsset =
                        Wearable.DataApi.getFdForAsset(googleApiClient, iconAsset);
                obtainWeatherIconUsingFileDescriptor(fileDescriptorForIconAsset);
            }
        }

        private void obtainWeatherIconUsingFileDescriptor(PendingResult<DataApi.GetFdForAssetResult> fileDescriptorForIconAsset) {
            fileDescriptorForIconAsset.setResultCallback(new ResultCallback<DataApi.GetFdForAssetResult>() {
                @Override
                public void onResult(@NonNull DataApi.GetFdForAssetResult getFdForAssetResult) {
                    InputStream weatherIconInputStream = getFdForAssetResult.getInputStream();
                    Bitmap fetchedWeatherIcon = BitmapFactory.decodeStream(weatherIconInputStream);
                    if (fetchedWeatherIcon == null) return;
                    weatherIcon = fetchedWeatherIcon;
                    invalidate();
                }
            });
        }

        private DataMap getWeatherDataMap(DataEvent dataEvent) {
            DataItem weatherDataItem = dataEvent.getDataItem();
            DataMapItem weatherDataMapItem = DataMapItem.fromDataItem(weatherDataItem);
            return weatherDataMapItem.getDataMap();
        }

        private boolean doesHaveWeatherData(DataEvent dataEvent) {
            return dataEvent.getDataItem().getUri().toString().contains(WeatherRequestKeys.DATA_PATH);
        }

        private class TimeZoneReceiver extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {
                timer.updateTimeZone(TimeZone.getDefault());
                invalidate();
            }
        }

        private void drawWatchface(Canvas canvas, TimeViewModel timeViewModel, float centerY, float centerX) {
            boolean isInAmbientMode = isInAmbientMode();
            setBackground(canvas, isInAmbientMode);
            Paint textPaint = textPaint();
            String formattedTime = timeViewModel.formattedTime();
            String formattedDate = timeViewModel.formattedDate();
            drawTime(canvas, formattedTime, centerX, centerY, bigTextPaint());
            drawDateBelowTime(canvas, formattedDate, centerX, centerY, bigTextPaint());
            drawHorizontalPartition(canvas, centerX, centerY);
            drawWeatherInformation(canvas, centerX, centerY, high, low);
        }

        private void drawHorizontalPartition(Canvas canvas, float centerX, float centerY) {
            float xCoordinateFactor = (centerX / 5) * 2;
            float startPoint = xCoordinateFactor * 2;
            float endPoint = xCoordinateFactor * 3;
            canvas.drawLine(startPoint, centerY, endPoint, centerY, textPaint());
        }

        private void drawWeatherInformation(Canvas canvas, float centerX, float centerY, Double high, Double low) {
            if (Double.isNaN(high) && Double.isNaN(low)) return;
            String highTemperature = high.toString().substring(0, 2);
            String lowTemperature = low.toString().substring(0, 2);
            Log.d("chi6rag", "" + highTemperature);
            Log.d("chi6rag", "" + lowTemperature);
            drawText(canvas, highTemperature, centerX, bigTextPaint(), centerY + 80);
            drawText(canvas, lowTemperature, centerX + 60, bigTextPaint(), centerY + 80);
            if (isWeatherIconPresent())
                canvas.drawBitmap(weatherIcon, centerX - 110, centerY + 30, textPaint());
        }

        private Paint bigTextPaint() {
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.white));
            paint.setTextSize(24);
            paint.setAntiAlias(true);
            return paint;
        }

        private boolean isWeatherIconPresent() {
            return weatherIcon != null;
        }

        @NonNull
        private Paint textPaint() {
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.white));
            paint.setAntiAlias(true);
            return paint;
        }

        private void setBackground(Canvas canvas, boolean isInAmbientMode) {
            Paint backgroundPaint = new Paint();
            if (!isInAmbientMode)
                backgroundPaint.setColor(getResources().getColor(R.color.sunshine_blue));
            else
                backgroundPaint.setColor(getResources().getColor(R.color.black));
            canvas.drawPaint(backgroundPaint);
        }

        private void drawTime(Canvas canvas, String formattedTime, float centerX, float centerY, Paint paint) {
            drawText(canvas, formattedTime, centerX, paint, centerY - 70);
        }

        private void drawDateBelowTime(Canvas canvas, String formattedDate, float centerX, float centerY, Paint paint) {
            drawText(canvas, formattedDate, centerX, paint, centerY - 30);
        }

        private void drawText(Canvas canvas, @NonNull String text, float centerX, Paint paint, float positionY) {
            float positionOnXCoordinate = centerX - (widthOfText(text) / 2f);
            canvas.drawText(text, positionOnXCoordinate, positionY, paint);
        }

        private float widthOfText(String text) {
            return new Paint().measureText(text, 0, text.length());
        }
    }
}
