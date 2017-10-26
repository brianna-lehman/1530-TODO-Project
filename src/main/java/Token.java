import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Token extends JButton {

  public Token(int playernum) {
    this.setText("" + playernum);
    this.setPreferredSize(new Dimension(20, 20));
    this.setBorder(null);
    this.addActionListener(new ButtonListener());
  }

  /**
   * The action to take when one of the colored squares on the board is clicked
   */
  private class ButtonListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      Game game = Game.getInstance();
      MessagePanel messagePanel = game.getMessagePanel();
      messagePanel.setMessage("Next Player's Turn");
      // TODO
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
