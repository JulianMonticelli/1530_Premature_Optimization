package sweets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


public class Main extends JFrame {

	public static final String TITLE = "World of Sweets | Premature Optimization";
	
	private WorldOfSweets gamePanel;
	
	public static void main(String[] args) {
		Main frame = new Main();
		frame.init();
		
	}
	
	public void init() {
		gamePanel = new WorldOfSweets();
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                JMenuBar jmb = new JMenuBar();
                this.setupJMenuBar(jmb);
                this.setJMenuBar(jmb);
                
		this.add(gamePanel);
                
                
                
		this.pack();
		this.setVisible(true);
                
		gamePanel.run();
	}
        
        public void setupJMenuBar(JMenuBar jmb) {
            
            JMenu file = new JMenu("File");
            
            JMenuItem save = new JMenuItem("Save");
            save.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gamePanel.saveStateFile();
                }
            });
            file.add(save);
            
            JMenuItem load = new JMenuItem("Load");
            load.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gamePanel.loadStateFile();
                }
            });
            file.add(load);
            
            /*JMenuItem reset = new JMenuItem();
            reset.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gamePanel.setPaused(true);
                    gamePanel.selectLoadFile();
                }
            });
            file.add(reset);
            */
            
            jmb.add(file);
            
        }
        
        public static String getAssetLocale() {
            return System.getProperty("user.dir") + "/src/main/java/sweets/assets/";
        }
}
