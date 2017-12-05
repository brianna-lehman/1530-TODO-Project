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
  static final Color CL_BRIGHT_PURPLE = hexToColor("#CA19D1");
  static final Color CL_WHITE = hexToColor("#FFFFFF");
  static final Color CL_RED = hexToColor("#D73149");
  static final Color CL_YELLOW = hexToColor("#DCCC1F");
  static final Color CL_BLUE = hexToColor("#1576D5");
  static final Color CL_GREEN = hexToColor("#4C9C55");
  static final Color CL_ORANGE = hexToColor("#DD6652");
  static final Color CL_LIGHT_PINK = hexToColor("#FF9797");
  static final Color CL_BROWN = hexToColor("#310C0C");
  static final Color CL_LIGHT_BROWN = hexToColor("#631919");
  static final Color CL_CYAN = hexToColor("#00FFFF");
  static final Color CL_BLACK = hexToColor("#000000");


  private static Game game = null;
  static int NUMBER_OF_PLAYERS = -1;
  static int current_turn = 0;
  static int numTurns;
  static Token[] tokens;
  static JMenuBar menu;
  static String[] playerNames;
  static CardDeck deck;
  static JPanel utilityPanel;
  static MessagePanel messagePanel;
  static Board gameboard;
  static CardDeckPanel cardDeckPanel;
  static boolean cardDrawn = false;
  static int mode = 0;

  static final int MODE_CLASSIC = 0;

  public Game() {
    // set label for frame
    super("World of Sweets");

    //dialog box for specifying which mode
    String[] modes = {"Classic"};
    JComboBox<String> comboModes = new JComboBox<>(modes);
    JPanel panelModes = new JPanel(new GridLayout(0, 1));
    panelModes.add(new JLabel("Welcome! Which gamemode?"));
    panelModes.add(comboModes);
    int resultModes = JOptionPane.showConfirmDialog(null, panelModes, "World of Sweets - Gamemode",
                                               JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (resultModes == JOptionPane.OK_OPTION) {
      mode = comboModes.getSelectedIndex();
    }
    else {
      System.exit(0);
    }

    if(mode == MODE_CLASSIC)
      initClassic();
  }

  public void initClassic() {
    //dialog box for specifying number of players
    String[] players = {"2", "3", "4"};
    JComboBox<String> combo = new JComboBox<>(players);
    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("How many players?"));
    panel.add(combo);
    int result = JOptionPane.showConfirmDialog(null, panel, "World of Sweets - Number of Players",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        NUMBER_OF_PLAYERS = Integer.parseInt((String)combo.getSelectedItem());
        tokens = new Token[NUMBER_OF_PLAYERS];
    }
    else {
        System.exit(0);
    }

    playerNames = new String[NUMBER_OF_PLAYERS];
    for(int i = 0; i < NUMBER_OF_PLAYERS; i++) {
      panel = new JPanel();
      JTextField txt = new JTextField(10);
      panel.add(new JLabel(String.format("Enter Player %d's name", i + 1)));
      panel.add(txt);
      result = JOptionPane.showConfirmDialog(null, panel, "World of Sweets - Number of Players",
                                                 JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

      if (result == JOptionPane.OK_OPTION) {
        String name = txt.getText();
        if(name.equals(""))
          playerNames[i] = "Player " + (i + 1);
        else
          playerNames[i] = name;
      }
      else {
        playerNames[i] = "Player " + (i + 1);
      }
    }

    numTurns = 1;

    deck = new CardDeck();
    utilityPanel = new JPanel();
    messagePanel = new MessagePanel();

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
    utilityPanel = new JPanel(new GridLayout(1, 2));
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

  public void restartClassic()
  {
    int reply = JOptionPane.showConfirmDialog(null, "Would you like to play again?", "World of Sweets - Replay", JOptionPane.YES_NO_OPTION);
    if (reply == JOptionPane.YES_OPTION) {

      current_turn = 0;

      String[] players = {"2", "3", "4"};
      JComboBox<String> combo = new JComboBox<>(players);
      JPanel panel = new JPanel(new GridLayout(0, 1));
      panel.add(new JLabel("Welcome! How many players?"));
      panel.add(combo);
      int result = JOptionPane.showConfirmDialog(null, panel, "World of Sweets - Number of Players",
          JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
      if (result == JOptionPane.OK_OPTION) {
          NUMBER_OF_PLAYERS = Integer.parseInt((String)combo.getSelectedItem());
          tokens = new Token[NUMBER_OF_PLAYERS];
      }
      else {
          System.exit(0);
      }

      for(int i = 0; i < NUMBER_OF_PLAYERS; i++) {
        tokens[i] = new Token(i);
      }

      playerNames = new String[NUMBER_OF_PLAYERS];
      for(int i = 0; i < NUMBER_OF_PLAYERS; i++) {
        panel = new JPanel();
        JTextField txt = new JTextField(10);
        panel.add(new JLabel(String.format("Enter Player %d's name", i + 1)));
        panel.add(txt);
        result = JOptionPane.showConfirmDialog(null, panel, "World of Sweets - Number of Players",
                                               JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
          String name = txt.getText();
          if(name.equals(""))
            playerNames[i] = "Player " + (i + 1);
          else
            playerNames[i] = name;
        }
        else {
          playerNames[i] = "Player " + (i + 1);
        }
      }

      numTurns = 0;
      cardDrawn = false;
      deck = new CardDeck();

      remove(gameboard);
      gameboard = new Board();
      add(gameboard);

      utilityPanel.remove(messagePanel);
      messagePanel = new MessagePanel();
      utilityPanel.add(messagePanel);

      utilityPanel.remove(cardDeckPanel);
      cardDeckPanel = new CardDeckPanel();
      utilityPanel.add(cardDeckPanel);

      pack();
    }
    else {
      System.exit(0);
    }
  }

  public void restart() {
    if(mode == MODE_CLASSIC)
      restartClassic();
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
    boolean win = this.getBoard().moveToken(tokens[current_turn], currentCard);

    current_turn = (current_turn + 1) % NUMBER_OF_PLAYERS;
    this.getMessagePanel().setCurrentTurn(current_turn);
    this.getMessagePanel().setMessage("");

    // next player should draw a new card
    cardDrawn = false;

    // increment turn counter
    this.getMessagePanel().incrementTurn();

    if(win)
      this.restart();
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

  public Token[] getOriginalTokens() {
    return tokens;
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

  public String[] getPlayerNames() {
    return playerNames;
  }

  public void setPlayerNames(String[] newNames) {
    playerNames = newNames;
  }

  // main simply creates an instance of Game for now
  public static void main(String [] args) {
    // creates a single instance of the game that can be used in all the other classes
    Game game = Game.getInstance();
  }
}
