package com.example.wear.timer;

import org.junit.Before;
import org.junit.Test;

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
    public void testThatItKnowsHowToBuildAFormattedTime() {
        Time time = Time.now(TimeZone.getDefault());
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        TimeViewModel timeViewModel = new TimeViewModel(time);
        assertEquals(hour + " : " + minute + " : " + second, timeViewModel.formattedTime());
    }
}
