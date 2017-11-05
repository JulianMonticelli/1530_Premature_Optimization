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
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.util.ArrayList;


public class WorldOfSweets extends JPanel {    
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 1000;
        
        private HUD hud;
        private SweetState gameState;
	
	public WorldOfSweets() {
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT)); // Preferred size affects packing
		this.setFocusable(true); // Focusable so we can use keyboard input
		this.addKeyListener(initKeyAdapter()); // Listens to keys, obv 
		this.addMouseListener(initMouseListener());
                
                hud = new HUD();
                gameState = new SweetState();
                gameState.storePath(WIDTH,HEIGHT);
                
		running = true;
	}
	
	
	private int targetFPS = 30;
	
	private boolean running = false;
	private int colorState = 1;
	
	
	
        
	
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
		gameState.makeTurn();
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
			g.drawImage(img, 0, 0,WIDTH,HEIGHT, null);
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

	public void drawPath(Graphics g)
	{
		ArrayList<BoardSpace> path = gameState.getPath();

		for(int i = 0; i < path.size() - 3;i++)
		{
			g.setColor(path.get(i).getColor());
			g.fill3DRect(path.get(i).getXOrigin(),path.get(i).getYOrigin(), WIDTH/10, HEIGHT/10, true); // Draw a rect at current calculated height and width.	
		}
	}

	public int drawToken(Graphics g, BoardSpace space)
	{
		g.setColor(space.getColor());
		g.fillArc(space.getXOrigin(), space.getYOrigin(), WIDTH/20, HEIGHT/20,0, 360);
		g.setColor(Color.black);
		g.drawArc(space.getXOrigin(), space.getYOrigin(), WIDTH/20, HEIGHT/20,0, 360);
		
		return 0;
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
