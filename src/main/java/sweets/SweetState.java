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
    private DeckFactory deckCreator;
    private Deck deck;
    private ArrayList<BoardSpace> spaces = new ArrayList<BoardSpace>(); //List of spaces on the board
    private int playerTurn;
	private int numPlayers;
    private ArrayList<Player> players;
    private int colorState = 1;
	private Color[] playerColors = { Color.cyan, Color.black, Color.pink, Color.white};
    
    public SweetState() {
        newGame = true;
        deckCreator = new DeckFactory();
        deck = deckCreator.makeDeck();
        playerTurn = 0;
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
			}
			catch (NumberFormatException e) {
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
		}
		colorState = 1;
		
    }
    
	// Returns 0 if someone won; otherwise return 1
    public boolean makeTurn() {
		if (paused){
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
		
			paused = false;
		}
		
    	return true;
    }
	
	public void endGame(Player winner) {
		JOptionPane.showMessageDialog(null, winner.getName() + " has won the game!");
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
	
	//Returns index of destination, or returns -1 when grandma's house is reached
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
					return grandmaLoc;
				else if (spaces.get(i).getIntColorCode() == drawnCard.getColor())
					return i;
			}
		}

		return 0;
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
        
        while(rowDistance < (HEIGHT - yDistance)) // While we have not reached the bottom of the screen (y).
        {
            rowDistance = currentY * yDistance; // Get the current height we want to draw at.
            
			if(pathState == 0) {
				while(columnDistance < (WIDTH - xDistance)) // While we have not reached the edge of the screen (x).
				{
					//g.setColor(colorPick());
					columnDistance = currentX * xDistance; // Get the current width we want to draw at.
					//g.fill3DRect(columnDistance,rowDistance, xDistance, yDistance, true); // Draw a rect at current calculated height and width.
					spaces.add(new BoardSpace(columnDistance,rowDistance, colorPick()));
					currentX++;
				}
				currentY++;
			}
			else
			{
				columnDistance = WIDTH;
				while(columnDistance > 0) // While we have not reached the edge of the screen (x).
				{
					//g.setColor(colorPick());
					columnDistance = WIDTH - xDistance * currentX;
					//g.fill3DRect(columnDistance,rowDistance, xDistance, yDistance, true); // Draw a rect at current calculated height and width.
					//spaces.add(new BoardSpace(columnDistance,rowDistance, colorPick()));
					spaces.add(new BoardSpace(columnDistance,rowDistance, colorPick()));
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
                    spaces.add(new BoardSpace(columnDistance,rowDistance, colorPick()));
                    pathState = 1;
                }
                else
                {
                    //g.fill3DRect(0,rowDistance, xDistance, yDistance, true);
                    spaces.add(new BoardSpace(0, rowDistance, colorPick()));
                    pathState = 0;
                }
                
            }
            // Move down in y and reset x variable to move left to right again.
            currentY++;
            currentX = 0;
            columnDistance = 0;
        }

        return spaces;
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
            return Color.MAGENTA; 
        }
        else if(colorState == 1)
        {
            colorState = 2;
            return Color.red;   
        }
        else if(colorState == 2)
        {
            colorState = 3;
            return Color.green;
        }
        else if(colorState == 3)
        {
            colorState = 4;
            return Color.orange;
        }
        else if(colorState == 4)
        {
            colorState = 5;
            return Color.blue;
        }
        else
        {
            colorState = 0;
            return Color.yellow;
        }   
    }
}
