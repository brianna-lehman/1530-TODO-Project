import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Token extends JButton {

  final int player_index;
  int currentSquare;

  public Token(int player_index) {
    this.player_index = player_index;
    // 0 is the start square
    this.currentSquare = 0;
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

  /**
   * The action to take when the current player's token is clicked
   */
  private class ButtonListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      Game game = Game.getInstance();
      if(game.boomerangNext == true && game.cardDrawn == false && game.current_turn != player_index)
      {
        game.boomerangTarget = player_index;
        game.getMessagePanel().setMessage("Player " + (player_index + 1) + " targeted. Draw a card");
      }
      if(game.current_turn == player_index && game.cardDrawn)
      {
        game.nextTurn();
      }
    }
  }
}
