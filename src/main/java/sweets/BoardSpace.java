package sweets;

import java.awt.Color;
import java.util.ArrayList;

public class BoardSpace
{
	private int xOrigin; // The x value of the graphical origin of the space.
	private int yOrigin; // The y value of the graphical origin of the space.
	private Color spaceColor; // The color of the space
	private ArrayList<Player> players; //Contains list of players currently occupying space
	private int numPlayers = 0;

	/**
	* No-arg constructor for a boardSpace
	**/
	public BoardSpace()
	{
		xOrigin = 0;
		yOrigin = 0;
		spaceColor = Color.white;
		players = new ArrayList<Player>();
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
		yOrigin = y;
		spaceColor = color;
		players = new ArrayList<Player>();
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

	public void addPlayer(Player s) 
	{
		players.add(s);
		numPlayers++;
	}
	
	public void removePlayer(Player s) 
	{
		players.remove(s);
		numPlayers--;
	}

	public int getNumPlayers()
	{
		return numPlayers;
	}
	
	public ArrayList<Player> getPlayers()
	{
		return players;
	}
}
