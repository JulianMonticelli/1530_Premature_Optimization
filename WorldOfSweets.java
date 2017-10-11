import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;


public class WorldOfSweets extends JPanel {
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	public WorldOfSweets() {
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT)); // Preferred size affects packing
		this.setFocusable(true); // Focusable so we can use keyboard input
		this.addKeyListener(initKeyAdapter()); // Listens to keys, obv 
		this.addMouseListener(initMouseListener());
		running = true;
	}
	
	
	private int targetFPS = 30;
	
	private boolean running = false;
	
	
	
	
	public void run() {
		// Basic af boilerplate. This is definitely subject to change.
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
		// Perform game logic at this stage.
		// We need a state system - ticks could be used to iterate through particle effects, animations, etc if we have any
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		// Draw all components - typically a loop, should be easy to implement if we use collections of players, tiles, etc
		// We could prematurely optimize and draw only what needs changed, etc. but for now fuck it - just worry about rudimentary stuff
		// ...although our team name implies that we will prematurely optimize:)
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
			}
		};
	}
}