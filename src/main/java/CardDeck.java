/**
 * This class represents a deck of World of Sweets cards.
 */

import java.util.*;

public class CardDeck {

  // deck of cards
  private Stack<Card> deck = new Stack<Card>();

  public CardDeck() {
    // fill the deck with cards
    fill();
    // shuffle the deck
    shuffle();
  }

  // method to return card on the top of the deck
  public Card getNextCard() {
    Card nextCard = (Card) deck.pop();
    if (deck.empty()) {
      fill();
      shuffle();
    }

    return nextCard;
  }

  private void fill() {
    // add the singles
    for (int i = 0; i < 10; i++) {
      Card redCard = new Card(Game.CL_RED);
      Card yellowCard = new Card(Game.CL_YELLOW);
      Card blueCard = new Card(Game.CL_BLUE);
      Card greenCard = new Card(Game.CL_GREEN);
      Card orangeCard = new Card(Game.CL_ORANGE);

      deck.push(redCard);
      deck.push(yellowCard);
      deck.push(blueCard);
      deck.push(greenCard);
      deck.push(orangeCard);
    }

    // add the doubles
    for (int i = 0; i < 2; i++) {
      Card redDouble = new Card(Game.CL_RED, 2);
      Card yellowDouble = new Card(Game.CL_YELLOW, 2);
      Card blueDouble = new Card(Game.CL_BLUE, 2);
      Card greenDouble = new Card(Game.CL_GREEN, 2);
      Card orangeDouble = new Card(Game.CL_ORANGE, 2);

      deck.push(redDouble);
      deck.push(yellowDouble);
      deck.push(blueDouble);
      deck.push(greenDouble);
      deck.push(orangeDouble);
    }

    // add special card "Skip Turn"
    for (int i = 0; i < 5; i++) {
      Card skip = new Card(Game.CL_PURPLE, "Skip Turn");
      deck.push(skip);
    }

    // add special card "Move to center square"
    for (int i = 0; i < 3; i++) {
      Card middle = new Card(Game.CL_PINK, "Move to middle square");
      deck.push(middle);
    }
  }

  private void shuffle() {
    Collections.shuffle(deck);
  }

  // returns size of deck for testing purposes
  public int deckSize() {
    return deck.size();
  }
}
