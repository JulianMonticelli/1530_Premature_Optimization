package sweets;

import java.util.ArrayList;
import java.util.Arrays;

public class SweetState {
    
    // Game state statements
    private boolean paused = false; // Game is paused (no UI update)    
    private boolean finished = false; // Game is finished
    private boolean newGame = false; // Game is new (no players yet, no moves made)
    private DeckFactory deckCreator;
    private Deck deck;
    private ArrayList<BoardSpace> spaces; //List of spaces on the board
    private int playerTurn;
    private ArrayList<String> firstPlace;
    //To be used as placeholder until method for getting actual names implemented
    private String[] players = {"Bob", "Mel", "Jonah", "Catherine"};
    
    public SweetState() {
        newGame = true;
        deckCreator = new DeckFactory();
        deck = deckCreator.makeDeck();
        playerTurn = 0;
        firstPlace = new ArrayList<String>(Arrays.asList(players));
    }
    
    public int makeTurn() {
    	return 0;
    }
    
    public void resetGameState() {
        paused = false;
        finished = false;
        newGame = true;
    }
    
    public boolean togglePaused() {
        paused = !paused;
        return paused;
    }
    
    public boolean isPaused() {
        return paused;
    }
    
    public boolean isFinished() {
        return finished;
    }
    
    public boolean isNewGame() {
        return newGame;
    }
    
    public Deck getDeck() {
        return deck;
    }
    
    public ArrayList<String> getPlayerInFirst() {
        return firstPlace;
    }

    public String getCurrentPlayerTurn() {
        String playerName = players[playerTurn];
        return playerName;
    }

}
