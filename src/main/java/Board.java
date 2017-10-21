/*
 * This class will extend JPanel and be used to represent the game board,
 * i.e. the panel which will contain all of the movement squares.
 */

import java.awt.*;
import javax.swing.*;

public class Board extends JPanel {

  // height of the game board, 0.75x height of the window
  public static final int GAMEBOARD_HEIGHT = Game.WINDOW_HEIGHT - 200;

  public Board() {
    setLayout(new GridBagLayout());
    setBackground(Game.CL_WHITE);
    setPreferredSize(new Dimension(Game.WINDOW_WIDTH, GAMEBOARD_HEIGHT));

    // filler message for now
    JLabel boardMessage = new JLabel("This is where the board will go");
    add(boardMessage);
  }
}
