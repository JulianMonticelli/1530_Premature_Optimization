package sweets;

import java.util.ArrayList;

public class SweetState {
    
    // Game state statements
    private boolean paused = false; // Game is paused (no UI update)    
    private boolean finished = false; // Game is finished
    private boolean newGame = false; // Game is new (no players yet, no moves made)
    private Deck deck;
    private ArrayList<BoardSpace> spaces; //List of spaces on the board
    private int playerTurn;
    //To be used as placeholder until method for getting actual names implemented
    private String[] players = {"Bob", "Mel", "Jonah", "Catherine"};
    
    public SweetState() {
        newGame = true;
        deck = new Deck();
        playerTurn = 0;
    }
    
    public int makeTurn() {
    	
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
        ArrayList<String> firstList;
        for (int i = spaces.size(); i >= 0; i--) {
        	firstList = spaces.get(i).getPlayers();
        	if (spaces.get(i).getPlayers().isEmpty() != true) {
        		return firstList;
        	}
        }
    }

    public String getCurrentPlayerTurn() {
        String playerName = players[playerTurn];
        return playerName;
    }

}
