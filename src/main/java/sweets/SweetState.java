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
				done = true;
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
    
	// Returns 1 if a turn was taken; otherwise return 0
    public int makeTurn() {
		if (paused){
			// TO DO: Move game pieces
			deck.draw();
			
			startNextTurn();
		
			paused = false;
		}
		
		
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
	
	// TO DO: Use actual player count instead of always having 4 players
	public int startNextTurn() {
                playerTurn = ++playerTurn % players.size();
			
		return playerTurn;
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
            
            while(columnDistance < (WIDTH - xDistance)) // While we have not reached the edge of the screen (x).
            {
                //g.setColor(colorPick());
                columnDistance = currentX * xDistance; // Get the current width we want to draw at.
                //g.fill3DRect(columnDistance,rowDistance, xDistance, yDistance, true); // Draw a rect at current calculated height and width.
                spaces.add(new BoardSpace(columnDistance,rowDistance, colorPick()));
                currentX++;
            }
            currentY++;
            
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
