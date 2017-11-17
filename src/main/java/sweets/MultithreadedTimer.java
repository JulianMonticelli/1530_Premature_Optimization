/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */
package sweets;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Julian
 */
public class MultithreadedTimer {

    Timer timer;
    Thread timerThread;
    
    public void unpauseThread() {
        timer.setGamePaused(false);
    }
    
    public void pauseThread() {
        timer.setGamePaused(true);
    }
    
    public void killThread() {
        timer.killTimer();
    }
    
    // Creates a new timer object and starts the timer.
    // WARNING: This is used only ONCE per game.
    public void startThread() {
        timer = new Timer();
        timerThread = new Thread(timer);
        timerThread.start();
    }
    
    public String getTimerString() {
        return timer.getTimerString();
    }

    
    // Timer Subclass
    public class Timer implements Runnable {
        
        public Timer() {
            // Nothing to construct here:)
        }
        
        private static final int nanosPerSec = 1000000000;
        
        // Create a volatile boolean, because another thread may 
        private volatile boolean gameIsPaused = false;
        private volatile boolean killTimer = false;
        
        
        
        private int timeInSeconds = 0;
        
        
        private long previousTime = System.nanoTime();
        
        
        // The nanosecond difference in time from one point to another
        private long timeDiff;
        
        // The amount of time into an elapsing second when we paused the timer
        private long extraTime;
        
        public void setGamePaused(boolean set) {
            gameIsPaused = set;
        }
        
        public void killTimer() {
            killTimer = true;
        }
        
        public String getTimerString() {
            int minutes = timeInSeconds/60;
            int seconds = timeInSeconds%60;
            return String.format("%02d:%02d", minutes, seconds);
        }
        
        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                // Check whether or not we should kill the timer
                if (killTimer) {
                    return;
                }
                
                // If the game is not paused, store time difference
                if (!gameIsPaused)
                {
                    timeDiff = (System.nanoTime() - previousTime) + extraTime;
                }
                
                // If the game is paused, wait for it to unpause and 
                // store the current time elapsed for a precise timer
                else
                {
                    
                    extraTime += System.nanoTime() - previousTime;
                    System.out.println("Detected game is paused!");
                    while (gameIsPaused) {
                        try {
                            Thread.sleep(50L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Detected game unpaused!");
                    previousTime = System.nanoTime();
                }
                
                if (timeDiff >= nanosPerSec) {
                    previousTime = System.nanoTime();
                    timeInSeconds++;
                    extraTime = timeDiff - nanosPerSec; // Reset pause time
                    System.out.println(getTimerString());
                }
                
                // THIS is premature optimization vvvv
                // I rolled out this delay before I even pushed my branch.
                // CPU usage on a 4.2GHz processor went from ~19% to <4%
                try {
                    Thread.sleep(5L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    
}
