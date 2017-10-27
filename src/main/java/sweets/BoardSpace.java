import java.awt.Color;

public class BoardSpace
{
	int xOrigin; // The x value of the graphical origin of the space.
	int yOrigin; // The y value of the graphical origin of the space.
	Color spaceColor; // THe color of the space

	/**
	* No-arg constructor for a boardSpace
	**/
	public BoardSpace()
	{
		xOrigin = 0;
		yOrigin = 0;
		spaceColor = Color.white;
	}

	/**
	* Constructor to intialize a board space with a location and color
	* @x The x location of the origin of the space
	* @y The y location of the origin of the space
	* @color The color of the space
	**/
	public BoardSpace(int x, int y, Color color)
	{
		xOrigin = x;
		yOrigin = x;
		spaceColor = color;
	}

	/**
	 * Quick and dirty color state function
	 * @return The color to be applied.
	 */
	private void colorPick(int colorState)
	{
		if(colorState == 0)
		{
			spaceColor = Color.MAGENTA;
			
		}
		if(colorState == 1)
		{
			spaceColor = Color.red;
			
		}
		if(colorState == 2)
		{
			spaceColor = Color.green;
			
		}
		if(colorState == 3)
		{
			spaceColor = Color.orange;
			
		}
		if(colorState == 4)
		{
			spaceColor = Color.blue;
			
		}
		else
		{
			spaceColor = Color.yellow;
			
		}
		
	}

	/*
	* Setters and getters
	*/
	public int getXOrigin()
	{
		return xOrigin;
	}

	public int getYOrigin()
	{
		return yOrigin;
	}

	public Color getColor()
	{
		return spaceColor;
	}

	public void setXOrigin(int x)
	{
		xOrigin = x;
	}

	public void setYOrigin(int y)
	{
		yOrigin = y;
	}

	public void setColor(Color c)
	{
		spaceColor = c;
	}
}