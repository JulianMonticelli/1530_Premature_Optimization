import org.junit.*;
import static org.junit.Assert.*;

import sweets.MultithreadedTimer;

public class TimerTest {
    
    
    // Incase anybody hasn't taken 1632, here's how you write a test
    // Also, if you haven't taken 1632, I HIGHLY suggest you do before you graduate.

    /*
    
    vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    >> TL;DR: some tests MAY fail occasionally because of inconsistencies in delays.
    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    
    It is important to recognize that since we have 50ms sleeps in the timer thread
    while we wait for the game to be unpaused, that each sleep function should have an 
    extra 25-50ms. So, if we sleep while a thread is executing post-pause for 20ms, we 
    should actually sleep for 70ms to allow the thread to resume execution.
    
    Chosen below is a 25ms delay time. Sleeping for exactly 25ms is near impossible, it's
    always around 25ms. ALSO, because threads can get caught up at different intervals, it
    is conceivably possible for one or two of these tests to fail (specifically the many
    sleep interval ones).
    
    They SHOULD pass. They do pass normally. I haven't yet witnessed them fail, but I DO
    believe it can be possible for them to fail. I could be wrong, but I wanted to express this
    and make this clear such that if there ever were a failure, this is documented as a reasonable
    explanation why they would fail...
    
    ...assuming that the sleep timer is 50ms in the MultithreadedTimer class and the delayTimer
    is set at 25ms in this class.

    */
    
    MultithreadedTimer mtt;
    
    long delayTime = 25L;
    
    @Before
    public void setUp() {
        mtt = new MultithreadedTimer();
    }
    
    @Test
    public void testTimerIsAccurateToSecondGranularity() {
        mtt.startThread();
        try {
            Thread.sleep(1000L + delayTime); // There's some delay with the threads - an extra 50ms is required
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(mtt.getTimerString(), "00:01");
        mtt.killThread();
    }
    
    @Test
    public void testTimerIsAccurateToSecondGranularityThreeSeconds() {
        mtt.startThread();
        try {
            Thread.sleep(3000L + delayTime); // There's some delay with the threads - an extra 50ms is required
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(mtt.getTimerString(), "00:03");
        mtt.killThread();
    }
    
    @Test
    public void testTimerIsAccurateToSecondGranularityWithPauses() {
        mtt.startThread();
        try {
            Thread.sleep(500L + delayTime); // There's some delay with the threads - an extra 50ms is required
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mtt.pauseThread();
        
        // Create some delay so the thread is guaranteed to catch up
        try {
            Thread.sleep(delayTime); // There's some delay with the threads - an extra 50ms is required
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        mtt.unpauseThread();
        
        try {
            Thread.sleep(500L + delayTime); // There's some delay with the threads - an extra 50ms is required
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        assertEquals(mtt.getTimerString(), "00:01");
        mtt.killThread();
    }
    
    
    @Test
    // Test that the timer works given a couple pauses and ~100ms runtime on the thread inbetween pauses
    public void testTimerIsAccurateToSecondGranularityWithManySmallPauses() {
        mtt.startThread();
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(100L + delayTime); // Some delay is required - on average ~=25ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mtt.pauseThread();

            // Create some delay so the thread is guaranteed to catch up
            try {
                Thread.sleep(delayTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mtt.unpauseThread(); 
        }
        
        assertEquals("00:01", mtt.getTimerString());
        mtt.killThread();
    }
    
    
    
    
    @Test
    // Test that the timer is accurate given there are numerous microscopic pauses inbetween execution (~20ms)
    public void testTimerIsAccurateToSecondGranularityWithManyExtraSmallPauses() { // say that one really fast with peanut butter in ya mouth :)
        mtt.startThread();
        
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(20L + delayTime); // There's some delay with the threads - an extra 50ms is required
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mtt.pauseThread();

            // Create some delay so the thread is guaranteed to catch up
            try {
                Thread.sleep(delayTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mtt.unpauseThread(); 
        }
        
        assertEquals("00:01", mtt.getTimerString());
        mtt.killThread();
    }
    
    @Test
    // Test that the timer is accurate given there are numerous microscopic pauses inbetween execution (~30ms)
    public void testTimerIsAccurateToSecondGranularityWithManyExtraSmallPausesThreeSeconds() { // say that one really fast with peanut butter in ya mouth :)
        mtt.startThread();
        
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(30 + delayTime); // There's some delay with the threads - 1 extra ms = +150ms overall
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mtt.pauseThread();

            // Create some delay so the thread is guaranteed to catch up
            try {
                Thread.sleep(delayTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mtt.unpauseThread(); 
        }
        
        assertEquals("00:03", mtt.getTimerString());
        mtt.killThread();
    }
    
}