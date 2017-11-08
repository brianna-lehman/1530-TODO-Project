/*
 * This class extends JPanel and will be a panel containing two smaller panels
 * which will represent the card and discard piles for the game.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class CardDeckPanel extends JPanel {

  // this will hold cards
  private JPanel cardPile = new JPanel();

  // card that will be shown
  Card currentCard = null;

  public CardDeckPanel() {
    GridBagConstraints constraints = new GridBagConstraints();
    setLayout(new GridBagLayout());

    // add a button used to draw new cards
    JButton drawCardButton = new JButton("Draw a Card");
    ActionListener drawCardListener = new DrawCardListener();
    drawCardButton.addActionListener(drawCardListener);
    add(drawCardButton, constraints);

    // add a space for cards that have been drawn
    cardPile.setPreferredSize(new Dimension(Card.CARD_WIDTH, Card.CARD_HEIGHT));
    constraints.gridy = 1;
    constraints.insets = new Insets(10, 0, 0, 0);
    add(cardPile, constraints);
  }

  /**
   * This method will force the cardPile component to redraw itself. It should
   * be used to refresh this panel when a card is drawn.
   */
  private void refresh() {
    cardPile.revalidate();
    cardPile.repaint();
  }

  /**
   * This ActionListener corresponds to the button used to draw cards from the
   * pile.
   */
  private class DrawCardListener implements ActionListener {

    // to be called each time the drawCardButton is clicked
    public void actionPerformed(ActionEvent e) {
      Game game = Game.getInstance();

      // do not let a user draw multiple cards per turn
      if (game.cardDrawn) {
        return;
      }

      // remove the current card from the screen if there is one there
      if (currentCard != null) {
        cardPile.remove(currentCard);
      }

      // replace the card with the next one from the deck
      currentCard = Game.deck.getNextCard();
      cardPile.add(currentCard);

      // set card drawn to true
      game.cardDrawn = true;

      // refresh the component
      refresh();

      if (currentCard.isSpecial()) {
        game.getMessagePanel().setMessage("Follow the instructions on the card");

        // skip this player's turn if they draw a skip turn card
        if (currentCard.getCardType() == Card.CardType.SKIP) {
          game.nextTurn();
        }
      }
      else if (currentCard.isMultiple()) {
        game.getMessagePanel().setMessage("Move to the second matching colored square");
      }
      else {
        game.getMessagePanel().setMessage("Move to the matching colored square");
      }
    }
  }
}
