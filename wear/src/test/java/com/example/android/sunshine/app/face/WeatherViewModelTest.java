package com.example.android.sunshine.app.face;

import android.graphics.Bitmap;

import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class WeatherViewModelTest {
    @Test
    public void testShouldNotDisplayWeatherWithInvalidLowTemperature() {
        Bitmap weatherIcon = Mockito.mock(Bitmap.class);
        WeatherViewModel weatherViewModel = new WeatherViewModel(Double.NaN, new Double(1), weatherIcon);
        assertTrue(weatherViewModel.shouldNotDisplay());
    }

    @Test
    public void testShouldDisplayWeatherWithValidTemperatures() {
        Bitmap weatherIcon = Mockito.mock(Bitmap.class);
        WeatherViewModel weatherViewModel = new WeatherViewModel(new Double(1), new Double(1), weatherIcon);
        assertFalse(weatherViewModel.shouldNotDisplay());
    }

    @Test
    public void testShouldNotDisplayWeatherWithInvalidHighTemperature() {
        Bitmap weatherIcon = Mockito.mock(Bitmap.class);
        WeatherViewModel weatherViewModel = new WeatherViewModel(new Double(1), Double.NaN, weatherIcon);
        assertTrue(weatherViewModel.shouldNotDisplay());
    }
}
