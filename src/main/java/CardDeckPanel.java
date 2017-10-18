/*
 * This class extends JPanel and will be a panel containing two smaller panels
 * which will represent the card and discard piles for the game.
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class CardDeckPanel extends JPanel {

  // dimensions of the card decks
  private static final int DECK_WIDTH = 100;
  private static final int DECK_HEIGHT = 160;

  public CardDeckPanel() {
    // add area for decks of cards
    setLayout(new GridLayout(1, 2, 20, 0));
    setBackground(Game.CL_WHITE);
    setBorder(new EmptyBorder(10, 80, 10, 80));

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.fill = GridBagConstraints.BOTH;

    // card pile
    Card cardPile = Game.deck.getNextCard();

    // discard pile
    Card discardPile = Game.deck.getNextCard();

    // add card and discard piles to this panel
    add(cardPile);
    add(discardPile);
  }
}
