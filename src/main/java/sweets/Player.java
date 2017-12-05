package sweets;
import java.awt.Color;
import java.io.Serializable;

public class Player implements Serializable
{
	private Color playerColor;
	private String name;
	private int position;
	private int boomerangCount;
	private int playerX;
	private int playerY;
	private boolean isAI = false;
	private boolean isDad = false;
	private int playerNumber;

	public Player() {
		playerColor = null;
		name = "";
		position = 0;
	}

	public Player(Color c, String n, int p, int bCount, int num) {
		playerColor = c;
		name = n;
		position = p;
		boomerangCount = bCount;
		playerNumber = num;
		isDad = name.equals("Dad");
	}

	// If # of boomerangs is not specified, it will default to 0
	public Player(Color c, String n, int p) {
		playerColor = c;
		name = n;
		position = p;
		boomerangCount = 0;
	}

	public int getPlayerNumber()
	{
		return playerNumber;
	}

	public void setIsAI(boolean ai)
	{
		isAI = ai;
	}

	public boolean getIsAI()
	{
		return isAI;
	}

	public void setName(String n)
	{
		name = n;
	}

	public String getName()
	{
		return name;
	}

	public void setPlayerX(int x)
	{
		playerX = x;
	}

	public int getPlayerX()
	{
		return playerX;
	}

	public void setPlayerY(int y)
	{
		playerY = y;
	}

	public int getPlayerY()
	{
		return playerY;
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

	// Return 1 if a boomerang is thrown; if no boomerangs are left, return 0
	public int throwBoomerang() {
		if (boomerangCount > 0) {
			boomerangCount--;
			return 1;
		}

		return 0;
	}

	public int getBoomerangCount() {
		return boomerangCount;
	}

	public void setBoomerangCount(int bCount) {
		boomerangCount = bCount;
	}

	public boolean isDad() {
		return isDad;
	}
}
