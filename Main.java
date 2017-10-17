import javax.swing.JFrame;




public class Main extends JFrame {

	public static final String TITLE = "World of Sweets | Premature Optimization";
	
	WorldOfSweets gamePanel;
	
	public static void main(String[] args) {
		Main frame = new Main();
		frame.init();
		
	}
	
	public void init() {
		gamePanel = new WorldOfSweets();
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(gamePanel);
		this.pack();
		this.setVisible(true);
		gamePanel.run();
	}
}