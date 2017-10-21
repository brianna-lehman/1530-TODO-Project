/*
 * This class will extend JPanel to create a panel in Game that will display
 * messages to the users of the game, e.g. "It is Player 1's turn!"
 */

import javax.swing.*;
import java.awt.*;

public class MessagePanel extends JPanel {

  // message to be displayed
  private JLabel userMessage;

  // CSS styles for the messages that will be displayed to the user
  private static final String MESSAGE_STYLES = "margin: 10px; text-align: center;"
      + "color: white;";

  // initial message to display
  private String initialMessage = formatText("Hello, welcome to the World of Sweets!",
      Game.WINDOW_WIDTH / 2, MESSAGE_STYLES);

  public MessagePanel() {
    setBackground(Game.CL_PINK);

    // message to be initially displayed to user
    userMessage = new JLabel();
    userMessage.setFont(new Font("Courier", Font.PLAIN, 24));
    userMessage.setText(initialMessage);
    add(userMessage);
  }

  /**
   * This method takes some text and wraps it in html elements and styles so
   * that it will wrap properly when placed inside a JPanel and have the given
   * CSS styles applied to it.
   * @param text the text to be wrapped
   * @param maxWidth the max width of the JPanel for the text to wrap in
   * @param styles CSS styles string of the format "name1: style; name2: style;"
   * @return the html-wrapped text
   */
  private String formatText(String text, int maxWidth, String styles) {
    String formatStr = "<html><div style='%s' WIDTH=%d>%s</div><html>";
    return String.format(formatStr, styles, maxWidth, text);
  }
}
