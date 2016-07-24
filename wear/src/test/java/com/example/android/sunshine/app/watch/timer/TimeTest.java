package com.example.android.sunshine.app.watch.timer;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TimeTest {
    public static final long FIRST_TIME_IN_MILLIS = 1469317645888l;
    public static final long SECOND_TIME_IN_MILLIS = 2469317645888l;

    @Test
    public void testShouldNotBeEqualToNothing() {
        Time time = new Time(FIRST_TIME_IN_MILLIS);
        assertFalse(time.equals(null));
    }

    @Test
    public void testShouldBeEqualToItself() {
        Time time = new Time(FIRST_TIME_IN_MILLIS);
        assertTrue(time.equals(time));
    }

    @Test
    public void testShouldBeEqualToOtherTimeWithSameMilliseconds() {
        Time firstTime = new Time(FIRST_TIME_IN_MILLIS);
        Time secondTime = new Time(FIRST_TIME_IN_MILLIS);
        assertTrue(firstTime.equals(secondTime));
    }

    @Test
    public void testShouldNotBeEqualToOtherTimeWithDifferentMilliseconds() {
        Time firstTime = new Time(FIRST_TIME_IN_MILLIS);
        Time secondTime = new Time(SECOND_TIME_IN_MILLIS);
        assertFalse(firstTime.equals(secondTime));
    }

    @Test
    public void testShouldNotBeEqualToAnythingOtherThanTime() {
        Time time = new Time(FIRST_TIME_IN_MILLIS);
        assertFalse(time.equals(new Object()));
    }

    @Test
    public void testShouldHaveSameHashCodeIfSameMilliseconds() {
        Time firstTime = new Time(FIRST_TIME_IN_MILLIS);
        Time secondTime = new Time(FIRST_TIME_IN_MILLIS);
        assertEquals(firstTime.hashCode(), secondTime.hashCode());
    }

    @Test
    public void testShouldKnowItsSerializedForm() {
        Time firstTime = new Time(FIRST_TIME_IN_MILLIS);
        String expectedString = "Time{timeInMillis=1469317645888}";
        assertEquals(expectedString, firstTime.toString());
    }
}
