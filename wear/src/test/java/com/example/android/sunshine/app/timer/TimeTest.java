package com.example.android.sunshine.app.timer;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.TimeZone;

import static junit.framework.Assert.assertEquals;

public class TimeTest {
    private Calendar calendar;

    @Before
    public void setup() {
        long currentTimeMillis = System.currentTimeMillis();
        calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeInMillis(currentTimeMillis);
    }

    @Test
    public void testThatTimeKnowsItsAssociatedHour() {
        Time time = Time.now(TimeZone.getDefault());
        assertEquals(calendar.get(Calendar.HOUR), time.hour());
    }

    @Test
    public void testThatTimeKnowsItsAssociatedMinute() {
        Time time = Time.now(TimeZone.getDefault());
        assertEquals(calendar.get(Calendar.MINUTE), time.minutes(), 2.0f);
    }

    @Test
    public void testThatTimeKnowsItsAssociatedSecond() {
        Time time = Time.now(TimeZone.getDefault());
        assertEquals(calendar.get(Calendar.SECOND), time.seconds(), 2.0f);
    }
}
