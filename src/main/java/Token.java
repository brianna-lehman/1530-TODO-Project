import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Token extends JButton {

  final int player_index;
  int currentSquare;
  boolean isAI;

  public Token(int player_index, boolean isAI) {
    this.player_index = player_index;
    // 0 is the start square
    this.currentSquare = 0;
    this.isAI = isAI;
    this.setText("" + (player_index + 1));
    this.setPreferredSize(new Dimension(20, 20));
    this.setBorder(null);
    this.addActionListener(new ButtonListener());
  }

  public void setLabelOnToken(int number) {
    if (number == player_index) {
      this.setText("" + (number + 1));
    }
  }

  public int getPlayerIndex() {
    return player_index;
  }

  public boolean getAIStatus() {
    return isAI;
  }

  /**
   * The action to take when the current player's token is clicked
   */
  private class ButtonListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      if(Game.current_turn == player_index && Game.cardDrawn)
      {
        Game game = Game.getInstance();
        game.nextTurn();
      }
    }
  }
}
