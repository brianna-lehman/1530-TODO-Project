import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class Menu extends JMenuBar implements Serializable{
  JMenu file;
  final String[] INVALID_CHARS = {"~", "#", "%", "&", "*", "{", "}", "\\", ":", "<", ">", "?", "/", "|", "\"", ".", " "};
  final String EXTENSION = ".ser";

  enum FileValidation {
    VALID,
    INVALID,
    EMPTY,
  }

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
        // get a filename, check that it's valid, and save the current game
        String filename = JOptionPane.showInputDialog("What would you like to name the file? ");
        filename = filename.replace(" ", "_");

        if (confirmFilenamePopup(filename)) {
          boolean success = saveGame(new File(filename+EXTENSION));
          if (success) {
            JOptionPane.showMessageDialog(null,"Game saved sucessfully");

            // reset the labels on the tokens
            Game game = Game.getInstance();
            Token[] tokens = game.getTokens();
            for (int i=0; i < tokens.length; i++) {
              tokens[i].setLabelOnToken(tokens[i].getPlayerIndex());
            }
          }
          else {
            JOptionPane.showMessageDialog(null,"Error saving game");
          }
        }
      }
    });
    file.add(save);
  }

  public boolean confirmFilenamePopup(String filename) {
    FileValidation type = validateFilename(filename);
    if (type == FileValidation.VALID) {
      return true;
    }
    else if (type == FileValidation.EMPTY) {
      JOptionPane.showMessageDialog(null, "Your file cannot be empty");
    }
    else if (type == FileValidation.INVALID) {
      JOptionPane.showMessageDialog(null, "Your file cannot contain these characters: "+Arrays.toString(INVALID_CHARS));
    }
    return false;
  }

  private FileValidation validateFilename(String filename) {
    boolean filenameContainsInvalidChar = false;
    for (int i = 0; i < INVALID_CHARS.length; i++) {
      if (filename.contains(INVALID_CHARS[i])) {
        filenameContainsInvalidChar = true;
      }
    }

    if (filename.isEmpty() || filename == null) {
      return FileValidation.EMPTY;
    }
    else if (filenameContainsInvalidChar) {
      return FileValidation.INVALID;
    }
    else {
      return FileValidation.VALID;
    }
  }

  public boolean saveGame(File file) {
    try {
        FileOutputStream fileStream = new FileOutputStream(file);
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);

        Game game = Game.getInstance();

        objectStream.writeObject(game);

        objectStream.close();
        fileStream.close();

        return true;
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,"Error: "+e.toString());
        return false;
    }
  }
}