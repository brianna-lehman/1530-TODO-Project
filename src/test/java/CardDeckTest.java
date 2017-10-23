import static org.junit.Assert.*;
import org.junit.Test;

public class CardDeckTest {
  @SuppressWarnings("unchecked")

  @Test
  public void testCardDeckConstructor() {
    CardDeck deck = new CardDeck();
    int numOfCards = 60;
    assertEquals(deck.deckSize(), numOfCards);
  }

  // when a card is taken off the deck the deck has one less card
  @Test
  public void testGetNextCardOneCard() {
    CardDeck deck = new CardDeck();
    deck.getNextCard();
    assertEquals(deck.deckSize(), 60-1);
  }

  // when n cards are taken off the deck the deck has n less cards
  @Test
  public void testGetNextCardManyCards() {
    CardDeck deck = new CardDeck();
    for (int i = 1; i <= 22; i++) {
        deck.getNextCard();
    }
    assertEquals(deck.deckSize(), 60-22);
  }

  // when all the cards are taken off the deck the deck is refilled and reshuffled
  @Test
  public void testGetNextCardAllCards() {
    CardDeck deck = new CardDeck();
    for (int i = 1; i <= 60; i++) {
        deck.getNextCard();
    }
    assertEquals(deck.deckSize(), 60);
  }
}