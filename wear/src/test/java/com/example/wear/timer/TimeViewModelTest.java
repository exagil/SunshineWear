package com.example.wear.timer;

import org.junit.Before;
import org.junit.Ignore;
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
    @Ignore
    public void testThatItKnowsHowToBuildAFormattedTime() {
        Time time = Time.now(TimeZone.getDefault());
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        TimeViewModel timeViewModel = new TimeViewModel(time);
        assertEquals(hour + " : " + minute + " : " + second, timeViewModel.formattedTime());
    }

    @Test
    public void testThatItKnowsHowToPrependZeroToHourIfHourIsLessThanTen() {
        Time time = Mockito.mock(Time.class);
        Mockito.when(time.hour()).thenReturn(4);
        Mockito.when(time.minutes()).thenReturn(37);
        Mockito.when(time.seconds()).thenReturn(24);
        TimeViewModel timeViewModel = new TimeViewModel(time);
        String expectedFormattedTime = "04 : 37 : 24";
        assertEquals(expectedFormattedTime, timeViewModel.formattedTime());
    }

    @Test
    public void testThatItKnowsNotToPrependZeroToHourIfHourIsEqualToTen() {
        Time time = Mockito.mock(Time.class);
        Mockito.when(time.hour()).thenReturn(10);
        Mockito.when(time.minutes()).thenReturn(37);
        Mockito.when(time.seconds()).thenReturn(24);
        TimeViewModel timeViewModel = new TimeViewModel(time);
        String expectedFormattedTime = "10 : 37 : 24";
        assertEquals(expectedFormattedTime, timeViewModel.formattedTime());
    }

    @Test
    public void testThatItKnowsNotToPrependZeroToHourIfHourIsMoreThanTen() {
        Time time = Mockito.mock(Time.class);
        Mockito.when(time.hour()).thenReturn(11);
        Mockito.when(time.minutes()).thenReturn(37);
        Mockito.when(time.seconds()).thenReturn(24);
        TimeViewModel timeViewModel = new TimeViewModel(time);
        String expectedFormattedTime = "11 : 37 : 24";
        assertEquals(expectedFormattedTime, timeViewModel.formattedTime());
    }

    @Test
    public void testThatItKnowsHowToPrependZeroToMinuteIfMinuteIsLessThanTen() {
        Time time = Mockito.mock(Time.class);
        Mockito.when(time.hour()).thenReturn(20);
        Mockito.when(time.minutes()).thenReturn(7);
        Mockito.when(time.seconds()).thenReturn(24);
        TimeViewModel timeViewModel = new TimeViewModel(time);
        String expectedFormattedTime = "20 : 07 : 24";
        assertEquals(expectedFormattedTime, timeViewModel.formattedTime());
    }

    @Test
    public void testThatItKnowsNotToPrependZeroToMinuteIfMinuteIsEqualToTen() {
        Time time = Mockito.mock(Time.class);
        Mockito.when(time.hour()).thenReturn(10);
        Mockito.when(time.minutes()).thenReturn(10);
        Mockito.when(time.seconds()).thenReturn(24);
        TimeViewModel timeViewModel = new TimeViewModel(time);
        String expectedFormattedTime = "10 : 10 : 24";
        assertEquals(expectedFormattedTime, timeViewModel.formattedTime());
    }

    @Test
    public void testThatItKnowsNotToPrependZeroToMinuteIfMinuteIsMoreThanTen() {
        Time time = Mockito.mock(Time.class);
        Mockito.when(time.hour()).thenReturn(11);
        Mockito.when(time.minutes()).thenReturn(37);
        Mockito.when(time.seconds()).thenReturn(24);
        TimeViewModel timeViewModel = new TimeViewModel(time);
        String expectedFormattedTime = "11 : 37 : 24";
        assertEquals(expectedFormattedTime, timeViewModel.formattedTime());
    }
}
