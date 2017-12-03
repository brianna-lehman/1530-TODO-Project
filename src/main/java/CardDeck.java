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
    if (deck.empty()) {
      fill();
      shuffle();
    }
    Card nextCard = (Card) deck.pop();

    return nextCard;
  }

  /**
   * given the current player's position
   * find the card in the remaining deck that will move them closest to the start
   * @param currentSquare the index of the square where the token we're cheating against is
   * @return the worst card for the player in the currentSquare to move to
   */
  public Card getWorstCard(int currentSquare) {
    if (deck.empty()) {
      fill();
      shuffle();
    }

    int squareClosestToStart = -1;
    Game game = Game.getInstance();
    Board board = game.getBoard();
    Card worstCard = null;
    Stack<Card> temp = new Stack<Card>();

    while (!deck.empty()) {
      Card currentCard = deck.pop();
      int nextSquareIndex = board.nextSquare(currentSquare, currentCard);
      if (squareClosestToStart == -1 || nextSquareIndex < squareClosestToStart) {
        squareClosestToStart = nextSquareIndex;
        worstCard = currentCard;
      }
      temp.push(currentCard);
    }

    // puts all the remaining cards back in the deck in the order they were removed
    // except for the card that's being pulled and given to the player
    while (!temp.empty()) {
      Card topCard = temp.pop();
      if (topCard.getId() != worstCard.getId()) {
        deck.push(topCard);
      }
    }

    return worstCard;
  }

  private void fill() {
    int id = 0;
    // add the singles
    for (int i = 0; i < 10; i++) {
      Card redCard = new Card(Game.CL_RED, id++);
      Card yellowCard = new Card(Game.CL_YELLOW, id++);
      Card blueCard = new Card(Game.CL_BLUE, id++);
      Card greenCard = new Card(Game.CL_GREEN, id++);
      Card orangeCard = new Card(Game.CL_ORANGE, id++);

      deck.push(redCard);
      deck.push(yellowCard);
      deck.push(blueCard);
      deck.push(greenCard);
      deck.push(orangeCard);
    }

    // add the doubles
    for (int i = 0; i < 2; i++) {
      Card redDouble = new Card(Game.CL_RED, 2, id++);
      Card yellowDouble = new Card(Game.CL_YELLOW, 2, id++);
      Card blueDouble = new Card(Game.CL_BLUE, 2, id++);
      Card greenDouble = new Card(Game.CL_GREEN, 2, id++);
      Card orangeDouble = new Card(Game.CL_ORANGE, 2, id++);

      deck.push(redDouble);
      deck.push(yellowDouble);
      deck.push(blueDouble);
      deck.push(greenDouble);
      deck.push(orangeDouble);
    }

    // add special card "Skip Turn"
    for (int i = 0; i < 5; i++) {
      Card skip = new Card(Card.CardType.SKIP, id++);
      deck.push(skip);
    }

    // add "Move to [special square]"
    deck.push(new Card(Card.CardType.SPECIAL0, id++));
    deck.push(new Card(Card.CardType.SPECIAL1, id++));
    deck.push(new Card(Card.CardType.SPECIAL2, id++));
    deck.push(new Card(Card.CardType.SPECIAL3, id++));
    deck.push(new Card(Card.CardType.SPECIAL4, id++));
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
