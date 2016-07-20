package com.example.wear.timer;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
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
    public void testThatTimeViewModelKnowsItsAssociatedHour() {
        Time time = Time.now(TimeZone.getDefault());
        TimeViewModel timeViewModel = new TimeViewModel(time);
        int expectedHour = calendar.get(Calendar.HOUR);
        assertEquals(String.valueOf(expectedHour), timeViewModel.hour());
    }

    @Test
    public void testThatTimeViewModelKnowsItsAssociatedMinute() {
        Time time = Time.now(TimeZone.getDefault());
        TimeViewModel timeViewModel = new TimeViewModel(time);
        int expectedMinutes = calendar.get(Calendar.MINUTE);
        assertEquals(String.valueOf(expectedMinutes), timeViewModel.minutes());
    }

    @Test
    public void testThatTimeViewModelKnowsItsAssociatedSecond() {
        Time time = Time.now(TimeZone.getDefault());
        TimeViewModel timeViewModel = new TimeViewModel(time);
        int expectedSeconds = calendar.get(Calendar.SECOND);
        assertEquals(String.valueOf(expectedSeconds), timeViewModel.seconds());
    }
}
