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
    public static final String TYPEFACE_SANS_SERIF_LIGHT = "sans-serif-light";
    public static final int INDEX_TEXT_START = 0;
    public static final int TEXT_SIZE_TIME = 56;
    public static final int OFFSET_Y_TIME = 50;
    public static final int OFFSET_Y_DATE = 5;
    public static final int TEXT_SIZE_DATE = 24;
    public static final int OFFSET_X_HIGH_TEMPERATURE = 5;
    public static final int TEXT_SIZE_HIGH_TEMPERATURE = 40;
    public static final int OFFSET_Y_HIGH_TEMPERATURE = 80;
    public static final int OFFSET_X_LOW_TEMPERATURE = 70;
    public static final int TEXT_SIZE_LOW_TEMPERATURE = 40;
    public static final int OFFSET_Y_LOW_TEMPERATURE = 80;
    public static final int OFFSET_X_WEATHER_ICON = 110;
    public static final int OFFSET_Y_WEATHER_ICON = 30;

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
        WeatherViewModel weatherViewModel = new WeatherViewModel(low, high, bitmap);
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        drawWatchface(canvas, timeViewModel, center(height), center(width), isInAmbientMode, weatherViewModel);
    }

    private float center(int height) {
        return height / 2f;
    }

    private void drawWatchface(Canvas canvas, TimeViewModel timeViewModel, float centerY,
                               float centerX, boolean isInAmbientMode,
                               WeatherViewModel weatherViewModel) {

        setBackground(canvas, isInAmbientMode);
        String formattedTime = timeViewModel.formattedTime();
        String formattedDate = timeViewModel.formattedDate();
        drawTime(canvas, formattedTime, centerX, centerY, isInAmbientMode);
        drawDateBelowTime(canvas, formattedDate, centerX, centerY, isInAmbientMode);
        drawHorizontalPartition(canvas, centerX, centerY, isInAmbientMode);
        drawWeatherInformation(canvas, weatherViewModel, centerX, centerY, isInAmbientMode);
        onDrawListener.onDrawSuccess();
    }

    private void setBackground(Canvas canvas, boolean isInAmbientMode) {
        Paint backgroundPaint = new Paint();
        if (!isInAmbientMode)
            backgroundPaint.setColor(resources.getColor(R.color.sunshine_blue));
        else
            backgroundPaint.setColor(resources.getColor(R.color.black));
        canvas.drawPaint(backgroundPaint);
    }

    private void drawHorizontalPartition(Canvas canvas, float centerX, float centerY, boolean isInAmbientMode) {
        if (isInAmbientMode) return;
        float xCoordinateFactor = (centerX / 5) * 2;
        float startPoint = xCoordinateFactor * 2;
        float endPoint = xCoordinateFactor * 3;
        canvas.drawLine(startPoint, centerY + 20, endPoint, centerY + 20, textPaint());
    }

    private void drawWeatherInformation(Canvas canvas, WeatherViewModel weatherViewModel,
                                        float centerX, float centerY, boolean isInAmbientMode) {

        if (isInAmbientMode || weatherViewModel.shouldNotDisplay()) return;
        String highTemperature = weatherViewModel.highTemperature();
        String lowTemperature = weatherViewModel.lowTemperature();
        drawHighTemperature(canvas, centerX, centerY, highTemperature);
        drawLowTemperature(canvas, centerX, centerY, lowTemperature);
        drawWeatherIcon(canvas, weatherViewModel, centerX, centerY);
    }

    private void drawWeatherIcon(Canvas canvas, WeatherViewModel weatherViewModel, float centerX, float centerY) {
        if (weatherViewModel.isWeatherIconPresent())
            canvas.drawBitmap(weatherViewModel.weatherIcon, centerX - OFFSET_X_WEATHER_ICON,
                    centerY + OFFSET_Y_WEATHER_ICON, textPaint());
    }

    private void drawLowTemperature(Canvas canvas, float centerX, float centerY, String lowTemperature) {
        drawText(canvas, lowTemperature, centerX + OFFSET_X_LOW_TEMPERATURE,
                lightPaintWithBigSlimText(TEXT_SIZE_LOW_TEMPERATURE, false),
                centerY + OFFSET_Y_LOW_TEMPERATURE);
    }

    private void drawHighTemperature(Canvas canvas, float centerX, float centerY, String highTemperature) {
        drawText(canvas, highTemperature, centerX + OFFSET_X_HIGH_TEMPERATURE,
                bigTextPaint(TEXT_SIZE_HIGH_TEMPERATURE), centerY + OFFSET_Y_HIGH_TEMPERATURE);
    }

    private Paint lightPaintWithBigSlimText(int textSize, boolean isInAmbientMode) {
        Typeface typeface = Typeface.create(TYPEFACE_SANS_SERIF_LIGHT, Typeface.NORMAL);
        Paint paint = lightPaintWithBigText(textSize, isInAmbientMode);
        paint.setTypeface(typeface);
        return paint;
    }

    @NonNull
    private Paint lightPaintWithBigText(int textSize, boolean isInAmbientMode) {
        Paint lightPaint = bigTextPaint(textSize);
        if (isInAmbientMode) lightPaint.setColor(resources.getColor(R.color.white));
        else lightPaint.setColor(resources.getColor(R.color.light_watchface_text));
        return lightPaint;
    }

    private Paint bigTextPaint(int textSize) {
        Paint bigTextPaint = textPaint();
        bigTextPaint.setTextSize(textSize);
        return bigTextPaint;
    }

    private Paint bigAntiAliasTextPaint(int textSize) {
        Paint bigTextPaint = bigTextPaint(textSize);
        bigTextPaint.setAntiAlias(false);
        return bigTextPaint;
    }

    @NonNull
    private Paint textPaint() {
        Paint paint = new Paint();
        paint.setColor(resources.getColor(R.color.white));
        paint.setAntiAlias(true);
        return paint;
    }

    private void drawTime(Canvas canvas, String formattedTime, float centerX, float centerY, boolean isInAmbientMode) {
        Paint paint = (isInAmbientMode) ? bigAntiAliasTextPaint(TEXT_SIZE_TIME) : bigTextPaint(TEXT_SIZE_TIME);
        drawText(canvas, formattedTime, centerX, paint, centerY - OFFSET_Y_TIME);
    }

    private void drawDateBelowTime(Canvas canvas, String formattedDate, float centerX,
                                   float centerY, boolean isInAmbientMode) {

        Paint paint = lightPaintWithBigText(TEXT_SIZE_DATE, isInAmbientMode);
        if (isInAmbientMode) paint.setAntiAlias(false);
        drawText(canvas, formattedDate, centerX, paint, centerY - OFFSET_Y_DATE);
    }

    private void drawText(Canvas canvas, @NonNull String text, float centerX, Paint paint, float positionY) {
        float positionOnXCoordinate = centerX - halfOfWidthOfText(text, paint);
        canvas.drawText(text, positionOnXCoordinate, positionY, paint);
    }

    private float halfOfWidthOfText(@NonNull String text, Paint paint) {
        return widthOfText(text, paint) / 2f;
    }

    private float widthOfText(String text, Paint paint) {
        return paint.measureText(text, INDEX_TEXT_START, text.length());
    }
}
