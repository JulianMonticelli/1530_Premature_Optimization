package sweets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
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


public class WorldOfSweets extends JPanel {    
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 1000;


	ArrayList<BoardSpace> path;
        
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
        
        hud = new HUD(WIDTH, HEIGHT);
		int option = JOptionPane.showOptionDialog(null,  
												"Do you want to load an existing game?",  
												"Start", JOptionPane.YES_NO_OPTION,  
												JOptionPane.WARNING_MESSAGE, null, null,  
												null);

		if (option == JOptionPane.YES_OPTION) {
			gameState = loadState(selectSave());	
			while (!gameState.getMultithreadedTimer().getTimerString().equals(gameState.getTime())) {};
		}
		else if (option == JOptionPane.NO_OPTION) {
			gameState = new SweetState();
            gameState.gameModeSelection = pickGameMode();
			chooseSpecialSpacePickMode();
			gameState.storePath(WIDTH,HEIGHT);
			gameState.addPlayers(getPlayerCountAndNames());
		}
    
        running = true;
    }

    public int pickGameMode()
    {
        int option;
        
        option = JOptionPane.showOptionDialog(null,  
                                           "Do you want to play in strategic mode, please click yes? Otherwise click no for classic mode.",  
                                            "Start", JOptionPane.YES_NO_OPTION,  
                                            JOptionPane.WARNING_MESSAGE, null, null,  
                                            null);
                
        if (option == JOptionPane.YES_OPTION) 
        {
           return 1;
        }
        else if (option == JOptionPane.NO_OPTION) 
        {
           return 0;
        }
        
        return -1;
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
	
	public String selectSave() {
		ArrayList<String> saveFiles = new ArrayList<String>();
		File dir = new File(".");
		for (File file : dir.listFiles()) {
			if (file.getName().endsWith((".ser"))) {
				saveFiles.add(file.getName());
			}
		}
		String[] options = saveFiles.toArray(new String[saveFiles.size()]);
		int save = JOptionPane.showOptionDialog(null,
											"Which save would you like to load?",
											"Saved Games",
											JOptionPane.YES_NO_CANCEL_OPTION,
											JOptionPane.DEFAULT_OPTION,
											null,
											options,
											options[0]);
		return options[save];									
	}
	
	public static SweetState loadState(String filename) {
		SweetState state = null;
		try {
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			state = (SweetState) ois.readObject();
			ois.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		state.togglePaused();
		MultithreadedTimer timer = new MultithreadedTimer();
		state.setMTTimer(timer);	
		timer.startThread();
		return state;
	}

    public void chooseSpecialSpacePickMode()
    {
        int option = JOptionPane.showOptionDialog(null,  
                                                "Do you want to randomize locations for special squares?",  
                                                "Start", JOptionPane.YES_NO_OPTION,  
                                                JOptionPane.WARNING_MESSAGE, null, null,  
                                                null);

        
                
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

            // Perform turn
            tick();

            // Redraw screen
            repaint();

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
        // Make player turn
        running = gameState.makeTurn();
        
        // Update HUD 
        hud.update(gameState);
        
        // Update WarningSystem
        gameState.getWarningManager().update();
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
        
        gameState.getWarningManager().draw(g, 0, 0);
        
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

        for(int i = 0; i < path.size() - 2;i++) // Draw the path stored in the array
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
        g.setColor(user.getColor());
        g.fillArc(space.getXOrigin() + xOffset, space.getYOrigin() + yOffset, WIDTH/25, HEIGHT/20,0, 360);
        g.fillArc(space.getXOrigin() + xOffset, space.getYOrigin() + yOffset, WIDTH/25, HEIGHT/20,0, 360);
        g.setColor(Color.black);
        g.drawArc(space.getXOrigin() + xOffset, space.getYOrigin() + yOffset, WIDTH/25, HEIGHT/20,0, 360);

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

	public ArrayList<Player> getPlayerCountAndNames() {
		Color[] playerColors = { Color.cyan, Color.black, Color.pink, Color.white};
		boolean done = false; 	//used to make sure we only accept correct input
		ArrayList<Player> players = new ArrayList<Player>();
		int numPlayers = 0;
		
		// Get number of players
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
        
		//Get names of players
		for (int i = 0; i < numPlayers; i++) {
			
			String playerName = JOptionPane.showInputDialog("What is player " + (i + 1) + "'s name?");
			
			for (int j = 0; j < i; j++) {
				if (playerName.equals(players.get(j).getName()) || playerName.length() < 1 || playerName.length() > 10) {
					playerName = JOptionPane.showInputDialog("Please enter a unique name between 1 and 10 characters");
					j = 0;
				}
			}
			
			players.add(i, new Player(playerColors[i], playerName, 0));
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
					if (gameState.isPaused()) {
						int option = JOptionPane.showOptionDialog(null,  
																"Do you want to save?",  
																"Save Game", JOptionPane.YES_NO_OPTION,  
																JOptionPane.WARNING_MESSAGE, null, null,  
																null);
						if (option == JOptionPane.YES_OPTION) {
							String saveName = JOptionPane.showInputDialog("What would you like to call this save?");
							gameState.saveState(saveName);
						}
					}																
                }
                
                System.out.println("Key pressed: " + e.getKeyChar());
                
                
                
            }
        };
    }

    private MouseAdapter initMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Mouse button " + e.getButton() + "clicked at " + e.getX() + ", " + e.getY());

                //Deck button is 125x100 pixels, placed in bottom right corner
                if (e.getX() <= WIDTH && e.getX() >= (WIDTH - 125) && e.getY() <= HEIGHT && e.getY() >= (HEIGHT - 100)) {
                    // Only draw a card from the deck if the game is NOT paused.
                    if (!gameState.isPaused())
                    { 
                        gameState.clickDeck();
                    }
                }
            }
        };
    }
}
