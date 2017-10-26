import java.awt.Color;

public class boardSpace
{
	int xOrigin; // The x value of the graphical origin of the space.
	int yOrigin; // The y value of the graphical origin of the space.
	Color spaceColor;

	public boardSpace()
	{
		xOrigin = 0;
		yOrigin = 0;
		spaceColor = Color.white;
	}

	public boardSpace(int x, int y, Color color)
	{
		xOrigin = x;
		yOrigin = x;
		spaceColor = Color.white;
	}

	/**
	 * Quick and dirty color state function
	 * @return The color to be applied.
	 */
	private Color colorPick(int colorState)
	{
		if(colorState == 0)
		{
			return Color.MAGENTA;
			
		}
		if(colorState == 1)
		{
			return Color.red;
			
		}
		if(colorState == 2)
		{
			return Color.green;
			
		}
		if(colorState == 3)
		{
			return Color.orange;
			
		}
		if(colorState == 4)
		{
			return Color.blue;
			
		}
		else
		{
			return Color.yellow;
			
		}
		
	}

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
		return ;
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