import static org.junit.Assert.*;
import org.junit.Test;

public class MenuTest {
  @Test
  public void testFilenameCorrect() {
    Menu menu = new Menu();
    String filename = "a_valid_filename";
    assertEquals(menu.confirmFilenamePopup(filename), true);

    filename = "null";
    assertEquals(menu.confirmFilenamePopup(filename), true);
  }

  @Test
  public void testFilenameIncorrectChar() {
    Menu menu = new Menu();
    String filename = "invali.d";
    assertEquals(menu.confirmFilenamePopup(filename), false);
  }

  @Test
  public void testFilenameIncorrectChars() {
    Menu menu = new Menu();
    String filename = "in&va*d";
    assertEquals(menu.confirmFilenamePopup(filename), false);
  }

  @Test
  public void testFilenameChar() {
    Menu menu = new Menu();
    String filename = "&";
    assertEquals(menu.confirmFilenamePopup(filename), false);
  }

  @Test
  public void testFilenameChars() {
    Menu menu = new Menu();
    String filename = "\\\\:\\:\\{}}";
    assertEquals(menu.confirmFilenamePopup(filename), false);
  }

  @Test
  public void testFilenameSpaces() {
    Menu menu = new Menu();
    String filename = "also invalid";
    assertEquals(menu.confirmFilenamePopup(filename), false);
  }

  @Test
  public void testFilenameEmpty() {
    Menu menu = new Menu();
    String filename = "";
    assertEquals(menu.confirmFilenamePopup(filename), false);
  }
}