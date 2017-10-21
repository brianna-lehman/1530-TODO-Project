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
  private boolean isDouble = false;
  // is this card a special card
  private boolean isSpecial = false;

  // dimensions
  static final int CARD_WIDTH = 160;
  static final int CARD_HEIGHT = 110;

  public Card(Color _color, boolean _isDouble, boolean _isSpecial) {
    this.color = _color;
    this.isDouble = _isDouble;
    this.isSpecial = _isSpecial;
    setBackground(Game.CL_WHITE);

    // add colored squares to the card
    add(new ColoredSquare(this.color));
    if (isDouble) {
      add(new ColoredSquare(color));
    }
  }

  public Card(Color _color) {
    this.color = _color;
    setBackground(Game.CL_WHITE);
    add(new ColoredSquare(this.color));
  }

  // public getter methods
  public String getColor() {
    return this.color.toString();
  }

  public boolean isDouble() {
    return this.isDouble;
  }

  public boolean isSpecial() {
    return this.isSpecial;
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
