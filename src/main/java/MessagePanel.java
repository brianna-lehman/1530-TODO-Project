/*
 * This class will extend JPanel to create a panel in Game that will display
 * messages to the users of the game, e.g. "It is Player 1's turn!"
 */

import javax.swing.*;
import java.awt.*;

public class MessagePanel extends JPanel {

  // game timer
  private Timer timer;
  // turn counter
  private JLabel turnCounter;
  // turn indicator
  private JLabel turnIndicator;
  // message to be displayed
  private JLabel userMessage;

  // the text of the message, unformatted
  private String message;

  // CSS styles for the messages that will be displayed to the user
  private static final String MESSAGE_STYLES = "margin: 10px; text-align: center;"
      + "color: white;";

  // initial message to display
  private String initialMessage = formatText("Hello, welcome to the World of Sweets!",
      Game.WINDOW_WIDTH / 2, MESSAGE_STYLES);

  public MessagePanel() {
    setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
    setBackground(Game.CL_PINK);

    timer = new Timer();
    timer.setFont(new Font("Courier", Font.PLAIN, 16));
    timer.setForeground(Game.CL_WHITE);
    add(timer);

    turnCounter = new JLabel();
    turnCounter.setFont(new Font("Courier", Font.PLAIN, 16));
    turnCounter.setText(formatText("Turn #" + Game.numTurns, Game.WINDOW_WIDTH / 2, MESSAGE_STYLES));
    add(turnCounter);

    turnIndicator = new JLabel();
    turnIndicator.setFont(new Font("Courier", Font.PLAIN, 24));
    turnIndicator.setText(formatText(Game.playerNames[0] + "'s turn", Game.WINDOW_WIDTH / 2, MESSAGE_STYLES));
    add(turnIndicator);

    // message to be initially displayed to user
    userMessage = new JLabel();
    userMessage.setFont(new Font("Courier", Font.PLAIN, 24));
    userMessage.setText(initialMessage);
    add(userMessage);

    // save unformatted message String
    message = new String("Hello, welcome to the World of Sweets!");
  }

  public void setCurrentTurn(int turn) {
    turnIndicator.setText(formatText(Game.playerNames[turn] + "'s turn", Game.WINDOW_WIDTH / 2, MESSAGE_STYLES));
  }

  public String getMessage() {
    return new String(message);
  }

  public void setMessage(String newMessage) {
    userMessage.setText(formatText(newMessage, Game.WINDOW_WIDTH / 2, MESSAGE_STYLES));
    message = new String(newMessage);
  }

  public void startTimer() {
    timer.start();
  }

  public Timer getTimer() {
    return timer;
  }

  public void incrementTurn()
  {
    Game.numTurns++;
    turnCounter.setText(formatText("Turn #" + Game.numTurns, Game.WINDOW_WIDTH / 2, MESSAGE_STYLES));
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
