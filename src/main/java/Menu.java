import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class Menu extends JMenuBar implements Serializable{
  JMenu file;
  final String[] INVALID_CHARS = {"~", "#", "%", "&", "*", "{", "}", "\\", ":", "<", ">", "?", "/", "|", "\"", ".", " "};
  final String EXTENSION = ".ser";
  final String DIRECTORY = "saved_games/";

  enum FileValidation {
    VALID,
    INVALID,
    EMPTY,
    EXISTS
  }

  public Menu() {
    JMenu file = new JMenu("File");
    this.add(file);

    JMenuItem load = new JMenuItem("Load Game");
    load.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        // find a list of all files in DIRECTORY w/ the extension .ser
        File dir = new File(DIRECTORY);
        File[] uncheckedFiles = dir.listFiles();
        ArrayList<String> gameFileNames = new ArrayList<String>();
        for (int i = 0; i < uncheckedFiles.length; i++) {
          String fileName = uncheckedFiles[i].getName();
          if (fileName.contains(EXTENSION)) {
            gameFileNames.add(fileName);
          }
        }

        // create a popup for player to chose from this list
        String filename;
        String[] fileNames = gameFileNames.toArray(new String[0]);
        JComboBox<String> combo = new JComboBox<String>(fileNames);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Choose a game to load"));
        panel.add(combo);
        int result = JOptionPane.showConfirmDialog(null, panel, "Load Game",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            filename = (String)combo.getSelectedItem();
            loadGame(new File(DIRECTORY+filename));
        }
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
          File dir = new File(DIRECTORY);
          if (!dir.exists()) {
            try {
              dir.mkdir();
            } catch (Exception e) {
              JOptionPane.showMessageDialog(null,"Error creating directory");
            }
          }

          boolean success = saveGame(new File(DIRECTORY+filename+EXTENSION));
          if (success) {
            JOptionPane.showMessageDialog(null,"Game saved sucessfully");
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
    else if (type == FileValidation.EXISTS) {
      JOptionPane.showMessageDialog(null, "This file already exists");
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
    File file = new File(DIRECTORY+filename+EXTENSION);

    if (filename.isEmpty() || filename == null) {
      return FileValidation.EMPTY;
    }
    else if (filenameContainsInvalidChar) {
      return FileValidation.INVALID;
    }
    else if (file.exists()) {
      return FileValidation.EXISTS;
    }
    else {
      return FileValidation.VALID;
    }
  }

  public boolean loadGame(File file) {
    try {
      FileInputStream fileStream = new FileInputStream(file);
      ObjectInputStream objectStream = new ObjectInputStream(fileStream);

      Game game = Game.getInstance();

      ArrayList<Object> gameDetails = (ArrayList<Object>)objectStream.readObject();

      int j = 0;

      // resetting the timer
      int time = (int)gameDetails.get(j++);
      game.getMessagePanel().getTimer().setTime(time);
      game.getMessagePanel().getTimer().start();

      // resetting the tokens
      int numOfPlayers = (int)gameDetails.get(j++);
      Token[] tokens = (Token[])gameDetails.get(j++);
      game.setNumberOfPlayers(numOfPlayers);
      game.getBoard().clearBoard(game.getTokens());
      game.setTokens(tokens);
      for (int i = 0; i < tokens.length; i++) {
          game.getBoard().setToken(tokens[i]);
      }

      // resetting the message panel
      int currentTurn = (int)gameDetails.get(j++);
      String userMessage = (String)gameDetails.get(j++);
      game.setCurrentTurn(currentTurn);
      game.getMessagePanel().setCurrentTurn(currentTurn);
      game.getMessagePanel().setMessage(userMessage);

      // resetting the card deck and card deck panel
      CardDeck deck = (CardDeck)gameDetails.get(j++);
      boolean cardDrawn = (boolean)gameDetails.get(j++);
      Card currentCard = (Card)gameDetails.get(j++);
      game.setDeck(deck);
      game.setCardDrawn(cardDrawn);
      game.getCardDeckPanel().removeCurrentCard();
      game.getCardDeckPanel().setCurrentCard(currentCard);

      objectStream.close();
      fileStream.close();

      return true;

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,"Error: "+e.toString());
        return false;
    }
  }

  public boolean saveGame(File file) {
    try {
        FileOutputStream fileStream = new FileOutputStream(file);
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);

        Game game = Game.getInstance();
        ArrayList<Object> gameDetails = new ArrayList<Object>();

        // saving the timer
        int time = game.getMessagePanel().getTimer().getTime();
        gameDetails.add(time);

        // saving the token details
        int numOfPlayers = game.getNumberOfPlayers();
        gameDetails.add(numOfPlayers);
        Token[] tokens = game.getTokens();
        gameDetails.add(tokens);
        int currentTurn = game.getCurrentTurn();
        gameDetails.add(currentTurn);
        String userMessage = game.getMessagePanel().getMessage();
        gameDetails.add(userMessage);

        // saving the deck details
        CardDeck deck = game.getDeck();
        gameDetails.add(deck);
        boolean cardDrawn = game.getCardDrawn();
        gameDetails.add(cardDrawn);
        JOptionPane.showMessageDialog(null, "Card Drawn: "+cardDrawn);
        Card currentCard = game.getCardDeckPanel().getCurrentCard();
        gameDetails.add(currentCard);

        // write all the above objects to objectStream
        objectStream.writeObject(gameDetails);

        objectStream.close();
        fileStream.close();

        return true;

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,"Error: "+e.toString());
        return false;
    }
  }
}