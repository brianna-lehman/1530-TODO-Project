/**
 * This class represents a timer that keeps track of and displays the current
 * game time to the user.
 */

import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Timer extends JLabel {

  // number of seconds in the game so far
  private int timeInSeconds;

  // thread that will update this timer
  private Thread timerThread;

  public Timer(){
    // initialize time to zero minutes and seconds
    timeInSeconds = 0;
    setText("Game Clock: " + formatTime(timeInSeconds));

    timerThread = new Thread(() -> {
      while (true) {
        try {
          // sleep for a second
          Thread.sleep(1000);

          // update the time
          setText("Game Clock: " + formatTime(++timeInSeconds));
        }
        catch (Exception e) {
          System.err.println("Timer Thread Exception: " + e.getMessage());
        }
      }
    });
  }

  // Starts the timer
  public void start() {
    if (!timerThread.isAlive()) {
      timerThread.start();
    }
  }

  // Gets the current time in seconds for this timer.
  public int getTime() {
    return timeInSeconds;
  }

  // Set time in seconds
  public void setTime(int seconds) {
    if (seconds < 0) {
      throw new IllegalArgumentException("Time must be greater than zero.");
    }

    timeInSeconds = seconds;
  }

  // Get time string
  public String getTimeString() {
    return formatTime(timeInSeconds);
  }

  /**
   * This message converts a number of seconds into a readable format in
   * minutes and seconds for the user.
   * @param totalSeconds the number of seconds to convert
   * @return a readable time string in minutes and seconds
   */
  private String formatTime(int totalSeconds) {
    int seconds = totalSeconds % 60;
    int minutes = totalSeconds / 60;

    return String.format("%02d:%02d", minutes, seconds);
  }
}
