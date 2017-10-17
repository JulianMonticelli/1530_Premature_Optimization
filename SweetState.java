public class SweetState {
    
    Player[] players;
    
    
    
    
    // Game state statements
    private boolean paused = false; // Game is paused (no UI update)    
    private boolean finished = false; // Game is finished
    private boolean newGame = false; // Game is new (no players yet, no moves made)
    private Player firstPlace = null; // Player who is in first place, who can be displayed as the current leader
    
    public SweetState() {
        newGame = true;
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
    
}