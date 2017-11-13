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
		// one wrong character in otherwise valid name
		String filename = "invali.d";
		assertEquals(menu.confirmFilenamePopup(filename), false);
	}

	@Test
	public void testFilenameIncorrectChars() {
		Menu menu = new Menu();
		// multipe wrong characters in otherwise valid name
		String filename = "in&va*d";
		assertEquals(menu.confirmFilenamePopup(filename), false);
	}

	@Test
	public void testFilenameChar() {
		Menu menu = new Menu();
		// one wrong character
		String filename = "&";
		assertEquals(menu.confirmFilenamePopup(filename), false);
	}

	@Test
	public void testFilenameChars() {
		Menu menu = new Menu();
		// multiple wrong characters
		String filename = "\\\\:\\:\\{}}";
		assertEquals(menu.confirmFilenamePopup(filename), false);
	}

	@Test
	public void testFilenameSpaces() {
		Menu menu = new Menu();
		// spaces
		String filename = "also invalid";
		assertEquals(menu.confirmFilenamePopup(filename), false);
	}

	@Test
	public void testFilenameEmpty() {
		Menu menu = new Menu();
		// empty
		String filename = "";
		assertEquals(menu.confirmFilenamePopup(filename), false);
	}
}