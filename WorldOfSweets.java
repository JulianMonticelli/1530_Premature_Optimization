import java.awt.Color;
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

    SweetState gameState;

    public WorldOfSweets() {
            this.setPreferredSize(new Dimension(WIDTH,HEIGHT)); // Preferred size affects packing
            this.setFocusable(true); // Focusable so we can use keyboard input
            this.addKeyListener(initKeyAdapter()); // Listens to keys, obv 
            this.addMouseListener(initMouseListener());

            gameState = new SweetState();                

            running = true;



            this.setBackground(Color.black);
    }


    private int targetFPS = 30;

    private boolean running = false;

    int x = 0;
    int y = 599;


    public void run() {
            // Basic af boilerplate. This is definitely subject to change.
            // This is the standard basic template for Swing java games.
            // Assuming 30FPS


        while (running) {
            // Target time is always recalculated in case we want to switch frame rate
            long targetTime = (1000L/targetFPS);
            long startTime = System.currentTimeMillis();

            // Check if the game is paused and handle accordingly
            handlePause();

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

    public void handlePause() {
        if (gameState.isPaused()) {
            while (gameState.isPaused()) {
                try {
                    Thread.sleep(50); // Sleep for 50ms and check again
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } 
            }
        }
    }


    public void tick() {
        // If there has been a change in the game state
        x++; y--;
        // Do this shiznit
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.red);
        g.drawRect(x, y, 10, 10);

        g.drawRect(100, 100, 40, 40);
        // Iterate players on board and draw them


        // HUD / Deck and 
    }




    // Key and Mouse adapters

    private KeyAdapter initKeyAdapter() {
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println("Key typed: " + e.getKeyChar());
                        
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        gameState.togglePause();
                        break;
                    default:
                        break;
                }   
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