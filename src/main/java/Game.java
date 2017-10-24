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

  static int NUMBER_OF_PLAYERS = -1;

  // deck of cards
  static CardDeck deck = new CardDeck();
  static MessagePanel messagePanel = new MessagePanel();

  static MessagePanel messagePanel = new MessagePanel();

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
    } 
    else {
        System.exit(0);
    }

    // create a frame for the game
    setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // panel which contains all of the movement squares
    Board gameboard = new Board();
    add(gameboard);

    // utility panel at the bottom of the main panel
    JPanel utilityPanel = new JPanel(new GridLayout(1, 2));
    utilityPanel.setPreferredSize(new Dimension(WINDOW_WIDTH,
        WINDOW_HEIGHT - Board.GAMEBOARD_HEIGHT));
    add(utilityPanel, BorderLayout.PAGE_END);

    // panel to display messages to user
    utilityPanel.add(messagePanel);

    // add panels that represent card and discard piles
    CardDeckPanel cardDeckPanel = new CardDeckPanel();
    utilityPanel.add(cardDeckPanel);

    pack();
    setVisible(true);
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

  // main simply creates an instance of Game for now
  public static void main(String [] args) {
    new Game();
  }
}
