import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Token extends JButton {

  private final int player_index;

  public Token(int player_index) {
    this.player_index = player_index;
    this.setText("" + (player_index + 1));
    this.setPreferredSize(new Dimension(20, 20));
    this.setBorder(null);
    this.addActionListener(new ButtonListener());
  }

  /**
   * The action to take when the current player's token is clicked
   */
  private class ButtonListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      if(Game.current_turn == player_index)
      {
        Game game = Game.getInstance();
        game.nextTurn();
      }
    }
  }
}
