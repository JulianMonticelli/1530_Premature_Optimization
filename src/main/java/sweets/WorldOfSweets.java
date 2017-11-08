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
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.util.ArrayList;


public class WorldOfSweets extends JPanel {    
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 1000;


	ArrayList<BoardSpace> path;
        
    private HUD hud;
    private SweetState gameState;

    private int targetFPS = 30;
	
	private boolean running = false;
	private int colorState = 1;
	
	public WorldOfSweets() {
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT)); // Preferred size affects packing
		this.setFocusable(true); // Focusable so we can use keyboard input
		this.addKeyListener(initKeyAdapter()); // Listens to keys, obv 
		this.addMouseListener(initMouseListener());
                
                hud = new HUD(WIDTH, HEIGHT);
                gameState = new SweetState();
                gameState.storePath(WIDTH,HEIGHT);
				gameState.addTokensToBoard();
                
		running = true;

	}    
	
	public void run() {
		// Basic as boilerplate. This is definitely subject to change.
		// This is the standard basic template for Swing java games.
		// Assuming 30FPS
		
		
		while (running) {
			// Target time is always recalculated in case we want to switch frame rate
			long targetTime = (1000L/targetFPS);
			long startTime = System.currentTimeMillis();
			
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
		running = gameState.makeTurn();
		hud.update(gameState.getDeck(), gameState);
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		// Draw all components - typically a loop, should be easy to implement if we use collections of players, tiles, etc
		// We could prematurely optimize and draw only what needs changed, etc. but for now fuck it - just worry about rudimentary stuff
		// ...although our team name implies that we will prematurely optimize:)
              
		try 
		{
            BufferedImage img = ImageIO.read(new File(Main.getAssetLocale() + "background.jpg"));
			//img = Scalr.r
			g.drawImage(img, 0,0,WIDTH,HEIGHT, null);
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
        hud.draw(g, WIDTH, HEIGHT);
        
        //BoardSpace b = new BoardSpace(WIDTH/2,HEIGHT/2,Color.MAGENTA);
        //drawToken(g,b);
        drawPath(g);
		
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
		
		for(int i = 0; i < path.size() - 3;i++) // Draw the path stored in the array
		{
			g.setColor(path.get(i).getColor()); // Get the color of this specific space
			g.fill3DRect(path.get(i).getXOrigin(),path.get(i).getYOrigin(), WIDTH/10, HEIGHT/10, true); // Draw the rect at this index	
			
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

		try 
		{
            BufferedImage img = ImageIO.read(new File(Main.getAssetLocale() + "grandma's.jpg"));
			//img = Scalr.r
			g.drawImage(img, path.get(path.size() - 3).getXOrigin(),path.get(path.size() - 3).getYOrigin(),WIDTH/10,HEIGHT/10, null);
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
	
	
	// Key and Mouse adapters
	
	private KeyAdapter initKeyAdapter() {
		return new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				System.out.println("Key typed: " + e.getKeyChar());
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
					gameState.togglePaused();
				}
			}
		};
	}
}
