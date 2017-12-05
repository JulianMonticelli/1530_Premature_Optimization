package sweets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;


public class WorldOfSweets extends JPanel {    
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;


    ArrayList<BoardSpace> path;
    
    private WarningManager wm;
    
    private HUD hud;
    private SweetState gameState;

    private int targetFPS = 30;
	
    private boolean running = false;
    //private int colorState = 1;

    private BufferedImage backgroundImage;
    private BufferedImage grandmasHouseImage;

    private BufferedImage iceCreamImage;
    private BufferedImage chocolateBarImage;
    private BufferedImage candyCaneImage;
    private BufferedImage lollipopImage;
    private BufferedImage candyImage;
    
    private static Font PAUSED_FONT = new Font("Verdana", Font.PLAIN|Font.BOLD, 72);
    private static final Color PAUSED_COLOR_1 = Color.RED;
    private static final Color PAUSED_COLOR_2 = Color.WHITE;
    private static final Color PAUSED_COLOR_3 = Color.BLACK;
    
    private static final int RADIUS_OF_TOKEN_HIGHLIGHTING = 7;
    
    private static final int PAUSED_STRING_X_OFFSET = WIDTH/2-250;
    private static final int PAUSED_STRING_Y_OFFSET = HEIGHT/2;
    
    private static final String GAME_PAUSED = "Game Paused";

    

    
    
    public WorldOfSweets() {
        
        
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT)); // Preferred size affects packing
        this.setFocusable(true); // Focusable so we can use keyboard input
        this.addKeyListener(initKeyAdapter()); // Listens to keys, obv 
        this.addMouseListener(initMouseListener());
        try {
            backgroundImage = ImageIO.read(new File(Main.getAssetLocale() + "background.jpg"));
            grandmasHouseImage = ImageIO.read(new File(Main.getAssetLocale() + "grandma's.jpg"));
            iceCreamImage = ImageIO.read(new File(Main.getAssetLocale() + "icon_icecream.png"));
            chocolateBarImage = ImageIO.read(new File(Main.getAssetLocale() + "icon_chocolate.png"));
            candyCaneImage = ImageIO.read(new File(Main.getAssetLocale() + "icon_candycane.png"));
            lollipopImage = ImageIO.read(new File(Main.getAssetLocale() + "icon_lollipop.png"));
            candyImage = ImageIO.read(new File(Main.getAssetLocale() + "icon_candy.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Prompt to load a game from a saved file
        gameState = getSweetStateFromLoad(true);
        
        
        hud = new HUD(WIDTH, HEIGHT);
        wm = new WarningManager(hud.getWidthRatio(), hud.getHeightRatio());
                
        if (gameState == null) {
            gameState = new SweetState();
            boolean gameModeIsStrategic = pickGameMode();
            chooseSpecialSpacePickMode();
            gameState.storePath(WIDTH,HEIGHT);
            gameState.addPlayers(getPlayerCountAndNames(gameModeIsStrategic));
            // WARNING: IF you change any of the code in this method remember:
            // INITIALIZING THE TIMER SHOULD HAPPEN L A S T!
            gameState.initializeTimer();
        }
        
    
        running = true;
    }
    
    public SweetState getSweetStateFromLoad(boolean showLoadConfirmation) {
        // Initially, this is set to yes because if we skip showing load confirmation
        // then, we want to have it automatically be yes.
        int option = JOptionPane.YES_OPTION; 
        
        if (showLoadConfirmation) {
            option = JOptionPane.showOptionDialog(null,  
                                                    "Do you want to load an existing game?",  
                                                    "Start", JOptionPane.YES_NO_OPTION,  
                                                    JOptionPane.WARNING_MESSAGE, null, null,  
                                                    null
                                                 );
        }
        
        if (option == JOptionPane.YES_OPTION) {
            SweetState loadState = loadState(selectLoadFile());
            if (loadState == null) {
                int loadCancelOption = JOptionPane.showOptionDialog(null,
                                                    "It appears you have cancelled loading a save file.\n"
                                                   +"This will, then, start a new game. Are you sure you wish"
                                                   +" to cancel loading?",
                                                    "Load cancelled!",
                                                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                                                    null, null, null);
                if (loadCancelOption == JOptionPane.YES_OPTION) {
                    return null;
                } else {
                    loadState = getSweetStateFromLoad(false);
                }
            }
            return loadState;
        } else {
            return null;
        }
    }

    public boolean pickGameMode()
    {
        int option;
        
        option = JOptionPane.showOptionDialog(null,  
                                           "Do you want to play in strategic mode?",  
                                            "Start", JOptionPane.YES_NO_OPTION,  
                                            JOptionPane.WARNING_MESSAGE, null, null,  
                                            null);
                
        if (option == JOptionPane.YES_OPTION) 
        {
            gameState.setGameModeIsStrategicMode(true);
            return true;
        }
        else if (option == JOptionPane.NO_OPTION) 
        {
            gameState.setGameModeIsStrategicMode(false);
        }
        return false;
    }
	
    public WorldOfSweets(ArrayList<Player> p) {
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT)); // Preferred size affects packing
        this.setFocusable(true); // Focusable so we can use keyboard input
        this.addKeyListener(initKeyAdapter()); // Listens to keys, obv 
        this.addMouseListener(initMouseListener());
        try {
            backgroundImage = ImageIO.read(new File(Main.getAssetLocale() + "background.jpg"));
            grandmasHouseImage = ImageIO.read(new File(Main.getAssetLocale() + "grandma's.jpg"));
            iceCreamImage = ImageIO.read(new File(Main.getAssetLocale() + "icon_icecream.png"));
            chocolateBarImage = ImageIO.read(new File(Main.getAssetLocale() + "icon_chocolate.png"));
            candyCaneImage = ImageIO.read(new File(Main.getAssetLocale() + "icon_candycane.png"));
            lollipopImage = ImageIO.read(new File(Main.getAssetLocale() + "icon_lollipop.png"));
            candyImage = ImageIO.read(new File(Main.getAssetLocale() + "icon_candy.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        hud = new HUD(WIDTH, HEIGHT);
        gameState = new SweetState();
        gameState.storePath(WIDTH,HEIGHT);
        gameState.addPlayers(p);
		
        running = true;
    }
	
    public File selectLoadFile() {
            JFrame frame = new JFrame("Select file...");
            JFileChooser jfc = new JFileChooser();
            
            // Filter only for .wosv files
            FileNameExtensionFilter fef = new FileNameExtensionFilter("World of"
                                        +" Sweets save files (*.wosv)", "wosv");
            jfc.setFileFilter(fef);
            frame.add(jfc);
            
            
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(false);
            
            File file = null;
            
            if(jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                file = jfc.getSelectedFile();
                // If the file isn't the appropriate filetype, prompt again to 
                // select a file after dumping a warning.
                if(!file.getPath().endsWith(".wosv")) {
                    JOptionPane.showMessageDialog(null, "You cannot select a "
                          + "file that does not have a .wosv extension!", 
                            "Wrong filetype!", JOptionPane.WARNING_MESSAGE);
                    file = selectLoadFile(); 
                }
                frame.setVisible(false);
                frame.setEnabled(false);
            } else {
                frame.setVisible(false);
                frame.setEnabled(false);
            }
            return file;					
    }
    
    public File selectSaveFile() { 
        gameState.setPaused(true);
        JFrame frame = new JFrame("Select file...");
        JFileChooser jfc = new JFileChooser();
        
        FileNameExtensionFilter fef = new FileNameExtensionFilter("World of"
                                    +" Sweets save files (*.wosv)", "wosv");
        jfc.setFileFilter(fef);
        
        jfc.setApproveButtonText("Save");
        
        frame.add(jfc);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(false);
        File file = null;

        if(jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            file = jfc.getSelectedFile();
            // If the file isn't the appropriate filetype, prompt again to 
            // select a file after dumping a warning.
            if(!file.getPath().endsWith(".wosv")) {
                file = new File(file.getPath() + ".wosv");
            }
            frame.setVisible(false);
            frame.setEnabled(false);
        } else {
            frame.setVisible(false);
            frame.setEnabled(false);
        }
        return file;
    }
    
    
    public void saveStateFile() {
        gameState.saveState(selectSaveFile());
        gameState.setPaused(false);
    }
    
    public void loadStateFile() {
        gameState.setPaused(true);
        SweetState loadedState = loadState(selectLoadFile());
        if (loadedState != null) {
            gameState.killTimerThread();
            gameState = loadedState;
        }
    }
	
    public SweetState loadState(File file) {
        // First, and foremost, catch a null file.
        if (file == null) {
            return null;
        }
        
        StateFile sf = null;
        SweetState state = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            sf = (StateFile) ois.readObject();
            ois.close();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "The save file has been either"
                                        + " structurally corrupted or does not"
                                        + " exist on the filesystem.",
                                            "Save file missing or corrupted",
                                            JOptionPane.WARNING_MESSAGE);
            return null;
        }
        
        if (sf == null) {
            return null;
        }
        
        if (sf.verifyContent()) {
            state = sf.getGameState();
            if (state != null) {            
                state.applySavedTime();
                state.setPaused(false);
            }
            return state;
        } else {
            JOptionPane.showMessageDialog(null, "The save file has been corrupted."
                                          + "\nFor the integrity of World of Sweets,"
                                          + " we cannot load this file.",
                                            "Corrupt save file",
                                            JOptionPane.WARNING_MESSAGE);
            return null;
        }
        
    }

    public void chooseSpecialSpacePickMode()
    {
        int option = JOptionPane.showOptionDialog(null,  
            "Do you want to randomize locations for special squares?",  
            "Start", JOptionPane.YES_NO_OPTION,  
            JOptionPane.WARNING_MESSAGE, null, null,  
            null
        );

        
                
        if (option == JOptionPane.YES_OPTION) 
        {
           gameState.randomSpaces = true;
        }
        else if (option == JOptionPane.NO_OPTION) 
        {
           gameState.randomSpaces = false;
        }
    }    

    public void run() {

        while (running) {
            // Target time is always recalculated in case we want to switch frame rate
            long targetTime = (1000L/targetFPS);
            long startTime = System.currentTimeMillis();

            

            // Perform turn
            tick();

            // Redraw screen
            repaint();

            // Game pause loop
            if (gameState.isPaused()) {
                // If we've paused the game, pause the timer thread
                gameState.getMultithreadedTimer().pauseThread();

                // Repaint while paused and a paused overlay will be drawn
                repaint();

                while(gameState.isPaused()) {
                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException e) {
                        e.printStackTrace(); // If something goes wrong here, we really want to know what happened.
                    }
                }

                // When we unpause the game, unpause the timer thread
                gameState.getMultithreadedTimer().unpauseThread();
                
            }


                // Frame limiting code
                long totalSleepTime = targetTime - (System.currentTimeMillis() - startTime);
                if (totalSleepTime > 0) {
                    try {
                            Thread.sleep(totalSleepTime);
                    } catch (InterruptedException e) {
                            e.printStackTrace(); // This shouldn't happen - but in case it does throw an error!
                    }
                }
            
        }
    }


    public void tick() {
        //System.out.println(identityHashCode(gameState));
        // Make player turn
        if(gameState.getCurrentPlayer().getIsAI())
        {
             running = gameState.aiMakeTurn();
        }
        else
        {
             running = gameState.makeTurn();
        }

       
        
        // Update HUD 
        hud.update(gameState);
        
        // Update WarningSystem
        WarningManager.getInstance().update();
    }


    @Override
    public void paintComponent(Graphics g) {
        // Draw all components - typically a loop, should be easy to implement if we use collections of players, tiles, etc
        // We could prematurely optimize and draw only what needs changed, etc. but for now fuck it - just worry about rudimentary stuff
        // ...although our team name implies that we will prematurely optimize:)

        
        //backgroundImage = Scalr.r
        g.drawImage(backgroundImage, 0,0,WIDTH,HEIGHT, null);


        hud.draw(g, WIDTH, HEIGHT);

        //BoardSpace b = new BoardSpace(WIDTH/2,HEIGHT/2,Color.MAGENTA);
        //drawToken(g,b);
        drawPath(g);

        if (gameState.isPaused()) {
            g.setColor(new Color(0.0f,0.0f,0.0f,.5f));
            g.fillRect(0, 0, WIDTH, HEIGHT);
            
            g.setFont(PAUSED_FONT);
            
            g.setColor(PAUSED_COLOR_1);
            g.drawString(GAME_PAUSED, PAUSED_STRING_X_OFFSET-1, PAUSED_STRING_Y_OFFSET-1);
            g.setColor(PAUSED_COLOR_3);
            g.drawString(GAME_PAUSED, PAUSED_STRING_X_OFFSET, PAUSED_STRING_Y_OFFSET);
            g.setColor(PAUSED_COLOR_2);
            g.drawString(GAME_PAUSED, PAUSED_STRING_X_OFFSET+1, PAUSED_STRING_Y_OFFSET+1);
        }
        
        WarningManager.getInstance().draw(g, 0, 0);
        
    }

    /**
    *  This function draws the path stored
    *  in the gameState object. It also draws 
    *  tokens on their specific locations 
    *  the path.
    *  @g The graphics object we are using to draw
    **/
    public void drawPath(Graphics g)
    {
        ArrayList<BoardSpace> path = gameState.getPath(); // The game board path

        for(int i = 0; i < path.size() - 2; i++) // Draw the path stored in the array
        {
            if(path.get(i).specialNum == -1)
            {
                g.setColor(path.get(i).getColor()); // Get the color of this specific space
                g.fill3DRect(path.get(i).getXOrigin(),path.get(i).getYOrigin(), WIDTH/10, HEIGHT/10, true); // Draw the rect at this index  
            }
            else
            {
                g.setColor(Color.white); // Get the color of this specific space
                g.fill3DRect(path.get(i).getXOrigin(),path.get(i).getYOrigin(), WIDTH/10, HEIGHT/10, true);   
                g.drawImage(specialSpaceImg(path.get(i).specialNum), path.get(i).getXOrigin(),path.get(i).getYOrigin(), WIDTH/10, HEIGHT/10, null);
            }

            g.drawImage(grandmasHouseImage, path.get(path.size() - 3).getXOrigin(),path.get(path.size() - 3).getYOrigin(),WIDTH/10,HEIGHT/10, null);

        }
        
        for(int i = 0; i < path.size() - 2; i++) // Draw the users stored in the array
        {
            ArrayList<Player> players = path.get(i).getPlayers(); // Get the players stored in this space

            for(int j = 0; j < path.get(i).getNumPlayers(); j++) // Iterate through the Boardspaces's players and draw tokens as necessary
            {

                if(j == 0) 
                {
                    drawToken(g, path.get(i), 0,0,players.get(j)); // Draw the first token in top left of square
                }
                else if(j == 1)
                {
                    drawToken(g, path.get(i), WIDTH/17,0,players.get(j)); // Draw the second token  in top right of square
                }
                else if(j == 2)
                {
                    drawToken(g, path.get(i), 0, HEIGHT/20,players.get(j)); // Draw the third token in bottom left of square
                }
                else
                {
                    drawToken(g, path.get(i), WIDTH/17, HEIGHT/20,players.get(j)); // Draw the fourth token in bottom right of square
                }

            }

        }
        //Draw the start block
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.PLAIN|Font.BOLD, HEIGHT/50 + WIDTH/50));
        g.drawString("Start", path.get(0).getXOrigin() + WIDTH/70, path.get(0).getYOrigin()+ HEIGHT/15);

        
        //Draw the start block
        //g.setColor(Color.black);
        //g.setFont(new Font("Arial", Font.PLAIN|Font.BOLD, HEIGHT/50 + WIDTH/50));
        //g.drawString("Grandma's House", path.get(path.size() - 3).getXOrigin() - WIDTH/85, path.get(path.size() - 3).getYOrigin()+ HEIGHT/5);
    }
    
    /**
    * This function draws a token at an offset from a Boardspace.
    * @g The graphics object we are using for drawing
    * @space The board space we are offsetting from
    * @xOffset The xOffset from the BoardSpace
    * @yOffset The yOffset from the BoardSpace
    * @user The player whose token is being drawn
    *
    **/
    public void drawToken(Graphics g, BoardSpace space, int xOffset, int yOffset, Player user)
    {
        user.setPlayerX(space.getXOrigin() + xOffset);
        user.setPlayerY(space.getYOrigin() + yOffset);

        g.setColor(user.getColor());
        g.fillArc(space.getXOrigin() + xOffset, space.getYOrigin() + yOffset, HEIGHT/20, HEIGHT/20,0, 360);
        
        
        g.setColor(Color.black);
        g.drawArc(space.getXOrigin() + xOffset, space.getYOrigin() + yOffset, HEIGHT/20, HEIGHT/20,0, 360);
        
        // If we should highlight
        if(user.getPlayerNumber() == gameState.getSelectedPlayer())
        {            
            g.setColor(new Color(255, 255, 255, 255));
            for (int i = 0; i <= RADIUS_OF_TOKEN_HIGHLIGHTING; i++) {
                int alpha = g.getColor().getAlpha();
                int red = g.getColor().getRed();
                int green = g.getColor().getGreen();
                int blue = g.getColor().getBlue();
                
                alpha -= 10;
                red -= 10;
                blue -= 5;
                if (green > 51) {
                    green -= 51;
                }
                
                g.setColor(new Color(red, green, blue, alpha));
                g.drawArc(space.getXOrigin() + xOffset - i, space.getYOrigin() + yOffset - i,
                                    HEIGHT/20 + (i*2), HEIGHT/20 + (i*2), 0, 360);
            }
            
        }
        

    }

    public BufferedImage specialSpaceImg(int id)
    {
        if(id == 0)
        {
            return iceCreamImage;
        }
        else if(id == 1)
        {
            return chocolateBarImage;
        }
        else if(id == 2)
        {
            return candyCaneImage;
        }
        else if(id == 3)
        {
            return lollipopImage;
        }
        else if(id == 4)
        {
            return candyImage;
        }
        else
        {
            return null;
        }
    }
	
    public SweetState getGameState() {
            return gameState;
    }

    public ArrayList<Player> getPlayerCountAndNames(boolean gameModeIsStrategicMode) {
        Color[] playerColors = { Color.cyan, Color.black, Color.pink, Color.white};
        boolean done = false; 	// Used to make sure we only accept correct input
        ArrayList<Player> players = new ArrayList<Player>();
        int numPlayers = 0;
        int numAIPlayers = 0;
        int startLocation = 0;
        int numBoomerangs;
        int option;
		
        // Set number of boomerangs players will have. 
        // If gameModeSelection is 1, we are playing strategic mode
        if (gameModeIsStrategicMode)
            numBoomerangs = 3;
        else
            numBoomerangs = 0;

        // Get number of players
        while (done == false) 
        {
            try 
            {

                String input = JOptionPane.showInputDialog("How many players are playing?");
                numPlayers = Integer.parseInt(input);


                if (numPlayers < 5 && numPlayers > 1)
                {
                    done  = true;
                }
                else 
                {
                    JOptionPane.showMessageDialog(null, "Only between 1 and 4 total players permitted");
                    done  = false;
                    continue;
                }


            } 
            catch (NumberFormatException e) 
            {
                JOptionPane.showMessageDialog(null, "Invalid Input");
                done = false;
            }
        }
        int playerNum = 0;
		//Get names of players
		for (int i = 0; i < numPlayers; i++) 
        {
			        
                    String aiMessage = "Would you like player " + (playerNum +1) + " to be AI controlled?";
                    
                    option = JOptionPane.showOptionDialog(null,  
                                   aiMessage,  
                                    "Start", JOptionPane.YES_NO_OPTION,  
                                    JOptionPane.WARNING_MESSAGE, null, null,  
                                    null);

                    String playerName;

                    if(option == JOptionPane.NO_OPTION) 
                    {
                         playerName = JOptionPane.showInputDialog("What is player " + (i + 1) + "'s name?");
                    }
                    else
                    {
                        playerName = "AIPlayer " + (playerNum + 1);
                    }

                    

                    for (int j = 0; j < i; j++) 
                    {
                        if (playerName.equals(players.get(j).getName()) || playerName.length() < 1 || playerName.length() > 10) 
                        {
                            playerName = JOptionPane.showInputDialog("Please enter a unique name between 1 and 10 characters");


                            j = 0;
                        }
                    }
                    
                    players.add(i, new Player(playerColors[i], playerName, startLocation, numBoomerangs, playerNum));


                    if(option == JOptionPane.YES_OPTION) 
                    {
                         players.get(i).setIsAI(true);
                    }

                    playerNum++;
		}

		return players;
	}
	
    // Key and Mouse adapters

    private KeyAdapter initKeyAdapter() {
        return new KeyAdapter() {
            /*
            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println("Key typed: " + e.getKeyChar());
            }
            */
            
            @Override
            public void keyPressed(KeyEvent e) {
                
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.out.println("Pause button has been pressed!");
                    gameState.togglePaused();														
                }
                
                System.out.println("Key pressed: " + e.getKeyChar());
                
                
                
            }
        };
    }

    public float distance(int x1, int y1, int x2, int y2)
    {
    	return (float) Math.sqrt(Math.pow((x2-x1),2) + Math.pow((y2-y1),2));
    }

    public int checkForPlayer(int x, int y, ArrayList<Player> players)
    {
    	
    	for(int i = 0; i < players.size();i++)
    	{
    		Player currentPlayer = players.get(i);

            float distance =  distance(x,y,currentPlayer.getPlayerX(),currentPlayer.getPlayerY()+(HEIGHT/20)/4);

    		if(distance <= (HEIGHT/20))
    		{
    			return i;
    		}
    	}

    	return -1;
    }

    private static final int BB_X_LEFT = 968;
    private static final int BB_X_RIGHT = 1191;
    private static final int BB_Y_TOP = 706;
    private static final int BB_Y_BOTTOM = 800;
    
    
    private MouseAdapter initMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Mouse button " + e.getButton() + " clicked at " + e.getX() + ", " + e.getY());
                gameState.clickPlayer(checkForPlayer(e.getX(), e.getY(),gameState.getPlayers()));
                if(gameState.getSelectedPlayer() != -1)
                {
                    System.out.println("Player " + gameState.getSelectedPlayer() + " has been clicked");
                }
                
                //Deck button is 125x100 pixels in old resolution, placed in bottom right corner
                if ( e.getX() <= WIDTH && e.getX() >= ( WIDTH - (int)(125*hud.getWidthRatio()) )  
                    && e.getY() <= HEIGHT && e.getY() >= (HEIGHT - (int)(100*hud.getHeightRatio()))) {
                    // Only draw a card from the deck if the game is NOT paused.
                    if (!gameState.isPaused())
                    { 
                        gameState.clickDeck();
                        System.out.println(gameState.getCurrentPlayerTurn() + " clicked the deck!");
                    }
                } else if (gameState.isGameModeStrategicMode() 
                        && e.getX() >= (int)(BB_X_LEFT*hud.getWidthRatio()) 
                        && e.getX() <= (int)(BB_X_RIGHT*hud.getWidthRatio())
                        && e.getY() >= (int)(BB_Y_TOP*hud.getHeightRatio())
                        && e.getY() <= (int)(BB_Y_BOTTOM*hud.getHeightRatio())) {
                    gameState.clickBoomerang();
                    System.out.println(gameState.getCurrentPlayerTurn() + " clicked the boomerang box!");
                }
            }
        };
    }
}
