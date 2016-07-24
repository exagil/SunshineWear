package com.example.android.sunshine.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.SurfaceHolder;

import com.example.android.sunshine.app.sync.WeatherInformationListener;
import com.example.android.sunshine.app.sync.WeatherSyncService;
import com.example.android.sunshine.app.timer.Time;
import com.example.android.sunshine.app.timer.TimeTicker;
import com.example.android.sunshine.app.timer.TimeViewModel;
import com.example.android.sunshine.app.timer.Timer;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.TimeZone;

public class SunshineWatchFaceService extends CanvasWatchFaceService {

    // SunshineWatchFaceService knows about the mechanism used in order to render the Android Wear Watch Face

    @Override
    public Engine onCreateEngine() {
        return new SunshineWatchFaceEngine();
    }

    public class SunshineWatchFaceEngine extends CanvasWatchFaceService.Engine implements
            TimeTicker,
            WeatherInformationListener {

        // SunshineWatchFaceEngine provides a basis of interaction between the Android Wear Watch Face
        // and the Handheld App

        final String DEGREE = "\u00b0";
        public static final int TIME_UPDATE_INTERVAL = 500;
        private boolean hasRegisteredTimeZoneChangedReceiver;
        private TimeZoneReceiver timeZoneReceiver;
        private GoogleApiClient googleApiClient;
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
            TimeViewModel timeViewModel = new TimeViewModel(time);

            int height = canvas.getHeight();
            int width = canvas.getWidth();
            float centerY = height / 2f;
            float centerX = width / 2f;

            drawWatchface(canvas, timeViewModel, centerY, centerX);
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
            String formattedTime = timeViewModel.formattedTime();
            String formattedDate = timeViewModel.formattedDate();
            drawTime(canvas, formattedTime, centerX, centerY, bigTextPaint(56));
            drawDateBelowTime(canvas, formattedDate, centerX, centerY, lightPaint(24), isInAmbientMode);
            drawHorizontalPartition(canvas, centerX, centerY, isInAmbientMode);
            drawWeatherInformation(canvas, centerX, centerY, high, low, isInAmbientMode);
        }

        private void drawHorizontalPartition(Canvas canvas, float centerX, float centerY, boolean isInAmbientMode) {
            if (isInAmbientMode) return;
            float xCoordinateFactor = (centerX / 5) * 2;
            float startPoint = xCoordinateFactor * 2;
            float endPoint = xCoordinateFactor * 3;
            canvas.drawLine(startPoint, centerY + 20, endPoint, centerY + 20, textPaint());
        }

        private void drawWeatherInformation(Canvas canvas, float centerX, float centerY, Double high, Double low, boolean isInAmbientMode) {
            if (isInAmbientMode) return;
            if (Double.isNaN(high) && Double.isNaN(low)) return;
            String highTemperature = high.toString().substring(0, 2) + DEGREE;
            String lowTemperature = low.toString().substring(0, 2) + DEGREE;
            drawText(canvas, highTemperature, centerX + 5, bigTextPaint(40), centerY + 80);
            drawText(canvas, lowTemperature, centerX + 70, lightPaintWithSlimText(40), centerY + 80);
            if (isWeatherIconPresent())
                canvas.drawBitmap(weatherIcon, centerX - 110, centerY + 30, textPaint());
        }

        private Paint lightPaintWithSlimText(int textSize) {
            Typeface typeface = Typeface.create("sans-serif-light", Typeface.NORMAL);
            Paint paint = lightPaint(textSize);
            paint.setTypeface(typeface);
            return paint;
        }

        @NonNull
        private Paint lightPaint(int textSize) {
            Paint paintLowTemperature = bigTextPaint(textSize);
            paintLowTemperature.setColor(getResources().getColor(R.color.light_watchface_text));
            return paintLowTemperature;
        }

        private Paint bigTextPaint(int textSize) {
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.white));
            paint.setTextSize(textSize);
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
            drawText(canvas, formattedTime, centerX, paint, centerY - 50);
        }

        private void drawDateBelowTime(Canvas canvas, String formattedDate, float centerX, float centerY, Paint paint, boolean isInAmbientMode) {
            if (isInAmbientMode) paint.setColor(getResources().getColor(R.color.white));
            drawText(canvas, formattedDate, centerX, paint, centerY - 5);
        }

        private void drawText(Canvas canvas, @NonNull String text, float centerX, Paint paint, float positionY) {
            float positionOnXCoordinate = centerX - (widthOfText(text, paint) / 2f);
            canvas.drawText(text, positionOnXCoordinate, positionY, paint);
        }

        private float widthOfText(String text, Paint paint) {
            return paint.measureText(text, 0, text.length());
        }
    }
}
