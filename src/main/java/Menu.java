import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;

public class Menu extends JMenuBar {
  JMenu file;
  final String[] INVALID_CHARS = {"~", "#", "%", "&", "*", "{", "}", "\\", ":", "<", ">", "?", "/", "|", "\"", ".", " "};
  final String EXTENSION = ".ser";
  final String DIRECTORY = "saved_games/";
  final String NO_CARD_DRAWN = "NO_CARD_DRAWN";

  // key filename
  final String KEY_FILENAME = "game.key";

  // AES mode of operation - just ECB because I doubt Bill's son is capable of
  // executing ciphertext replay attacks
  final String MODE_OF_OPERATION = "AES/ECB/PKCS5Padding";

  // AES key used to encrypt/decrypt saved games
  Key secretKey = null;

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

    try {
      // attempt to load AES key from file
      File keyFile = new File(KEY_FILENAME);
      if (keyFile.exists()) {
        // read key from file
        FileInputStream keyFileStream = new FileInputStream(keyFile);
        ObjectInputStream keyStream = new ObjectInputStream(keyFileStream);
        secretKey = (Key) keyStream.readObject();
        keyFileStream.close();
        keyStream.close();
      } else {
        // key file does not exist, we should create a new key
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        secretKey = keyGen.generateKey();

        // write the new key to file to read later
        FileOutputStream keyFileStream = new FileOutputStream(keyFile);
        ObjectOutputStream keyStream = new ObjectOutputStream(keyFileStream);
        keyStream.writeObject(secretKey);
        keyFileStream.close();
        keyStream.close();
      }
    } catch (Exception e) {
      System.err.println("Error loading AES key from file.");
      System.exit(1);
    }
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
      // initialize cipher for decryption, ECB because who cares?
      Cipher cipher = Cipher.getInstance(MODE_OF_OPERATION);
      cipher.init(Cipher.DECRYPT_MODE, secretKey);

      // read encrypted game data from file
      byte[] encryptedGameData = new byte[(int) file.length()];
      FileInputStream fileStream = new FileInputStream(file);
      fileStream.read(encryptedGameData);
      fileStream.close();

      // decrypt game data
      byte[] gameData = cipher.doFinal(encryptedGameData);

      // set the game state
      setGameState(gameData);

      return true;

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,"Error: "+e.toString());
        return false;
    }
  }

  /**
   * Given game data, this method states the state of the game from that data.
   * @param gameData the game data that will be used to set the state of this
   * game that was read from a saved game file.
   */
  private void setGameState(byte[] gameData) {
    String gameDataString = "";
    try {
      gameDataString = new String(gameData, "UTF8");
    } catch (Exception e) {
      System.err.println("Somehow UTF8 is unsupported.");
      System.exit(1);
    }

    Game game = Game.getInstance();
    ArrayList<String> gameDataParts = new ArrayList<String>(Arrays.asList(gameDataString.split("\n")));
    Iterator<String> gameDataIter = gameDataParts.iterator();

    // resetting the timer
    int time = Integer.parseInt(gameDataIter.next());
    game.getMessagePanel().getTimer().setTime(time);
    game.getMessagePanel().getTimer().start();

    // resetting the turn counter
    int numTurns = Integer.parseInt(gameDataIter.next());
    game.numTurns = numTurns;
    game.getMessagePanel().refreshTurnCount();

    // resetting the players
    int numOfPlayers = Integer.parseInt(gameDataIter.next());
    game.setNumberOfPlayers(numOfPlayers);

    // reset the tokens
    Token[] newTokens = new Token[numOfPlayers];
    String[] newNames = new String[numOfPlayers];
    for (int i = 0; i < numOfPlayers; i++) {
      String[] tokenStringParts = gameDataIter.next().split(":");
      String playerName = new String(tokenStringParts[0]);
      int playerIndex = Integer.parseInt(tokenStringParts[1]);
      int currentSquare = Integer.parseInt(tokenStringParts[2]);
      boolean aiStatus = Boolean.parseBoolean(tokenStringParts[3]);
      newNames[i] = playerName;
      newTokens[i] = new Token(playerIndex, aiStatus);
      newTokens[i].currentSquare = currentSquare;
    }
    game.setPlayerNames(newNames);
    game.getBoard().clearBoard(game.getOriginalTokens());
    game.setTokens(newTokens);
    for (int i = 0; i < newTokens.length; i++) {
        game.getBoard().setToken(newTokens[i]);
    }

    // resetting the message panel
    int currentTurn = Integer.parseInt(gameDataIter.next());
    String userMessage = gameDataIter.next();
    game.setCurrentTurn(currentTurn);
    game.getMessagePanel().setCurrentTurn(currentTurn);
    game.getMessagePanel().setMessage(userMessage);

    // resetting the card deck and card deck panel
    String cardDeckString = gameDataIter.next();
    CardDeck newDeck = new CardDeck(cardDeckString);
    boolean cardDrawn = Boolean.parseBoolean(gameDataIter.next());
    String currentCardString = gameDataIter.next();
    game.setDeck(newDeck);
    game.setCardDrawn(cardDrawn);
    if (!currentCardString.equals(NO_CARD_DRAWN)) {
      game.getCardDeckPanel().setCurrentCard(new Card(currentCardString));
    }

    // resetting the strategic details
    int newMode = Integer.parseInt(gameDataIter.next());
    if(newMode == game.MODE_STRATEGIC)
    {
      if(game.mode == game.MODE_CLASSIC)
      {
        game.boomerangPanel = new BoomerangPanel();
        game.utilityPanel.add(game.boomerangPanel);
      }
      game.mode = newMode;

      int[] newBoomerangs = new int[numOfPlayers];
      for (int i = 0; i < numOfPlayers; i++) {
        newBoomerangs[i] = Integer.parseInt(gameDataIter.next());
      }
      game.numBoomerangs = newBoomerangs;
      game.refreshBoomerang();
    }
    else
    {
      if(game.mode == game.MODE_STRATEGIC)
        game.utilityPanel.remove(game.boomerangPanel);
    }
  }

  public boolean saveGame(File file) {
    try {
        // build string representation of game state
        String gameDataString = buildGameDataString();

        // initialize cipher to encrypt file
        Cipher cipher = Cipher.getInstance(MODE_OF_OPERATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // encrypt the game data
        byte[] gameData = gameDataString.getBytes("UTF8");
        byte[] encryptedGameData = cipher.doFinal(gameData);

        // write the encrypted data to file
        FileOutputStream fileStream = new FileOutputStream(file);
        fileStream.write(encryptedGameData);
        fileStream.close();

        return true;

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,"Error: "+e.toString());
        return false;
    }
  }

  /**
   * This method returns a string representation of the current state of the
   * game.
   * @return the string representation of the game
   */
  private String buildGameDataString() {
    Game game = Game.getInstance();
    StringBuilder gameDataBuilder = new StringBuilder();

    // saving the timer
    int time = game.getMessagePanel().getTimer().getTime();
    gameDataBuilder.append(time);
    gameDataBuilder.append("\n");

    // saving the turn counter
    int numTurns = game.numTurns;
    gameDataBuilder.append(numTurns);
    gameDataBuilder.append("\n");

    // saving the token details
    int numOfPlayers = game.getNumberOfPlayers();
    gameDataBuilder.append(numOfPlayers);
    gameDataBuilder.append("\n");
    Token[] tokens = game.getTokens();
    String[] players = game.getPlayerNames();
    for (int i = 0; i < tokens.length; i++) {
      gameDataBuilder.append(players[i] + ":" + tokens[i].getPlayerIndex() + ":" + tokens[i].currentSquare + ":" + tokens[i].getAIStatus());
      gameDataBuilder.append("\n");
    }
    int currentTurn = game.getCurrentTurn();
    gameDataBuilder.append(currentTurn);
    gameDataBuilder.append("\n");
    String userMessage = game.getMessagePanel().getMessage();
    gameDataBuilder.append(userMessage);
    gameDataBuilder.append("\n");

    // saving the deck details
    CardDeck deck = game.getDeck();
    gameDataBuilder.append(deck.toString());
    gameDataBuilder.append("\n");
    boolean cardDrawn = game.getCardDrawn();
    gameDataBuilder.append(cardDrawn);
    gameDataBuilder.append("\n");
    Card currentCard = game.getCardDeckPanel().getCurrentCard();
    if (currentCard != null) {
      gameDataBuilder.append(currentCard.toString());
      gameDataBuilder.append("\n");
    } else {
      gameDataBuilder.append(NO_CARD_DRAWN);
      gameDataBuilder.append("\n");
    }

    // saving gamemode details
    gameDataBuilder.append(game.mode);
    gameDataBuilder.append("\n");
    if(game.mode == game.MODE_STRATEGIC)
    {
      for (int i = 0; i < game.numBoomerangs.length; i++) {
        gameDataBuilder.append(game.numBoomerangs[i]);
        gameDataBuilder.append("\n");
      }
    }


    return gameDataBuilder.toString();
  }
}
