/*
 * This class extends JPanel and will be a panel containing two smaller panels
 * which will represent the card and discard piles for the game.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class BoomerangPanel extends JPanel {

  JLabel label;
  int num = 3;

  public BoomerangPanel() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    label = new JLabel();
    label.setFont(new Font("Courier", Font.PLAIN, 16));
    label.setText("Boomerangs remaining: 3");
    label.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(label);

    JButton throwBoomerangButton = new JButton("Throw");
    ActionListener throwBoomerangListener = new ThrowBoomerangListener();
    throwBoomerangButton.addActionListener(throwBoomerangListener);
    throwBoomerangButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(throwBoomerangButton);
  }

  public void setNum(int i) {
    num = i;
    label.setText("Boomerangs remaining: " + i);
  }

  private class ThrowBoomerangListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Game game = Game.getInstance();

      if(num > 0 && game.boomerangNext == false) {
        game.numBoomerangs[game.current_turn]--;
        game.getMessagePanel().setMessage("Select another player");
        game.boomerangNext = true;
      }
    }
  }
}
