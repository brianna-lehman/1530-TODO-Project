/**
 * This class represents a single card in the game.
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class Card extends JPanel {

  // color of this card
  private Color color;

  // is this card a single or double color card
  private boolean isMultiple = false;
  // is this card a special card
  private boolean isSpecial = false;

  // dimensions
  static final int CARD_WIDTH = 160;
  static final int CARD_HEIGHT = 110;

  /** constructor for cards with one colored square
   * @param _color the color of the square on the card
   */
  public Card(Color _color) {
    this.color = _color;
    setBackground(Game.CL_WHITE);
    add(new ColoredSquare(this.color));
  }

  /** constructor for cards with multiple colored squares
   * @param _color the color of the squares on the card
   * @param numOfSquares number of squares on the card
   */
  public Card(Color _color, int numOfSquares) {
    if (numOfSquares == 1) {
      new Card(_color);
    }
    else {
      this.color = _color;
      this.isMultiple = true;
      setBackground(Game.CL_WHITE);

      for (int i = 1; i <= numOfSquares; i++) {
        add(new ColoredSquare(this.color));
      }
    }
  }

  /** constructor for special cards which present a message to the user
   * @param specialMessage the texts that will appear on the card
   */
  public Card(String specialMessage) {
    this.isSpecial = true;
    setBackground(Game.CL_WHITE);

    JLabel messageLabel = new JLabel();
    messageLabel.setFont(new Font("Courier", Font.PLAIN, 24));
    messageLabel.setText(formatText(specialMessage, CARD_WIDTH));

    add(messageLabel);
  }

  // public getter methods
  public String getColor() {
    return this.color.toString();
  }

  public boolean isMultiple() {
    return this.isMultiple;
  }

  public boolean isSpecial() {
    return this.isSpecial;
  }

  private String formatText(String text, int maxWidth) {
    String formatStr = "<html><div WIDTH=%d>%s</div><html>";
    return String.format(formatStr, maxWidth, text);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(CARD_WIDTH, CARD_HEIGHT);
  }

  /**
   * This class is just a single colored square created using a JPanel which
   * can be added to Cards to show their color.
   */
  private class ColoredSquare extends JPanel {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;

    public ColoredSquare(Color color) {
      // color of g.fillRect is determined by the foreground color
      setForeground(color);
      setBackground(Game.CL_WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.fillRect(0, 20, WIDTH, HEIGHT);
    }

    @Override
    public Dimension getPreferredSize() {
      // override preferred size so window.pack() works
      return new Dimension(WIDTH, HEIGHT + 2 * 20);
    }
  }
}
