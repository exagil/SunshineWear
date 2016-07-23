package com.example.android.sunshine.app.timer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static junit.framework.Assert.assertEquals;

public class TimeViewModelTest {
    private Calendar calendar;

    @Before
    public void setup() {
        long currentTimeMillis = System.currentTimeMillis();
        calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeInMillis(currentTimeMillis);
    }

    @Test
    public void testThatItKnowsHowToBuildAFormattedDate() {
        Time time = Time.now(TimeZone.getDefault());
        Date date = new Date();
        date.setTime(time.currentTimeInMillis);
        String expectedFormattedDate = new SimpleDateFormat("EEE, MMM d yyyy").format(date);
        TimeViewModel timeViewModel = new TimeViewModel(time);
        assertEquals(expectedFormattedDate, timeViewModel.formattedDate());
    }

    @Test
    public void testThatItKnowsHowToPrependZeroToHourIfHourIsLessThanTen() {
        Time time = Mockito.mock(Time.class);
        Mockito.when(time.hour()).thenReturn(4);
        Mockito.when(time.minutes()).thenReturn(37);
        Mockito.when(time.seconds()).thenReturn(24);
        TimeViewModel timeViewModel = new TimeViewModel(time);
        String expectedFormattedTime = "04:37";
        assertEquals(expectedFormattedTime, timeViewModel.formattedTime());
    }

    @Test
    public void testThatItKnowsNotToPrependZeroToHourIfHourIsEqualToTen() {
        Time time = Mockito.mock(Time.class);
        Mockito.when(time.hour()).thenReturn(10);
        Mockito.when(time.minutes()).thenReturn(37);
        Mockito.when(time.seconds()).thenReturn(24);
        TimeViewModel timeViewModel = new TimeViewModel(time);
        String expectedFormattedTime = "10:37";
        assertEquals(expectedFormattedTime, timeViewModel.formattedTime());
    }

    @Test
    public void testThatItKnowsHowToPrependZeroToMinutesIfMinutesIsLessThanTen() {
        Time time = Mockito.mock(Time.class);
        Mockito.when(time.hour()).thenReturn(20);
        Mockito.when(time.minutes()).thenReturn(7);
        Mockito.when(time.seconds()).thenReturn(24);
        TimeViewModel timeViewModel = new TimeViewModel(time);
        String expectedFormattedTime = "20:07";
        assertEquals(expectedFormattedTime, timeViewModel.formattedTime());
    }

    @Test
    public void testThatItKnowsNotToPrependZeroToMinutesIfMinutesIsEqualToTen() {
        Time time = Mockito.mock(Time.class);
        Mockito.when(time.hour()).thenReturn(10);
        Mockito.when(time.minutes()).thenReturn(10);
        Mockito.when(time.seconds()).thenReturn(24);
        TimeViewModel timeViewModel = new TimeViewModel(time);
        String expectedFormattedTime = "10:10";
        assertEquals(expectedFormattedTime, timeViewModel.formattedTime());
    }

    @Test
    public void testThatItKnowsNotToPrependZeroToEitherOfHoursMinutesOrSecondsIfAllAreMoreThanTen() {
        Time time = Mockito.mock(Time.class);
        Mockito.when(time.hour()).thenReturn(11);
        Mockito.when(time.minutes()).thenReturn(37);
        Mockito.when(time.seconds()).thenReturn(24);
        TimeViewModel timeViewModel = new TimeViewModel(time);
        String expectedFormattedTime = "11:37";
        assertEquals(expectedFormattedTime, timeViewModel.formattedTime());
    }
}
