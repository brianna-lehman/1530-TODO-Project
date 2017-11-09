import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SaveButton extends JButton {

  public SaveButton() {
        this.setText("" + "Save");
        this.setBorder(null);
        this.addActionListener(new ButtonListener());
  }

  private class ButtonListener implements ActionListener {

      public void actionPerformed(ActionEvent e) {
      // code to save this game to a file to be loaded later
      }
  }
}