package com.example.wear;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.SurfaceHolder;

import com.example.wear.timer.Time;
import com.example.wear.timer.TimeViewModel;
import com.example.wear.timer.Timer;

import java.util.TimeZone;

public class SunshineWatchFaceService extends CanvasWatchFaceService {
    @Override
    public Engine onCreateEngine() {
        return new SunshineWatchFaceEngine();
    }

    public class SunshineWatchFaceEngine extends CanvasWatchFaceService.Engine {
        public static final int TIME_UPDATE_INTERVAL = 500;
        private Timer timer;
        private boolean hasRegisteredTimeZoneChangedReceiver;
        private TimeZoneReceiver timeZoneReceiver;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            this.timer = Timer.getInstance(TIME_UPDATE_INTERVAL, this);
            timeZoneReceiver = new TimeZoneReceiver();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            invalidate();
            timer.update();
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

            Paint backgroundPaint = new Paint();
            backgroundPaint.setColor(getResources().getColor(R.color.black));
            canvas.drawPaint(backgroundPaint);

            String hour = timeViewModel.hour();
            String minutes = timeViewModel.minutes();
            String seconds = timeViewModel.seconds();

            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.white));
            paint.setAntiAlias(true);

            canvas.drawText(hour, centerX - 40, centerY, paint);
            canvas.drawText(minutes, centerX, centerY, paint);
            canvas.drawText(seconds, centerX + 40, centerY, paint);
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
            if (visible) registerTimeZoneChangedReceiver();
            else unregisterTimeZoneChangedReceiver();
            timer.update();
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

        private class TimeZoneReceiver extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent intent) {
                timer.updateTimeZone(TimeZone.getDefault());
                invalidate();
            }
        }
    }
}
