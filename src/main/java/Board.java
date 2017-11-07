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
        }
      } else if (row % 2 == 0) {
        // add a full row of colored squares starting from the right
        for (int col = COLS - 1; col >= 0; col--) {
          squares[row][col].setColor(getColorFromIndex(currIndex++));
        }
      } else {
        // the odd-numbered rows will mostly be white, exceptions being the
        // one colored square that will be used to connect even, colored rows
        for (int col = 0; col < COLS; col++) {
          if (row == 3 && col == 0) {
            squares[row][col].setColor(Game.CL_BRIGHT_PURPLE);
          }
          else {
            if (col == COLS - 1 && (row - 1) % 4 == 0) {
              // connect rows at the right end of the board
              squares[row][col].setColor(getColorFromIndex(currIndex++));
            } else if (col == 0 && (row - 3) % 4 == 0) {
              // connect rows at the left end of the board
              squares[row][col].setColor(getColorFromIndex(currIndex++));
            }
          }
        }
      }
    }

    // add Grandma's House
    GameboardSquare grandmasHouse = squares[0][0];
    grandmasHouse.setColor(Game.CL_PINK);
    grandmasHouse.setLabelText("Grandma's House");

    // add Start square
    GameboardSquare startSquare = squares[ROWS - 1][0];
    startSquare.setColor(Game.CL_PURPLE);
    startSquare.setLabelText("START");
    for(int i = 0; i < Game.NUMBER_OF_PLAYERS; i++) {
      startSquare.addToken(Game.tokens[i]);
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
    }

    public void removeToken(Token t) {
      tokens.remove(t);
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
