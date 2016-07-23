package com.example.android.sunshine.app.watch.timer;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TimerTest {
    @Test
    public void testThatTimerShouldNotTickIfItShouldNotBeRunning() {
        TimeTicker timeTicker = mock(TimeTicker.class);
        Timer timer = Timer.getInstance(500, timeTicker);
        when(timeTicker.shouldTimerBeRunning()).thenReturn(false);
        timer.tick();
        verify(timeTicker, never()).onTimeTick();
    }

    @Test
    public void testThatTimerShouldNotTickIfItShouldBeRunning() {
        TimeTicker timeTicker = mock(TimeTicker.class);
        Timer timer = Timer.getInstance(500, timeTicker);
        when(timeTicker.shouldTimerBeRunning()).thenReturn(true);
        timer.tick();
        verify(timeTicker).onTimeTick();
    }
}