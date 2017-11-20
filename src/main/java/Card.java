/**
 * This class represents a single card in the game.
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class Card extends JPanel {

  // color of this card
  Color color;

  // is this card a single or double color card
  boolean isMultiple = false;

  // possible special card types
  enum CardType {
    NORMAL,
    SKIP,
    SPECIAL1,
    SPECIAL2,
    SPECIAL3,
    SPECIAL4,
    SPECIAL0
  }
  // is this card a special card
  boolean isSpecial = false;
  CardType cardType = CardType.NORMAL;

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

  /**
   * Constructor for special cards which present a message to the user
   * @param _color the background color of the card
   * @param type the card type
   */
  public Card(CardType type) {
    this.isSpecial = (type != CardType.NORMAL);
    this.cardType = type;

    // initally white background
    setBackground(Game.CL_WHITE);

    JLabel messageLabel = new JLabel();
    messageLabel.setFont(new Font("Courier", Font.PLAIN, 24));
    messageLabel.setText(formatText(getSpecialMessage(type), CARD_WIDTH));
    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    messageLabel.setVerticalAlignment(SwingConstants.CENTER);

    add(messageLabel);
  }

  /**
   * This method is for creating a card from a string representation of a
   * card which comes from a game saved to a file.
   * @param cardCode string representation of a card, expected to be of the
   * form <numSquares>:<color/type>, i.e. "1:R" is a single red square card,
   * "2:B" is a double blue square card, and "1:S" is a skip card.
   */
  public Card(String cardCode) {
    String[] parts = cardCode.split(":");
    if (parts[1].substring(0,1).equals("S")) {
      this.isSpecial = true;
      String cardIdentifier = parts[1];
      if(cardIdentifier.equals("S")) {
        this.cardType =  CardType.SKIP;
      }
      else{
        switch(cardIdentifier) {
          case "S0": 
            this.cardType = CardType.SPECIAL0;
          case "S1": 
            this.cardType = CardType.SPECIAL1;
          case "S2": 
            this.cardType = CardType.SPECIAL2;
          case "S3": 
            this.cardType = CardType.SPECIAL3;
          case "S4": 
            this.cardType = CardType.SPECIAL4;
        }
      }

      // initally white background
      setBackground(Game.CL_WHITE);

      JLabel messageLabel = new JLabel();
      messageLabel.setFont(new Font("Courier", Font.PLAIN, 24));
      messageLabel.setText(formatText(getSpecialMessage(this.cardType), CARD_WIDTH));
      messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
      messageLabel.setVerticalAlignment(SwingConstants.CENTER);

      add(messageLabel);
    } else {
      Color cardColor = Game.CL_WHITE;
      switch (parts[1]) {
        case "R": {
          cardColor = Game.CL_RED;
          break;
        }
        case "B": {
          cardColor = Game.CL_BLUE;
          break;
        }
        case "G": {
          cardColor = Game.CL_GREEN;
          break;
        }
        case "Y": {
          cardColor = Game.CL_YELLOW;
          break;
        }
        case "O": {
          cardColor = Game.CL_ORANGE;
          break;
        }
        default: {
          break;
        }
      }

      this.color = cardColor;
      setBackground(Game.CL_WHITE);

      int numSquares = Integer.parseInt(parts[0]);
      if (numSquares > 1) {
        this.isMultiple = true;
      }
      for (int i = 1; i <= numSquares; i++) {
        add(new ColoredSquare(this.color));
      }
    }
  }

  /**
   * This method is used to create a string representation of a card which will
   * be used to save card decks to file when saving a game.
   * @return the string representation of a card of the form <numSquares>:<color/type>,
   * i.e. "1:R" is a single red square card, "2:B" is a double blue square card,
   * and "1:S" is a skip card.
   */
  public String toString() {
    StringBuilder codeBuilder = new StringBuilder();
    if (isMultiple) {
      codeBuilder.append("2:");
    } else {
      codeBuilder.append("1:");
    }

    if (cardType == CardType.NORMAL) {
      if (color == Game.CL_RED) {
        codeBuilder.append("R");
      } else if (color == Game.CL_BLUE) {
        codeBuilder.append("B");
      } else if (color == Game.CL_GREEN) {
        codeBuilder.append("G");
      } else if (color == Game.CL_YELLOW) {
        codeBuilder.append("Y");
      } else if (color == Game.CL_ORANGE) {
        codeBuilder.append("O");
      }
    } else if (cardType == CardType.SKIP) {
      codeBuilder.append("S");
    } else if (cardType == CardType.SPECIAL0) {
      codeBuilder.append("S0");
    } else if (cardType == CardType.SPECIAL1) {
      codeBuilder.append("S1");
    } else if (cardType == CardType.SPECIAL2) {
      codeBuilder.append("S2");
    } else if (cardType == CardType.SPECIAL3) {
      codeBuilder.append("S3");
    } else if (cardType == CardType.SPECIAL4) {
      codeBuilder.append("S4");
    }

    return codeBuilder.toString();
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

  public CardType getCardType() {
    return this.cardType;
  }

  /**
   * This method returns the special message for given special card type.
   * @param cardType - the type of card
   * @return the special message for this card type
   */
  private String getSpecialMessage(CardType cardType) {
    switch (cardType) {
      case SKIP:
        return "Skip turn";
      case SPECIAL0:
        return "Move to Ice Cream Land";
      case SPECIAL1:
        return "Move to Chocolate River";
      case SPECIAL2:
        return "Move to Licorice Jungle";
      case SPECIAL3:
        return "Move to Rock Candy Caverns";
      case SPECIAL4:
        return "Move to Hershey Park";
      default:
        return "";
    }
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
