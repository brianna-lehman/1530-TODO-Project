/*
 * This class will extend JPanel and be used to represent the game board,
 * i.e. the panel which will contain all of the movement squares.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Board extends JPanel {

  // height of the game board, 0.75x height of the window
  public static final int GAMEBOARD_HEIGHT = Game.WINDOW_HEIGHT - 200;

  // grid dimensions - ROWS and COLS must be equal
  private static final int ROWS = 7;
  private static final int COLS = 7;

  // Map between the order in which squares appear and their location on the board
  private Map<Integer, SquareDetails> indexToSquareMap = new HashMap<Integer, SquareDetails>();
  private int winningIndex;
  private int middleIndex;

  // 2D array of GameboardSquare which make up the board
  private GameboardSquare [][] squares = new GameboardSquare [ROWS][COLS];

  // constraints to set the locations of squares in the grid
  private GridBagConstraints constraints = new GridBagConstraints();

  public Board() {
    setLayout(new GridBagLayout());
    setBackground(Game.CL_WHITE);
    setPreferredSize(new Dimension(Game.WINDOW_WIDTH, GAMEBOARD_HEIGHT));

    // initialize board with all white squares
    for (int row = 0; row < ROWS; row++) {
      for (int col = 0; col < COLS; col++) {
        squares[row][col] = new GameboardSquare(Game.CL_WHITE);
        constraints.gridx = col;
        constraints.gridy = row;
        add(squares[row][col], constraints);
      }
    }

    int currIndex = 0;
    for (int row = 0; row < ROWS; row++) {
      if (row % 4 == 0) {
        // add a full row of colored squares starting from the left
        for (int col = 0; col < COLS; col++) {
          if(row == 0 && col == 0) continue;
          Color color = getColorFromIndex(currIndex++);
          squares[row][col].setColor(color);
          indexToSquareMap.put(currIndex, new SquareDetails(row, col, color));
        }
      } else if (row % 2 == 0) {
        // add a full row of colored squares starting from the right
        for (int col = COLS - 1; col >= 0; col--) {
          Color color = getColorFromIndex(currIndex++);
          squares[row][col].setColor(color);
          indexToSquareMap.put(currIndex, new SquareDetails(row, col, color));
        }
      } else {
        // the odd-numbered rows will mostly be white, exceptions being the
        // one colored square that will be used to connect even, colored rows
        for (int col = 0; col < COLS; col++) {
          if (col == COLS - 1 && (row - 1) % 4 == 0) {
            // connect rows at the right end of the board
            Color color = getColorFromIndex(currIndex++);
            squares[row][col].setColor(color);
            indexToSquareMap.put(currIndex, new SquareDetails(row, col, color));
          } else if (col == 0 && (row - 3) % 4 == 0) {
            // connect rows at the left end of the board
            Color color = getColorFromIndex(currIndex++);
            squares[row][col].setColor(color);
            indexToSquareMap.put(currIndex, new SquareDetails(row, col, color));
          }
        }
      }
    }

    // add Grandma's House
    GameboardSquare grandmasHouse = squares[ROWS - 1][0];
    grandmasHouse.setColor(Game.CL_PINK);
    grandmasHouse.setLabelText("Grandma's House");
    winningIndex = currIndex;
    indexToSquareMap.put(winningIndex, new SquareDetails(ROWS - 1, 0, Game.CL_PINK));

    // set middle index for "Move To Middle card"
    middleIndex = winningIndex / 2;
    GameboardSquare middleSquare = squares[3][0];
    middleSquare.setLabelText("Middle");

    // add Start square
    GameboardSquare startSquare = squares[0][0];
    startSquare.setColor(Game.CL_PURPLE);
    startSquare.setLabelText("Start");
    indexToSquareMap.put(0, new SquareDetails(0, 0, Game.CL_PURPLE));
    for(int i = 0; i < Game.NUMBER_OF_PLAYERS; i++) {
      startSquare.addToken(Game.tokens[i]);
    }
    for(Token t : Game.tokens){
      t.currentSquare = 0;
    }


  }

  /**
   * This method takes an index and returns a Color. It is used to determine the
   * color of the next gameboard square as they are being generated.
   * @param index the index of the gameboard square
   * @return the color that corresponds to index
   */
  private Color getColorFromIndex(int index) {
    switch (index % 5) {
      case 0: return Game.CL_RED;
      case 1: return Game.CL_YELLOW;
      case 2: return Game.CL_BLUE;
      case 3: return Game.CL_GREEN;
      case 4: return Game.CL_ORANGE;
      default: return Game.CL_WHITE;
    }
  }

  private int getIndexFromColor(Color color){
    if(color.getRGB() == Game.CL_RED.getRGB()){
      return 0;
    }
    else if(color.getRGB() == Game.CL_YELLOW.getRGB()){
      return 1;
    }
    else if(color.getRGB() == Game.CL_BLUE.getRGB()){
      return 2;
    }
    else if(color.getRGB() == Game.CL_GREEN.getRGB()){
      return 3;
    }
    else if(color.getRGB() == Game.CL_ORANGE.getRGB()){
      return 4;
    }
    return -1;
  }

  void moveToken(Token token, Card currentCard) {
    int position = token.currentSquare;
    int newPosition = nextSquare(position, currentCard);
    if (newPosition >= winningIndex) {
      newPosition = winningIndex;
    }

    SquareDetails newSquare = indexToSquareMap.get(newPosition);
    GameboardSquare newGS = squares[newSquare.x][newSquare.y];
    SquareDetails oldSquare = indexToSquareMap.get(position);
    GameboardSquare oldGS = squares[oldSquare.x][oldSquare.y];
    oldGS.removeToken(token);
    newGS.addToken(token);
    token.currentSquare = newPosition;

    if(newPosition == winningIndex) {
      displayVictoryBox(token.player_index);
      Game.getInstance().restart();
    }
  }

  void clearBoard(Token[] tokens) {
    for (int i = 0; i < tokens.length; i++) {
      int position = tokens[i].currentSquare;
      SquareDetails square = indexToSquareMap.get(position);
      GameboardSquare gs = squares[square.x][square.y];
      gs.removeToken(tokens[i]);
    }
  }

  void setToken(Token token) {
    int position = token.currentSquare;
    SquareDetails square = indexToSquareMap.get(position);
    GameboardSquare gs = squares[square.x][square.y];
    gs.addToken(token);
  }

  private int nextSquare(int currentSquare, Card card) {
    switch (card.getCardType()) {
      case MIDDLE:
        return middleIndex;
      case SKIP:
        return currentSquare;
      default: {
        boolean isDouble = card.isMultiple;
        Color nextSquareColor = card.color;
        Color currentSquareColor = indexToSquareMap.get(currentSquare).color;

        int nextColorIndex = getIndexFromColor(nextSquareColor);
        int currentColorIndex = getIndexFromColor(currentSquareColor);
        int moves = 1;

        while ((currentColorIndex + moves) % 5 != nextColorIndex) {
          moves++;
        }

        if (isDouble) {
          moves += 5;
        }

        return currentSquare + moves;
      }
    }
  }

  private class SquareDetails {
    int x;
    int y;
    Color color;

    SquareDetails(int x, int y, Color c) {
      this.x = x;
      this.y = y;
      this.color = c;
    }
  }

  private void displayVictoryBox(int playerIndex){
    JPanel panel = new JPanel(new GridLayout(0, 1));
    JOptionPane.showMessageDialog(panel, Game.playerNames[playerIndex] + " wins!");
  }

  /**
   * This class extends JPanel and will represent a square on the gameboard.
   */
  private class GameboardSquare extends JPanel {

    // color of this square
    private Color color;
    // label at the top of the square
    private JLabel label;
    // panel containing the tokens in the middle of the square
    private JPanel tokens;

    public GameboardSquare(Color _color) {
      this.setLayout(new BorderLayout());

      this.color = _color;
      setBackground(this.color);

      label = new JLabel();
      label.setFont(new Font("Courier", Font.PLAIN, 12));
      label.setForeground(Game.CL_WHITE);
      label.setHorizontalAlignment(JLabel.CENTER);
      label.setMinimumSize(new Dimension(0, 20));
      label.setPreferredSize(new Dimension(0, 20));
      label.setMaximumSize(new Dimension(0, 20));
      this.add(label, BorderLayout.PAGE_START);

      tokens = new JPanel();
      tokens.setOpaque(false);
      this.add(tokens, BorderLayout.CENTER);

      // spacer at the bottom of a square same size as the label to center the tokens JPanel
      JPanel spacer = new JPanel();
      spacer.setMinimumSize(new Dimension(0, 20));
      spacer.setPreferredSize(new Dimension(0, 20));
      spacer.setMaximumSize(new Dimension(0, 20));
      spacer.setOpaque(false);
      this.add(spacer, BorderLayout.PAGE_END);
    }

    public void setLabelText(String text) {
      label.setText(text);
    }

    public void addToken(Token t) {
      tokens.add(t);
      validate();
      repaint();
    }

    public void removeToken(Token t) {
      tokens.remove(t);
      validate();
      repaint();
    }

    @Override
    public Dimension getPreferredSize() {
      return new Dimension(Game.WINDOW_WIDTH / ROWS, GAMEBOARD_HEIGHT / COLS);
    }

    // method to set the color of this square
    public void setColor(Color _color) {
      this.color = _color;
      setBackground(this.color);
    }
  }
}
