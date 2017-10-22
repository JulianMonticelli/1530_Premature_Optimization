package sweets;

public class SweetState {
    
    Player[] players;
    
    
    
    
    // Game state statements
    private boolean paused = false; // Game is paused (no UI update)    
    private boolean finished = false; // Game is finished
    private boolean newGame = false; // Game is new (no players yet, no moves made)
    private Player firstPlace; // Player who is in first place, who can be displayed as the current leader
    private Deck deck;
    
    public SweetState() {
        newGame = true;
        deck = new Deck();
    }
    
    public void resetGameState() {
        players = null; // Null Player List
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
    
}
