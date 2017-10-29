package sweets;

import java.awt.Color;
import java.util.ArrayList;

public class BoardSpace
{
	int xOrigin; // The x value of the graphical origin of the space.
	int yOrigin; // The y value of the graphical origin of the space.
	Color spaceColor; // The color of the space
	ArrayList<String> players; //Contains list of players currently occupying space
	

	/**
	* No-arg constructor for a boardSpace
	**/
	public BoardSpace()
	{
		xOrigin = 0;
		yOrigin = 0;
		spaceColor = Color.white;
		players = new ArrayList<String>(4);
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
		players = new ArrayList<String>(4);
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

	public void addPlayer(String s) 
	{
		players.add(s);
	}
	
	public void removePlayer(String s) 
	{
		players.remove(s);
	}
	
	public ArrayList<String> getPlayers()
	{
		return players;
	}
}