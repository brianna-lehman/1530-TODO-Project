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
    file.add(load);

    JMenuItem save = new JMenuItem("Save Game");
    file.add(save);
  }
}