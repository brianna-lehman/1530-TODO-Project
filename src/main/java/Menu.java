import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Menu extends JMenuBar {
  JMenu file;
  public static final String[] incorrectChars = {"~", "#", "%", "&", "*", "{", "}", "\\", ":", "<", ">", "?", "/", "|", "\"", "."};

  public Menu() {
    JMenu file = new JMenu("File");
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
        // create popup asking what to name the game file
        // validate that the name returned is valid
        // store the game instance in an objectoutputstream
        String filename = JOptionPane.showInputDialog("What would you like to name the file? ");
        JOptionPane.showMessageDialog(null,validateFilename(filename));
      }
    });
    file.add(save);
  }

  private String validateFilename(String filename) {
    boolean filenameContainsInvalidChar = false;
    for (int i = 0; i < incorrectChars.length; i++) {
      if (filename.contains(incorrectChars[i])) {
        filenameContainsInvalidChar = true;
      }
    }

    if (filename.isEmpty()) {
      return "The file must have a name.";
    }
    else if (filenameContainsInvalidChar) {
      StringBuilder errorMsg = new StringBuilder("The file cannot contain these characters: ");
      errorMsg.append(Arrays.toString(incorrectChars));
      return errorMsg.toString();
    }
    else {
      StringBuilder correctMsg = new StringBuilder("Do you want to save you file as '");
      correctMsg.append(filename);
      correctMsg.append("'?");
      return correctMsg.toString();
    }
  }
}