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

  /**
   * This constructor is used when loading a card deck state from file for a
   * saved game.
   * @param cardDeckString a string representation of a card deck, expected to be
   * of the form <card1>,<card2>,...<cardN>
   */
  public CardDeck(String cardDeckString) {
    parseCardDeckString(cardDeckString);
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
      Card skip = new Card(Card.CardType.SKIP);
      deck.push(skip);
    }

    // add special card "Move to [special square]"
    String[] specialSquares = {"Ice Cream Land","Chocolate River","Licorice Jungle","Rock Candy Caverns","Hershey Park"};
    for (int i = 0; i < specialSquares.length; i++) {
      Card special_card = null;
      switch(i) {
        case 0:
          special_card = new Card(Card.CardType.SPECIAL0);
        case 1:
          special_card = new Card(Card.CardType.SPECIAL1);
        case 2:
          special_card = new Card(Card.CardType.SPECIAL2);
        case 3:
          special_card = new Card(Card.CardType.SPECIAL3);
        case 4:
          special_card = new Card(Card.CardType.SPECIAL4);
      }
      deck.push(special_card);
    }
  }

  private void shuffle() {
    Collections.shuffle(deck);
  }

  // returns size of deck for testing purposes
  public int deckSize() {
    return deck.size();
  }

  /**
   * This method creates a string representation of a card deck which will be
   * used when saving a game to file.
   * @return the string representation of this deck in the form of:
   * <card1>,<card2>,...<cardN>
   */
  public String toString() {
    List<Card> cardList = new ArrayList<Card>(deck);
    StringBuilder deckString = new StringBuilder();
    for (int i = 0; i < cardList.size(); i++) {
      deckString.append(cardList.get(i).toString());

      if (i != cardList.size() - 1) {
        deckString.append(",");
      }
    }

    return deckString.toString();
  }

  /**
   * This method parses a card deck string representation and adds cards to the
   * deck from the parsed string.
   * @param cardDeckString the string to parse
   */
  public void parseCardDeckString(String cardDeckString) {
    String[] cardCodeStrings = cardDeckString.split(",");
    for (int i = 0; i < cardCodeStrings.length; i++) {
      Card newCard = new Card(cardCodeStrings[i]);
      deck.push(newCard);
    }
  }
}
