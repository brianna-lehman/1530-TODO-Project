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
        // find a list of all files in this directory w/ the extension .ser
        // create a popup for player to chose from this list
        // if they choose from the list
            // create a popup to remind the player that they'll override the current game
            // if they press okay
                loadGame(new File("test.ser"));
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

  public boolean loadGame(File file) {
    try {
      FileInputStream fileStream = new FileInputStream(file);
      ObjectInputStream objectStream = new ObjectInputStream(fileStream);

      Game game = Game.getInstance();

      ArrayList<Object> gameDetails = (ArrayList<Object>)objectStream.readObject();

      // resetting the timer
      int time = (int)gameDetails.get(0);
      game.getMessagePanel().getTimer().setTime(time);
      game.getMessagePanel().getTimer().start();

      // resetting the tokens
      int numOfPlayers = (int)gameDetails.get(1);
      Token[] tokens = (Token[])gameDetails.get(2);
      game.setNumberOfPlayers(numOfPlayers);
      game.getBoard().clearBoard(game.getTokens());
      game.setTokens(tokens);
      for (int i = 0; i < tokens.length; i++) {
          game.getBoard().setToken(tokens[i]);
      }

      // resetting the message panel
      int currentTurn = (int)gameDetails.get(3);
      game.setCurrentTurn(currentTurn);
      game.getMessagePanel().setCurrentTurn(currentTurn);

      // resetting the card deck and card deck panel
      CardDeck deck = (CardDeck)gameDetails.get(4);
      boolean cardDrawn = (boolean)gameDetails.get(5);
      Card currentCard = (Card)gameDetails.get(6);
      game.setDeck(deck);
      game.setCardDrawn(cardDrawn);
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

        // saving the deck details
        CardDeck deck = game.getDeck();
        gameDetails.add(deck);
        boolean cardDrawn = game.getCardDrawn();
        gameDetails.add(cardDrawn);
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