import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Menu extends JMenuBar {
  JMenu file;

  public Menu() {
    JMenu file = new JMenu("File");
    file.setMnemonic(KeyEvent.VK_A);
    this.add(file);

    JMenuItem load = new JMenuItem("Load Game");
    load.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        // code to load a saved game
      }
    });
    file.add(load);

    JMenuItem save = new JMenuItem("Save Game");
    save.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        // code to save the current game
      }
    });
    file.add(save);
  }

  public void actionPerformed(ActionEvent e) {

  }
}