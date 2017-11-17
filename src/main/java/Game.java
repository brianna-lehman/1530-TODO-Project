import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class Game extends JFrame {

  // constants for the height and width of game window
  static final int WINDOW_WIDTH = 1000;
  static final int WINDOW_HEIGHT = 800;

  // candyland colors!
  static final Color CL_PINK = hexToColor("#ED5E9C");
  static final Color CL_PURPLE = hexToColor("#6A2580");
  static final Color CL_WHITE = hexToColor("#FFFFFF");
  static final Color CL_RED = hexToColor("#D73149");
  static final Color CL_YELLOW = hexToColor("#DCCC1F");
  static final Color CL_BLUE = hexToColor("#1576D5");
  static final Color CL_GREEN = hexToColor("#4C9C55");
  static final Color CL_ORANGE = hexToColor("#DD6652");
  static final Color CL_BRIGHT_PURPLE = hexToColor("#CA19D1");

  private static Game game = null;
  static int NUMBER_OF_PLAYERS = -1;
  static int current_turn = 0;
  static Token[] tokens;
  static CardDeck deck = new CardDeck();
  static JMenuBar menu;
  static MessagePanel messagePanel = new MessagePanel();
  static Board gameboard;
  static CardDeckPanel cardDeckPanel;
  static boolean cardDrawn = false;

  public Game() {
    // set label for frame
    super("World of Sweets");

    //dialog box for specifying number of players
    String[] players = {"2", "3", "4"};
    JComboBox<String> combo = new JComboBox<>(players);
    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Welcome! How many players?"));
    panel.add(combo);
    int result = JOptionPane.showConfirmDialog(null, panel, "Test",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        NUMBER_OF_PLAYERS = Integer.parseInt((String)combo.getSelectedItem());
        tokens = new Token[NUMBER_OF_PLAYERS];
    }
    else {
        System.exit(0);
    }

    // create a frame for the game
    setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // initialize the player tokens
    for(int i = 0; i < NUMBER_OF_PLAYERS; i++) {
      tokens[i] = new Token(i);
    }

    // menu bar for saving/loading games
    menu = new Menu();
    setJMenuBar(menu);

    // panel which contains all of the movement squares
    gameboard = new Board();
    add(gameboard);

    // utility panel at the bottom of the main panel
    JPanel utilityPanel = new JPanel(new GridLayout(1, 2));
    utilityPanel.setPreferredSize(new Dimension(WINDOW_WIDTH,
        WINDOW_HEIGHT - Board.GAMEBOARD_HEIGHT));
    add(utilityPanel, BorderLayout.PAGE_END);

    // panel to display messages to user
    utilityPanel.add(messagePanel);

    // add panels that represent card and discard piles
    cardDeckPanel = new CardDeckPanel();
    utilityPanel.add(cardDeckPanel);

    pack();
    setVisible(true);
    messagePanel.startTimer();
  }

  public void nextTurn()
  {
    //move Token of current player to square given my currentCard
    Card currentCard = cardDeckPanel.currentCard;
    //if no card has been drawn yet at the start of the game
    if (currentCard == null) {
      return;
    }

    // possibly move the token on the board
    this.getBoard().moveToken(tokens[current_turn], currentCard);

    current_turn = (current_turn + 1) % NUMBER_OF_PLAYERS;
    this.getMessagePanel().setCurrentTurn(current_turn);
    this.getMessagePanel().setMessage("");

    // next player should draw a new card
    cardDrawn = false;
  }

  /**
   * @param colorStr e.g. "#FFFFFF"
   * @return Color that represents this hex string
   */
  private static Color hexToColor(String colorStr) {
    return new Color(Integer.valueOf(colorStr.substring(1, 3), 16),
        Integer.valueOf(colorStr.substring(3, 5), 16),
        Integer.valueOf(colorStr.substring(5, 7), 16));
  }

  public static Game getInstance() {
    if (game == null) {
      game = new Game();
    }
    return game;
  }

  public MessagePanel getMessagePanel() {
    return messagePanel;
  }

  public Board getBoard() {
    return gameboard;
  }

  public Token[] getTokens() {
    Token[] tokensCopy = new Token[tokens.length];
    for (int i = 0; i < tokens.length; i++) {
      tokensCopy[i] = new Token(tokens[i].getPlayerIndex());
      tokensCopy[i].currentSquare = tokens[i].currentSquare;
    }
    return tokensCopy;
  }

  public void setTokens(Token[] t) {
    tokens = t;
  }

  public CardDeckPanel getCardDeckPanel() {
    return cardDeckPanel;
  }

  public int getNumberOfPlayers() {
    return NUMBER_OF_PLAYERS;
  }

  public void setNumberOfPlayers(int numOfPlayers) {
    NUMBER_OF_PLAYERS = numOfPlayers;
  }

  public int getCurrentTurn() {
    return current_turn;
  }

  public void setCurrentTurn(int turn) {
    current_turn = turn;
  }

  public CardDeck getDeck() {
    return deck;
  }

  public void setDeck(CardDeck d) {
    deck = d;
  }

  public boolean getCardDrawn() {
    return cardDrawn;
  }

  public void setCardDrawn(boolean cd) {
    cardDrawn = cd;
  }

  // main simply creates an instance of Game for now
  public static void main(String [] args) {
    // creates a single instance of the game that can be used in all the other classes
    Game game = Game.getInstance();
  }
}
