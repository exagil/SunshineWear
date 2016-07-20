package com.example.wear;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.SurfaceHolder;

import com.example.wear.timer.Time;
import com.example.wear.timer.TimeViewModel;
import com.example.wear.timer.Timer;

public class SunshineWatchFaceService extends CanvasWatchFaceService {
    @Override
    public Engine onCreateEngine() {
        return new SunshineWatchFaceEngine();
    }

    public class SunshineWatchFaceEngine extends CanvasWatchFaceService.Engine {
        public static final int TIME_UPDATE_INTERVAL = 500;
        private Timer timer;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            this.timer = Timer.getInstance(TIME_UPDATE_INTERVAL, this);
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
        }

        @Override
        public void setWatchFaceStyle(WatchFaceStyle watchFaceStyle) {
            super.setWatchFaceStyle(watchFaceStyle);
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            super.onDraw(canvas, bounds);
            if (isVisible()) {
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
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            timer.begin();
        }

        public boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }
    }
}
