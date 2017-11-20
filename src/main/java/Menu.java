import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class Menu extends JMenuBar {
  JMenu file;
  final String[] INVALID_CHARS = {"~", "#", "%", "&", "*", "{", "}", "\\", ":", "<", ">", "?", "/", "|", "\"", ".", " "};
  final String EXTENSION = ".ser";
  final String DIRECTORY = "saved_games/";
  final String NO_CARD_DRAWN = "NO_CARD_DRAWN";

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
        if (!dir.exists() || dir.listFiles().length == 0) {
          JOptionPane.showMessageDialog(null, "There are no saved games");
        }
        else {
          ArrayList<String> gameFileNames = new ArrayList<String>();
          File[] uncheckedFiles = dir.listFiles();
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
      }
    });
    file.add(load);

    JMenuItem save = new JMenuItem("Save Game");
    save.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        // get a filename, check that it's valid, and save the current game
        String filename = JOptionPane.showInputDialog("What would you like to name the file? ");

        if (filename != null) {
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
      Scanner fileScanner = new Scanner(fileStream);

      Game game = Game.getInstance();

      // resetting the timer
      int time = Integer.parseInt(fileScanner.nextLine());
      game.getMessagePanel().getTimer().setTime(time);
      game.getMessagePanel().getTimer().start();

      // resetting the players
      int numOfPlayers = Integer.parseInt(fileScanner.nextLine());
      game.setNumberOfPlayers(numOfPlayers);

      // reset the tokens
      Token[] newTokens = new Token[numOfPlayers];
      for (int i = 0; i < numOfPlayers; i++) {
        String[] tokenStringParts = fileScanner.nextLine().split(":");
        int playerIndex = Integer.parseInt(tokenStringParts[0]);
        int currentSquare = Integer.parseInt(tokenStringParts[1]);
        newTokens[i] = new Token(playerIndex);
        newTokens[i].currentSquare = currentSquare;
      }
      // game.getBoard().clearBoard(game.getTokens());
      game.getBoard().clearBoard(game.getOriginalTokens());
      game.setTokens(newTokens);
      for (int i = 0; i < newTokens.length; i++) {
          game.getBoard().setToken(newTokens[i]);
      }

      // resetting the message panel
      int currentTurn = Integer.parseInt(fileScanner.nextLine());
      String userMessage = fileScanner.nextLine();
      game.setCurrentTurn(currentTurn);
      game.getMessagePanel().setCurrentTurn(currentTurn);
      game.getMessagePanel().setMessage(userMessage);

      // resetting the card deck and card deck panel
      String cardDeckString = fileScanner.nextLine();
      CardDeck newDeck = new CardDeck(cardDeckString);
      boolean cardDrawn = Boolean.parseBoolean(fileScanner.nextLine());
      String currentCardString = fileScanner.nextLine();
      game.setDeck(newDeck);
      game.setCardDrawn(cardDrawn);
      if (!currentCardString.equals(NO_CARD_DRAWN)) {
        game.getCardDeckPanel().setCurrentCard(new Card(currentCardString));
      }

      fileScanner.close();
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
        PrintWriter fileWriter = new PrintWriter(fileStream);

        Game game = Game.getInstance();

        // saving the timer
        int time = game.getMessagePanel().getTimer().getTime();
        fileWriter.println(time);

        // saving the token details
        int numOfPlayers = game.getNumberOfPlayers();
        fileWriter.println(numOfPlayers);
        Token[] tokens = game.getTokens();
        for (int i = 0; i < tokens.length; i++) {
          fileWriter.println(tokens[i].getPlayerIndex() + ":" + tokens[i].currentSquare);
        }
        int currentTurn = game.getCurrentTurn();
        fileWriter.println(currentTurn);
        String userMessage = game.getMessagePanel().getMessage();
        fileWriter.println(userMessage);

        // saving the deck details
        CardDeck deck = game.getDeck();
        fileWriter.println(deck.toString());
        boolean cardDrawn = game.getCardDrawn();
        fileWriter.println(cardDrawn);
        Card currentCard = game.getCardDeckPanel().getCurrentCard();
        if (currentCard != null) {
          fileWriter.println(currentCard.toString());
        } else {
          fileWriter.println(NO_CARD_DRAWN);
        }

        fileWriter.close();
        fileStream.close();

        return true;

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,"Error: "+e.toString());
        return false;
    }
  }
}
