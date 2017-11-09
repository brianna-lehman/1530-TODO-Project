import static org.junit.Assert.*;
import org.junit.Test;

public class TimerTest {
  @SuppressWarnings("unchecked")
  final int INITIAL_TIME = 0;

  @Test
  public void testSetTime() {
    Timer timer = new Timer();
    timer.setTime(10);
    assertEquals(timer.getTime(), 10);
    timer.setTime(100);
    assertEquals(timer.getTime(), 100);
    timer.setTime(0);
    assertEquals(timer.getTime(), 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetTimeException() {
    Timer timer = new Timer();
    timer.setTime(-1);
  }

  @Test
  public void testFormatTime() {
    Timer timer = new Timer();
    timer.setTime(120);
    assertEquals(timer.getTimeString(), "02:00");
    timer.setTime(0);
    assertEquals(timer.getTimeString(), "00:00");
    timer.setTime(90);
    assertEquals(timer.getTimeString(), "01:30");
  }
}
