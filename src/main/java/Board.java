/*
 * This class will extend JPanel and be used to represent the game board,
 * i.e. the panel which will contain all of the movement squares.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel {

  // height of the game board, 0.75x height of the window
  public static final int GAMEBOARD_HEIGHT = Game.WINDOW_HEIGHT - 200;

  // grid dimensions - ROWS and COLS must be equal
  private static final int ROWS = 7;
  private static final int COLS = 7;

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
          squares[row][col].setColor(getColorFromIndex(currIndex++));
          squares[row][col].addButtonToSquare();
        }
      } else if (row % 2 == 0) {
        // add a full row of colored squares starting from the right
        for (int col = COLS - 1; col >= 0; col--) {
          squares[row][col].setColor(getColorFromIndex(currIndex++));
          squares[row][col].addButtonToSquare();
        }
      } else {
        // the odd-numbered rows will mostly be white, exceptions being the
        // one colored square that will be used to connect even, colored rows
        for (int col = 0; col < COLS; col++) {
          if (col == COLS - 1 && (row - 1) % 4 == 0) {
            // connect rows at the right end of the board
            squares[row][col].setColor(getColorFromIndex(currIndex++));
            squares[row][col].addButtonToSquare();
          } else if (col == 0 && (row - 3) % 4 == 0) {
            // connect rows at the left end of the board
            squares[row][col].setColor(getColorFromIndex(currIndex++));
            squares[row][col].addButtonToSquare();
          }
        }
      }
    }

    // add Grandma's House
    GameboardSquare grandmasHouse = squares[0][0];
    grandmasHouse.setColor(Game.CL_PINK);
    JLabel grandmasHouseLabel = new JLabel("Grandma");
    grandmasHouseLabel.setFont(new Font("Courier", Font.PLAIN, 12));
    grandmasHouseLabel.setForeground(Game.CL_WHITE);
    grandmasHouse.add(grandmasHouseLabel);

    // add Start square
    GameboardSquare startSquare = squares[ROWS - 1][0];
    startSquare.setColor(Game.CL_PURPLE);
    JLabel startLabel = new JLabel("START");
    startLabel.setFont(new Font("Courier", Font.PLAIN, 12));
    startLabel.setForeground(Game.CL_WHITE);
    startSquare.add(startLabel);
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

  /**
   * This class extends JPanel and will represent a square on the gameboard.
   */
  private class GameboardSquare extends JPanel {

    // color of this square
    private Color color;

    public GameboardSquare(Color _color) {
      this.color = _color;
      setBackground(this.color);
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

    /**
     * This method adds a button with no text to the square
     * if it's colored and returns true, otherwise returns false
     * @return a boolean signifying whether a button was added or not
     */
    public boolean addButtonToSquare() {
      if (this.color == Game.CL_WHITE) {
        return false;
      }

      JButton btn = new JButton(" ");
      ActionListener btnListener = new ButtonListener();
      btn.addActionListener(btnListener);
      this.add(btn);
      return true;
    }
  }

  /**
   * The action to take when one of the colored squares on the board is clicked
   */
  private class ButtonListener implements ActionListener {

    // TODO
    public void actionPerformed(ActionEvent e) {
      // somehow check that the square that was clicked matches the card the player got
      // move the current player's token
      // if the current player is at Grandma's house
        // display message - this player wins
      // else
        // switch to next player
        // display message - next player's turn
    }
  }
}
