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

  public Card(Color _color, boolean _isDouble, boolean _isSpecial) {
    this.color = _color;
    this.isDouble = _isDouble;
    this.isSpecial = _isSpecial;

    setBorder(new EtchedBorder(EtchedBorder.RAISED));

    // add colored squares to the card
    add(new ColoredSquare(this.color));
    if (isDouble) {
      add(new ColoredSquare(color));
    }
  }

  public Card(Color _color) {
    this.color = _color;
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
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    @Override
    public Dimension getPreferredSize() {
      // override preferred size so window.pack() works
      return new Dimension(WIDTH, HEIGHT);
    }
  }
}
