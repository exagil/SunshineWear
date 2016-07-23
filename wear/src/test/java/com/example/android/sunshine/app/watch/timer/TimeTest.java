package com.example.android.sunshine.app.watch.timer;

import junit.framework.Assert;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TimeTest {
    @Test
    public void testShouldNotBeEqualToNothing() {
        Time time = new Time(1469317645888l);
        assertFalse(time.equals(null));
    }

    @Test
    public void testShouldBeEqualToItself() {
        Time time = new Time(1469317645888l);
        assertTrue(time.equals(time));
    }

    @Test
    public void testShouldBeEqualToOtherTimeWithSameMilliseconds() {
        Time firstTime = new Time(1469317645888l);
        Time secondTime = new Time(1469317645888l);
        assertTrue(firstTime.equals(secondTime));
    }

    @Test
    public void testShouldNotBeEqualToOtherTimeWithDifferentMilliseconds() {
        Time firstTime = new Time(1469317645888l);
        Time secondTime = new Time(2469317645888l);
        assertFalse(firstTime.equals(secondTime));
    }

    @Test
    public void testShouldNotBeEqualToAnythingOtherThanTime() {
        Time time = new Time(1469317645888l);
        assertFalse(time.equals(new Object()));
    }

    @Test
    public void testShouldHaveSameHashCodeIfSameMilliseconds() {
        Time firstTime = new Time(1469317645888l);
        Time secondTime = new Time(1469317645888l);
        Assert.assertEquals(firstTime.hashCode(), secondTime.hashCode());
    }
}
