package sweets;
import java.awt.Color;
import java.io.Serializable;

public class Player implements Serializable
{
	private Color playerColor;
	private String name;
	private int position;
	
	public Player() {
		playerColor = null;
		name = "";
		position = 0;
	}
	
	public Player(Color c, String n, int p) {
		playerColor = c;
		name = n;
		position = p;
	}
	
	public void setName(String n)
	{
		name = n;
	}

	public String getName()
	{
		return name;
	}
	
	public void setColor(Color colr)
	{
		playerColor = colr;
	}

	public Color getColor()
	{
		return playerColor;
	}
	
	public int getPos() {
		return position;
	}
	
	public void setPos(int pos) {
		position = pos;
	}	
}
