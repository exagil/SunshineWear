package com.example.android.sunshine.app.face;

import android.graphics.Bitmap;

import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;
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

    @Test
    public void testThatZeroIsPrependedToHighTemperatureIfSingleDigit() {
        WeatherViewModel weatherViewModel = new WeatherViewModel(new Double(0), new Double(3), null);
        assertEquals("03°", weatherViewModel.highTemperature());
    }

    @Test
    public void testThatZeroIsNotPrependedToHighTemperatureIfMultiDigit() {
        WeatherViewModel weatherViewModel = new WeatherViewModel(new Double(0), new Double(27), null);
        assertEquals("27°", weatherViewModel.highTemperature());
    }

    @Test
    public void testThatNonDecimalValueOfHighTemperatureIsShownIfHighTemperatureContainsDecimal() {
        WeatherViewModel weatherViewModel = new WeatherViewModel(new Double(0), new Double(27.1397d), null);
        assertEquals("27°", weatherViewModel.highTemperature());
    }

    @Test
    public void testThatZeroIsPrependedToLowTemperatureIfSingleDigit() {
        WeatherViewModel weatherViewModel = new WeatherViewModel(new Double(3), new Double(0), null);
        assertEquals("03°", weatherViewModel.lowTemperature());
    }

    @Test
    public void testThatZeroIsNotPrependedToLowTemperatureIfMultiDigit() {
        WeatherViewModel weatherViewModel = new WeatherViewModel(new Double(27), new Double(0), null);
        assertEquals("27°", weatherViewModel.lowTemperature());
    }

    @Test
    public void testThatNonDecimalValueOfLowTemperatureIsShownIfLowTemperatureContainsDecimal() {
        WeatherViewModel weatherViewModel = new WeatherViewModel(new Double(27.1397d), new Double(0), null);
        assertEquals("27°", weatherViewModel.lowTemperature());
    }

    @Test
    public void testShouldKnowIfWeatherIconIsPresent() {
        Bitmap weatherIcon = Mockito.mock(Bitmap.class);
        WeatherViewModel weatherViewModel = new WeatherViewModel(1d, 1d, weatherIcon);
        assertTrue(weatherViewModel.isWeatherIconPresent());
    }

    @Test
    public void testShouldKnowIfWeatherIconIsNotPresent() {
        WeatherViewModel weatherViewModel = new WeatherViewModel(1d, 1d, null);
        assertFalse(weatherViewModel.isWeatherIconPresent());
    }
}
