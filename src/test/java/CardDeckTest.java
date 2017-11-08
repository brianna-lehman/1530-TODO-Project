import static org.junit.Assert.*;
import org.junit.Test;

public class CardDeckTest {
  @SuppressWarnings("unchecked")
  final int SIZEOFDECK = 68;

  @Test
  public void testCardDeckConstructor() {
    CardDeck deck = new CardDeck();
    assertEquals(deck.deckSize(), SIZEOFDECK);
  }

  // when a card is taken off the deck the deck has one less card
  @Test
  public void testGetNextCardOneCard() {
    CardDeck deck = new CardDeck();
    deck.getNextCard();
    assertEquals(deck.deckSize(), SIZEOFDECK-1);
  }

  // when n cards are taken off the deck the deck has n less cards
  @Test
  public void testGetNextCardManyCards() {
    CardDeck deck = new CardDeck();
    for (int i = 1; i <= 22; i++) {
        deck.getNextCard();
    }
    assertEquals(deck.deckSize(), SIZEOFDECK-22);
  }

  // when all the cards are taken off the deck the deck is refilled and reshuffled
  @Test
  public void testGetNextCardAllCards() {
    CardDeck deck = new CardDeck();
    for (int i = 1; i <= SIZEOFDECK; i++) {
        deck.getNextCard();
    }
    assertEquals(deck.deckSize(), SIZEOFDECK);
  }

  // when the deck is filled it contains 5 skip cards
  @Test
  public void testContainsSkipCards() {
    int numSkipCards = 0;
    CardDeck deck = new CardDeck();
    for (int i = 1; i <= SIZEOFDECK; i++) {
      if(deck.getNextCard().getCardType() == Card.CardType.SKIP) {
        numSkipCards++;
      }
    }
    assertEquals(numSkipCards, 5);
  }

  // when the deck is filled it contains 3 middle cards
  @Test
  public void testContainsMiddleCards() {
    int numMiddleCards = 0;
    CardDeck deck = new CardDeck();
    for (int i = 1; i <= SIZEOFDECK; i++) {
      if(deck.getNextCard().getCardType() == Card.CardType.MIDDLE) {
        numMiddleCards++;
      }
    }
    assertEquals(numMiddleCards, 3);
  }
}
