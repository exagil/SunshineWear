package com.example.android.sunshine.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.SurfaceHolder;

import com.example.android.sunshine.app.face.OnDrawListener;
import com.example.android.sunshine.app.face.WatchFace;
import com.example.android.sunshine.app.sync.WeatherInformationListener;
import com.example.android.sunshine.app.sync.WeatherSyncService;
import com.example.android.sunshine.app.timer.Time;
import com.example.android.sunshine.app.timer.TimeTicker;
import com.example.android.sunshine.app.timer.Timer;

import java.util.TimeZone;

public class SunshineWatchFaceService extends CanvasWatchFaceService {

    // SunshineWatchFaceService knows about the mechanism used in order to render the Android Wear Watch Face

    @Override
    public Engine onCreateEngine() {
        return new SunshineWatchFaceEngine();
    }

    public class SunshineWatchFaceEngine extends CanvasWatchFaceService.Engine implements
            TimeTicker,
            WeatherInformationListener, OnDrawListener {

        // SunshineWatchFaceEngine provides a basis of interaction between the Android Wear Watch Face
        // and the Handheld App

        private boolean hasRegisteredTimeZoneChangedReceiver;
        private TimeZoneReceiver timeZoneReceiver;
        private Double high = Double.NaN;
        private Double low = Double.NaN;
        private Bitmap weatherIcon;
        private Timer timer;
        private WeatherSyncService weatherSyncService;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            timer = new Timer(this);
            timeZoneReceiver = new TimeZoneReceiver();
            weatherSyncService = WeatherSyncService.initialize(getApplicationContext(), this);
            weatherSyncService.performSync();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            invalidate();
            timer.update();
            weatherSyncService.performSync();
        }

        @Override
        public void setWatchFaceStyle(WatchFaceStyle watchFaceStyle) {
            super.setWatchFaceStyle(watchFaceStyle);
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            super.onDraw(canvas, bounds);
            Time time = timer.getTime();
            new WatchFace(canvas, getApplicationContext(), this).show(time, low, high, weatherIcon, isInAmbientMode());
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
                weatherSyncService.performSync();
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

        @Override
        public void onTimeUpdate() {
            invalidate();
        }

        @Override
        public boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        @Override
        public void onTemperatureFetchSuccess(double high, double low) {
            this.high = high;
            this.low = low;
            invalidate();
        }

        @Override
        public void onWeatherIconFetchSuccess(Bitmap icon) {
            this.weatherIcon = icon;
            invalidate();
        }

        @Override
        public void onWeatherInformationFetchFailure() {
        }

        @Override
        public void onDrawSuccess() {
            invalidate();
        }

        private class TimeZoneReceiver extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {
                timer.updateTimeZone(TimeZone.getDefault());
                invalidate();
            }
        }
    }
}
