package com.example.android.sunshine.app.face;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

import com.example.android.sunshine.app.R;
import com.example.android.sunshine.app.timer.Time;
import com.example.android.sunshine.app.timer.TimeViewModel;

public class WatchFace {
    final String DEGREE = "\u00b0";

    private final Canvas canvas;
    private final Resources resources;
    private OnDrawListener onDrawListener;

    public WatchFace(Canvas canvas, Context context, OnDrawListener onDrawListener) {
        this.canvas = canvas;
        resources = context.getResources();
        this.onDrawListener = onDrawListener;
    }

    public void show(Time time, Double low, Double high, Bitmap bitmap, boolean isInAmbientMode) {
        TimeViewModel timeViewModel = new TimeViewModel(time);
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        float centerY = height / 2f;
        float centerX = width / 2f;
        drawWatchface(canvas, timeViewModel, centerY, centerX, isInAmbientMode, low, high, bitmap);
    }

    private void drawWatchface(Canvas canvas, TimeViewModel timeViewModel, float centerY,
                               float centerX, boolean isInAmbientMode, Double low,
                               Double high, Bitmap weatherIcon) {

        setBackground(canvas, isInAmbientMode);
        String formattedTime = timeViewModel.formattedTime();
        String formattedDate = timeViewModel.formattedDate();
        drawTime(canvas, formattedTime, centerX, centerY, bigTextPaint(56));
        drawDateBelowTime(canvas, formattedDate, centerX, centerY, lightPaint(24), isInAmbientMode);
        drawHorizontalPartition(canvas, centerX, centerY, isInAmbientMode);
        drawWeatherInformation(canvas, centerX, centerY, high, low, isInAmbientMode, weatherIcon);
        onDrawListener.onDrawSuccess();
    }

    private void drawHorizontalPartition(Canvas canvas, float centerX, float centerY, boolean isInAmbientMode) {
        if (isInAmbientMode) return;
        float xCoordinateFactor = (centerX / 5) * 2;
        float startPoint = xCoordinateFactor * 2;
        float endPoint = xCoordinateFactor * 3;
        canvas.drawLine(startPoint, centerY + 20, endPoint, centerY + 20, textPaint());
    }

    private void drawWeatherInformation(Canvas canvas, float centerX, float centerY, Double high,
                                        Double low, boolean isInAmbientMode, Bitmap weatherIcon) {

        if (isInAmbientMode) return;
        if (Double.isNaN(high) && Double.isNaN(low)) return;
        String highTemperature = high.toString().substring(0, 2) + DEGREE;
        String lowTemperature = low.toString().substring(0, 2) + DEGREE;
        drawText(canvas, highTemperature, centerX + 5, bigTextPaint(40), centerY + 80);
        drawText(canvas, lowTemperature, centerX + 70, lightPaintWithSlimText(40), centerY + 80);
        if (isWeatherIconPresent(weatherIcon))
            canvas.drawBitmap(weatherIcon, centerX - 110, centerY + 30, textPaint());
    }

    private boolean isWeatherIconPresent(Bitmap weatherIcon) {
        return weatherIcon != null;
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
        paintLowTemperature.setColor(resources.getColor(R.color.light_watchface_text));
        return paintLowTemperature;
    }

    private Paint bigTextPaint(int textSize) {
        Paint paint = new Paint();
        paint.setColor(resources.getColor(R.color.white));
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        return paint;
    }

    @NonNull
    private Paint textPaint() {
        Paint paint = new Paint();
        paint.setColor(resources.getColor(R.color.white));
        paint.setAntiAlias(true);
        return paint;
    }

    private void setBackground(Canvas canvas, boolean isInAmbientMode) {
        Paint backgroundPaint = new Paint();
        if (!isInAmbientMode)
            backgroundPaint.setColor(resources.getColor(R.color.sunshine_blue));
        else
            backgroundPaint.setColor(resources.getColor(R.color.black));
        canvas.drawPaint(backgroundPaint);
    }

    private void drawTime(Canvas canvas, String formattedTime, float centerX, float centerY, Paint paint) {
        drawText(canvas, formattedTime, centerX, paint, centerY - 50);
    }

    private void drawDateBelowTime(Canvas canvas, String formattedDate, float centerX, float centerY, Paint paint, boolean isInAmbientMode) {
        if (isInAmbientMode) paint.setColor(resources.getColor(R.color.white));
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
