import java.awt.Color;

public class boardSpace
{
	int xOrigin; // The x value of the graphical origin of the space.
	int yOrigin; // The y value of the graphical origin of the space.
	Color spaceColor; // THe color of the space

	/**
	* No-arg constructor for a boardSpace
	**/
	public boardSpace()
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
	public boardSpace(int x, int y, Color color)
	{
		xOrigin = x;
		yOrigin = x;
		spaceColor = color;
	}

	/**
	 * Quick and dirty color state function
	 * @return The color to be applied.
	 */
	private Color colorPick(int colorState)
	{
		if(colorState == 0)
		{
			color = Color.MAGENTA;
			
		}
		if(colorState == 1)
		{
			color = Color.red;
			
		}
		if(colorState == 2)
		{
			color = Color.green;
			
		}
		if(colorState == 3)
		{
			color = Color.orange;
			
		}
		if(colorState == 4)
		{
			color = Color.blue;
			
		}
		else
		{
			color = Color.yellow;
			
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
		return color;
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
		color = c;
	}
}