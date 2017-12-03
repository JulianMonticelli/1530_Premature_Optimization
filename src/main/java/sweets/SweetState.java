package sweets;

import java.util.ArrayList;
import java.awt.Color;
import java.io.Serializable;
import java.io.*;
//import javax.swing.JOptionPane;

public class SweetState implements Serializable {
    // This needs to exist to allow serializability
    private static final long serialVersionUID = 489021829075290170L;

    // Game state statements
    private boolean paused = false; // Game is paused (no UI update)
    private boolean finished = false; // Game is finished
    private boolean newGame = false; // Game is new (no players yet, no moves made)

    private boolean deckClicked = false; // The variable for determining if the deck was clicked
    private boolean boomerangClicked = false; // The variable for determining if the current player's boomerang icon was clicked
    private int boomerangTarget = -1; // The variable for the target of a boomerang throw. -1 if no target selected
    private boolean waitingForTarget = false; //The variable for determining if we need to wait for a boomerang target
    private int selectedPlayer = -1; // Equal to the player id when that player's token is clicked

    private ArrayList<BoardSpace> spaces = new ArrayList<BoardSpace>(); //List of spaces on the board
    private ArrayList<Player> players;

    private DeckFactory deckCreator;
    private Deck deck;

    private int playerTurn;
    private int numPlayers;

    // Multi-threaded Timer
    private transient MultithreadedTimer mtTimer;
  
    private int timeInSeconds;
    
    // Warning system
    WarningManager warningManager;

    public boolean randomSpaces = false;

    // I changed this variable to make more sense to callers
    private boolean gameModeIsStrategicMode = true;

    public void setGameModeIsStrategicMode(boolean isStrategicMode) {
        this.gameModeIsStrategicMode = isStrategicMode;
    }

    public boolean isGameModeStrategicMode() {
        return gameModeIsStrategicMode;
    }
  
    public void applySavedTime() {
        initializeTimer();
        mtTimer.setTimeInSeconds(timeInSeconds);
    }
    
    private int colorState = 3;
    private int specialSpaces[] = {-1,-1,-1,-1,-1}; // This array holds the indexes into the board of the special squares
	private int grandmaLoc = -1;
    // 0 = iceCreamImage
    // 1 = chocolateBar
    // 2 = candyCane
    // 3 = lollipop
    // 4 = candy

    private int currentFrame = 0;
    private int decisionFrames = -1;

    public SweetState() {
        newGame = true;
        deckCreator = new DeckFactory();
        deck = deckCreator.makeDeck();
        deck.addSweetState(this);
        playerTurn = 0;

        warningManager = WarningManager.getInstance();

    }

    public void initializeTimer() {
        // Initialize timer and start thread
        mtTimer = new MultithreadedTimer();
        mtTimer.startThread();
    }

    public void clickDeck() {
        deckClicked = true;
    }

	public boolean isDeckClicked() {
		return deckClicked;
	}

	public void clickBoomerang() {
            if (!boomerangClicked) {
                boomerangClicked = true;
                WarningManager.getInstance().createWarning("Click on a player token", Warning.TYPE_INFORMATION);
            }
        }

	public boolean isBoomerangClicked() {
		return boomerangClicked;
	}

	public boolean isPlayerTargetted() {
		if (boomerangTarget != -1)
			return true;
		else
			return false;
	}

	public void clickPlayer(int playerNumber) {
            selectedPlayer = playerNumber;
        }

	public boolean isPlayerClicked() {
		if (selectedPlayer != -1)
			return true;
		else
			return false;
	}

	public int generateRandomNum(int min, int max)
	{
		return min + (int)(Math.random() * max);
	}

	public int getSelectedPlayer() {
		return selectedPlayer;
	}

	public boolean aiMakeTurn()
	{
		if(decisionFrames == -1)
		{
			decisionFrames  = generateRandomNum(30, 100);
		}
		else
		{
			currentFrame++;
		}

		if(currentFrame == decisionFrames)
		{
			decisionFrames = -1;
			currentFrame = 0;
		}
		else
		{
			return true;
		}


		Player cP = players.get(playerTurn);


		// A boomerang was just thrown and no target is selected
		if (generateRandomNum(0,5) > 2 && boomerangTarget == -1) // Simulate a boomarang click
		{
			if (cP.getBoomerangCount() > 0) {
				System.out.println("AI Throwing boomerang, waiting for target token to be selected");
				waitingForTarget = true;
				boomerangTarget = -1;

			} else {
				System.out.println("AI Attempted to throw boomerang when player had no boomerangs left");
			}

			selectedPlayer = playerTurn;
			while(selectedPlayer == playerTurn)
			{
				selectedPlayer = generateRandomNum(0,numPlayers);
				boomerangTarget = selectedPlayer;
				waitingForTarget = false;

			}

		}


		// Waiting for target, meaning we are waiting for player to select another player's token


		boolean isWinningMove = false;

		if (boomerangTarget == -1)
		{
			isWinningMove = drawAndMove(cP, false);

		} else
		{
			isWinningMove = drawAndMove(players.get(boomerangTarget), true);
			cP.throwBoomerang();
			boomerangTarget = -1;
		}

		if (isWinningMove)
		{
			endGame(cP);
			return false;
		}

		startNextTurn();

		selectedPlayer = -1;
		deckClicked = false;
		boomerangClicked = false;
		return true;
	}

    // Returns false if someone won; otherwise return true
    public boolean makeTurn() {
        Player cP = players.get(playerTurn);

        if (!waitingForTarget) {
            // A boomerang was just thrown and no target is selected
            if (boomerangClicked && boomerangTarget == -1) {
                if (cP.getBoomerangCount() > 0) {
                    System.out.println("Throwing boomerang, waiting for target token to be selected");
                    waitingForTarget = true;
                    boomerangTarget = -1;

                } else {
                        System.out.println("Attempted to throw boomerang when player had no boomerangs left");
                }

            } 

            if (isDeckClicked()) {
                boolean isWinningMove = false;

                if (boomerangTarget == -1) {
                    isWinningMove = drawAndMove(cP, false);

                } else  {
                    isWinningMove = drawAndMove(players.get(boomerangTarget), true);
                    cP.throwBoomerang();
                    boomerangTarget = -1;
                    selectedPlayer = -1;
                }

                if (isWinningMove) {
                    endGame(cP);
                    return false;
                }

                startNextTurn();
            }

        // Waiting for target, meaning we are waiting for player to select another player's token
        } else if (isPlayerClicked()){ 
            if (selectedPlayer != playerTurn) {
                boomerangTarget = selectedPlayer;
                waitingForTarget = false;

            } else {
                System.out.println("Player selected their own token as a boomerang target. Waiting until they select someone else's");
                selectedPlayer = -1;
            }

        }

        //selectedPlayer = -1;
        deckClicked = false;
        boomerangClicked = false;
        return true;
    }

	// Returns true if player move resulted in win; otherwise return false
    public boolean drawAndMove(Player currentPlayer, boolean isReverseMove) {

        Card drawnCard = null;
        if (currentPlayer.isDad()) {
            drawnCard = deck.dadDraw(currentPlayer.getPos());
        } else {
            drawnCard = deck.draw();
        }
        int currentPos;
        int destPos;

        currentPos = currentPlayer.getPos();
        if (!isReverseMove)
            destPos = calculateDest(currentPos, drawnCard);
        else
            destPos = calculateReverseDest(currentPos, drawnCard);

        movePlayer(currentPlayer, currentPos, destPos);

        if (destPos == grandmaLoc)
            return true;
        else
            return false;
    }

    // Returns destination
    public int movePlayer(Player p, int startLoc, int endLoc) {
        spaces.get(startLoc).removePlayer(p);
        spaces.get(endLoc).addPlayer(p);

        p.setPos(endLoc);

        System.out.println(p.getName() + " going from " + startLoc + " to " + endLoc);

        return endLoc;
    }
	
    // To be used to avoid doubling Timer Threads
    public void killTimerThread() {
        mtTimer.killThread();
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

    public boolean saveState(File file) {
        paused = true; // Assure that the game is paused.
        
        // If the file is a null file (say the user cancelled the save)
        // then resume the game and don't worry about saving anything.
        if (file == null) {
            togglePaused();
            return false;
        }
        
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        try {
             // Set timeInSeconds to time in timer
            timeInSeconds = mtTimer.getTimeInSeconds();
            
            // Create FOS and OOS 
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            // Create a statefile, which will get a messageDigest of the
            // GameState method, and write it to file
            StateFile sf = new StateFile(this);
            oos.writeObject(sf);
            
            // Close OOS and FFS and return true
            oos.close();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("File not found. This should have been caught earlier!");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }
    
    public boolean togglePaused() {
        paused = !paused;
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
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

    public Player getCurrentPlayer() {
        return players.get(playerTurn);
    }

    public ArrayList<BoardSpace> getPath()
    {
        return spaces;
    }

	public int startNextTurn() {
            playerTurn = ++playerTurn % players.size();

            return playerTurn;
	}

	public int addPlayers(ArrayList<Player> playersInfo) {
		players = playersInfo;
		numPlayers = playersInfo.size();

		//Add tokens to board
		try {
			for (int i = 0; i < numPlayers; i++) {
					spaces.get(0).addPlayer(players.get(i));
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Board has not been created yet!");
			return -1;
		}
            return 0;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	//Returns index of destination
	public int calculateDest(int startPos, Card drawnCard) {

            int destination = startPos;
			int specialNum = drawnCard.getSpecialMoveNumber();

            if (drawnCard.isSkipTurn())
                return startPos;

			if (specialNum != -1)
				return specialSpaces[specialNum];

            //Middle cards have been removed
			/*
			else if (drawnCard.isMiddleCard())
                return (spaces.size() - 3)/2;
			*/

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

			// If we haven't returned yet, that means we have reached grandma's house
            return grandmaLoc;
	}

	// Returns index of destination
	public int calculateReverseDest(int startPos, Card drawnCard) {

            int destination = startPos;
			int specialNum = drawnCard.getSpecialMoveNumber();

            if (drawnCard.isSkipTurn())
                return startPos;

			if (specialNum != -1)
				return specialSpaces[specialNum];

            //Middle cards have been removed
			/*
			else if (drawnCard.isMiddleCard())
                return (spaces.size() - 3)/2;
			*/

            else if (drawnCard.isDouble()) {
                boolean hasPassedMatchingSquare = false;
                for (int i = startPos - 1; i > -1; i--) {
                    if (spaces.get(i).getIntColorCode() == drawnCard.getColor()) {
                        if (hasPassedMatchingSquare)
                            return i;
                        else
                            hasPassedMatchingSquare = true;
                    }
                }

            } else {
                for (int i = startPos - 1; i > -1; i--) {
                    if (spaces.get(i).getIntColorCode() == drawnCard.getColor())
                        return i;
                }
            }

            // If we haven't returned i yet, that means we have reached the first square
			return 0;
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

		if(randomSpaces == true)
        {
             specialSpaces = pickSpecialSpaces(specialSpaces, 2, 49);
        }
        else
        {
            specialSpaces[0] = 5;
            specialSpaces[1] = 15;
            specialSpaces[2] = 25;
            specialSpaces[3] = 35;
            specialSpaces[4] = 45;

        }


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
		grandmaLoc = spaces.size() - 3;
        return spaces;
    }

	public int getGrandmaLoc() {
		return grandmaLoc;
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

	public int[] getSpecialSpaces() {
		return specialSpaces;
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

	public MultithreadedTimer setMTTimer(MultithreadedTimer m) {
            mtTimer = m;
            return mtTimer;
	}

	public String getTime() {
            return mtTimer.getTimerString();
	}

    public WarningManager getWarningManager() {
        return warningManager;
    }

}
