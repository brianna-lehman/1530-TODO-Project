import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.*;

public class CardDeckTest {
  @SuppressWarnings("unchecked")

  @Mock
  Card mockCard = mock(Card.class);

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(mockCard);
  }

  @Test
  public void testCardDeckConstructor() {
    CardDeck deck = new CardDeck();
    int numOfCards = 60;
    assertEquals(deck.deckSize(), numOfCards);
  }

  // when a card is taken off the deck
  // the deck has one less card
  @Test
  public void testGetNextCardOneCard() {
    CardDeck deck = new CardDeck();
    deck.getNextCard();
    assertEquals(deck.deckSize(), 60-1);
  }

  // when n cards are taken off the deck
  // the deck has n less cards
  @Test
  public void testGetNextCardManyCards() {
    CardDeck deck = new CardDeck();
    for (int i = 1; i <= 22; i++) {
        deck.getNextCard();
    }
    assertEquals(deck.deckSize, 60-23)
  }

  // when all the cards are taken off the stack
  // the deck is refilled and reshuffled
  @Test
  public void testGetNextCardAllCards() {
    CardDeck deck = new CardDeck();
    for (int i = 1; i <= 60; i++) {
        deck.getNextCard();
    }
    assertEquals(deck.deckSize(), 60);
  }
}