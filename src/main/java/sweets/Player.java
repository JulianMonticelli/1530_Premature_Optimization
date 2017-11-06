package sweets;
import java.awt.Color;

public class Player 
{
	private Color playerColor;
	private String name;

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
}
