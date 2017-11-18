package sweets;

import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Color;
import javax.swing.JOptionPane;

public class SweetState {
    

    // Game state statements
    private boolean paused = false; // Game is paused (no UI update)    
    private boolean finished = false; // Game is finished
    private boolean newGame = false; // Game is new (no players yet, no moves made)
    
    private boolean deckClicked = false; // The variable for determining if the deck was clicked

    
    private ArrayList<BoardSpace> spaces = new ArrayList<BoardSpace>(); //List of spaces on the board
    private ArrayList<Player> players;

    private DeckFactory deckCreator;
    private Deck deck;

    private int playerTurn;
    private int numPlayers;
    
    // Multi-threaded Timer
    private MultithreadedTimer mtTimer;
    
    // Warning system
    WarningManager warningManager;
    
    private int colorState = 3;
    private int specialSpaces[] = {-1,-1,-1,-1,-1}; // This array holds the indexes into the board of the special squares
    // 0 = iceCreamImage
    // 1 = chocolateBar
    // 2 = candyCane
    // 3 = lollipop
    // 4 = candy
    
	private Color[] playerColors = { Color.cyan, Color.black, Color.pink, Color.white};
    
    public SweetState() {
        newGame = true;
        deckCreator = new DeckFactory();
        deck = deckCreator.makeDeck();
        playerTurn = 0;
        
        warningManager = WarningManager.getInstance();
        
        boolean done = false;
        
        while (!done) {
            
            try {
                
                String input = JOptionPane.showInputDialog("How many players are playing?");
                numPlayers = Integer.parseInt(input);
                
                if (numPlayers < 5 && numPlayers > 1)
                    done = true;
                else {
                    JOptionPane.showMessageDialog(null, "Please enter a number between 2 and 4");
                }
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid Input");
            }
        }
        players = new ArrayList<Player>(numPlayers);
        
        for (int i = 0; i < numPlayers; i++) {
            
            String playerName = JOptionPane.showInputDialog("What is player" + (i + 1) + "'s name?");
            
            for (int j = 0; j < i; j++) {
                if (playerName.equals(players.get(j).getName())) {
                    playerName = JOptionPane.showInputDialog("Please enter a unique name");
                }
            }
            
            players.add(i, new Player(colorPick(), playerName, 0));
            colorState = 0;
            
        }
        
        
        // Initialize timer and start thread
        mtTimer = new MultithreadedTimer();
        mtTimer.startThread();
		
    }
    
    public void clickDeck() {
        deckClicked = true;
    }
    
    // Returns 0 if someone won; otherwise return 1
    public boolean makeTurn() {
        if (deckClicked){
            Player currentPlayer = players.get(playerTurn);
            
            int currentPos = currentPlayer.getPos();
            int destPos = calculateDest(currentPos);
            
            System.out.println(currentPlayer.getName() + " going from " + currentPos + " to " + destPos);

            spaces.get(currentPos).removePlayer(currentPlayer);
            spaces.get(destPos).addPlayer(currentPlayer);
            
            currentPlayer.setPos(destPos);

            int grandmaLoc = spaces.size() - 3;

            if (destPos == grandmaLoc) {
                endGame(currentPlayer);
                return false;
            }

            startNextTurn();

            deckClicked = false;
        }
		
    	return true;
    }
	
    public void endGame(Player winner) {
        // Hacky solution. Creates a warning. These are made to be animated warnings, that
        // fade and travel up the screen.
        WarningManager.getInstance().createWarning("Game Over", Warning.TYPE_ENDGAME, 400, 475);
        mtTimer.killThread();
        
        // Display some sort of "Play another game?" message?
    }
    
    public void resetGameState() {
        paused = false;
        finished = false;
        newGame = true;
        warningManager.clearWarningList();
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
        ArrayList<String> firstPlace = new ArrayList<String>(numPlayers);
            int maxPos = 0;
            for (int i = 0; i < numPlayers; i++) {
                if (players.get(i).getPos() >= maxPos) {
                    if (players.get(i).getPos() > maxPos) {
                        firstPlace.clear();
                        maxPos = players.get(i).getPos();
                    }
                    firstPlace.add(players.get(i).getName());
                }
            }
		
        return firstPlace;
    }

    public String getCurrentPlayerTurn() {
        String playerName = players.get(playerTurn).getName();
        return playerName;
    }

    public ArrayList<BoardSpace> getPath()
    {
        return spaces;
    }
	
	public int startNextTurn() {
            playerTurn = ++playerTurn % players.size();

            return playerTurn;
	}
	
	public int addTokensToBoard() {
            for (int i = 0; i < numPlayers; i++) {
                //players.get(i).setPos(0);
                players.get(i).setColor(playerColors[i]);
                spaces.get(0).addPlayer(players.get(i));
            }

            return 0;
	}
	
	//Returns index of destination
	public int calculateDest(int startPos) {
            Card drawnCard = deck.draw();
            int destination = startPos;
            int grandmaLoc = spaces.size() - 3;

            if (drawnCard.isSkipTurn())
                return startPos;

            else if (drawnCard.isMiddleCard())
                return (spaces.size() - 3)/2;

            else if (drawnCard.isDouble()) {
                boolean hasPassedMatchingSquare = false;
                for (int i = startPos + 1; i < grandmaLoc + 1; i++) {
                    if (spaces.get(i).getIntColorCode() == drawnCard.getColor()) {
                        if (i == grandmaLoc)
                            return grandmaLoc;
                        else if (hasPassedMatchingSquare)
                            return i;
                        else
                            hasPassedMatchingSquare = true;
                    }
                }
            } else {
                for (int i = startPos + 1; i < grandmaLoc + 1; i++) {
                    if (i == grandmaLoc)
                    {
                       
                        return grandmaLoc;
                    }
                    else if (spaces.get(i).getIntColorCode() == drawnCard.getColor())
                    {

                        return i;
                    }
                }
            }

            return grandmaLoc;
	}

	/**
	*  Gets the index of a special square
	**/
	public int searchForSpecialSquare(int[] specials, int index)
	{

		for(int i = 0; i < specials.length;i++)
		{
			if(index == specials[i])
			{
				
				return i;
			}
		}

		return -1;
	}

    /**
     * Generates a zig-zag box pattern for the CandyLand path.
     * It works by drawing boxes from right to left across the screen,
     * then drawing a bridge box. It continues this process until 
     * it reaches the bottom of the screen. Then stores the result
     * in the spaces array.
     * @param The graphics object used to draw to the JPanel
     * @return Success or failure code;
     */
    public ArrayList<BoardSpace> storePath(int WIDTH, int HEIGHT)
    {
        // Current x and y keep track of our x and y indexes into the Jpanel
        int currentX = 0;
        int currentY = 0;
        
        // X and Y distance are multiplied by the current x or y index to get the total distance for the origin of the rectangle to be drawn.
        int xDistance = WIDTH/10;
        int yDistance = HEIGHT/10;
        
        // The variables that will hold distance times current
        int rowDistance = 0;
        int columnDistance = 0;
        
        // Path state determines whether we should draw a bridge on near x or far x side of the window.
        int pathState = 0; 
        specialSpaces = pickSpecialSpaces(specialSpaces, 2, 49);
        while(rowDistance < (HEIGHT - yDistance)) // While we have not reached the bottom of the screen (y).
        {
            rowDistance = currentY * yDistance; // Get the current height we want to draw at.
            
            if(pathState == 0) 
            {
                while(columnDistance < (WIDTH - xDistance)) // While we have not reached the edge of the screen (x).
                {
                    //g.setColor(colorPick());
                    columnDistance = currentX * xDistance; // Get the current width we want to draw at.
                    //g.fill3DRect(columnDistance,rowDistance, xDistance, yDistance, true); // Draw a rect at current calculated height and width.
                    int searchValue = searchForSpecialSquare(specialSpaces, spaces.size());
                    
                    if(searchValue == -1)
                    {
                    	spaces.add(new BoardSpace(columnDistance,rowDistance, colorPick()));
                    }
                    else
                    {
                    	spaces.add(new BoardSpace(columnDistance,rowDistance, Color.black));
                    	spaces.get(spaces.size()-1).specialNum = searchValue;
                    }
                    
                    currentX++;
                }
                currentY++;
            }
            else
            {
                columnDistance = WIDTH;
                currentX = 1;
                while(columnDistance > 0) // While we have not reached the edge of the screen (x).
                {
                    
                    //g.setColor(colorPick());
                    columnDistance = WIDTH - xDistance * currentX;
                    //g.fill3DRect(columnDistance,rowDistance, xDistance, yDistance, true); // Draw a rect at current calculated height and width.
                    //spaces.add(new BoardSpace(columnDistance,rowDistance, colorPick()));
                    int searchValue = searchForSpecialSquare(specialSpaces, spaces.size());
                    if(searchValue == -1)
                    {
                    	spaces.add(new BoardSpace(columnDistance,rowDistance, colorPick()));
                    }
                    else
                    {
                    	spaces.add(new BoardSpace(columnDistance,rowDistance, Color.black));
                    	spaces.get(spaces.size()-1).specialNum = searchValue;
                    }
                    currentX++;
                }
                currentY++;
            }
            
            // After we have drawn a row we need to draw a row of size one so move down one y index.
            rowDistance = currentY * yDistance;
            
            if(rowDistance < HEIGHT - yDistance) // Make sure we are not off the screen in the y direction
            {
                
                // Alternate drawing the bridge path on the right and left.
                if(pathState == 0)
                {
                    //g.fill3DRect(columnDistance,rowDistance, xDistance, yDistance, true);
                     int searchValue = searchForSpecialSquare(specialSpaces, spaces.size());
                    if(searchValue == -1)
                    {
                    	spaces.add(new BoardSpace(columnDistance,rowDistance, colorPick()));
                    }
                    else
                    {
                    	spaces.add(new BoardSpace(columnDistance,rowDistance, Color.black));
                    	spaces.get(spaces.size()-1).specialNum = searchValue;
                    }
                    pathState = 1;
                }
                else
                {
                    //g.fill3DRect(0,rowDistance, xDistance, yDistance, true);
                    int searchValue = searchForSpecialSquare(specialSpaces, spaces.size());
                    if(searchValue == -1)
                    {
                    	spaces.add(new BoardSpace(columnDistance,rowDistance, colorPick()));
                    }
                    else
                    {
                    	
                    	spaces.add(new BoardSpace(0,rowDistance, Color.black));
                    	spaces.get(spaces.size()-1).specialNum = searchValue;
                    }
                    pathState = 0;
                }
                
            }
            // Move down in y and reset x variable to move left to right again.
            currentY++;
            currentX = 0;
            columnDistance = 0;
        }

       
        
        System.out.println(specialSpaces[0] + " " + specialSpaces[1] + " " + specialSpaces[2] + " " + specialSpaces[3] + " " + specialSpaces[4] + " ");        
        return spaces;
    }

    /**
     * This function picks random spaces for the
     * special candy spaces and stores them in an array
     * @param specials The array of ints representing special blocks.
     * @param min Lower bound of random numbers
     * @param max upper bound of random numbers
     * @return The array containing the special block int
     */
    public int[] pickSpecialSpaces(int[] specials, int min, int max)
    {
    	int randomNum = -1;
    	
    	
    	for(int i = 0; i < specials.length;i++)
    	{
    		
    		do
    		{
    			randomNum = min + (int)(Math.random() * max); 	
    		}while(!validNum(specials,randomNum, i));

    		specials[i] = randomNum;
    	}

    	return specials;
    }

    /**
     * This funtion verifies that a sppace is an 
     * acceptable distance from another space to
     * ensure that they do not appear together
     * @param specials The array of ints representing special blocks.
     * @param number The generated number
     * @param index The index of insertion
     * @return Whether this number is acceptable
     */
    public boolean validNum(int[] specials, int number, int index)
    {
    	for(int i = 0; i < specials.length;i++)
    	{
    		if(specials[i] == -1)
    		{
    			return true;
    		}

    		if(i == index)
    		{
    			continue;
    		}
    		else
    		{
    			if( Math.abs(specials[i] - number) < 5)
    			{
    				return false;
    			}
    		}  
    	}

    	return true;  	
    }
    
    /**
     * This function assigns returns 
     * a color based on the current colorstate
     * @return The color to be applied.
     */
    private Color colorPick()
    {
        if(colorState == 0)
        {
            colorState = 1;
            return Color.red; 
        }
        else if(colorState == 1)
        {
            colorState = 2;
            return Color.yellow;   
        }
        else if(colorState == 2)
        {
            colorState = 3;
            return Color.blue;
        }
        else if(colorState == 3)
        {
            colorState = 4;
            return Color.green;
        }
        else
        {
            colorState = 0;
            return Color.orange;
        }   
    }
    
    public MultithreadedTimer getMultithreadedTimer() {
        return mtTimer;
    }
    
    public WarningManager getWarningManager() {
        return warningManager;
    }
    
}
