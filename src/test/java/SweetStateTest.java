import org.junit.Test;
import static org.junit.Assert.*;

import sweets.SweetState;
import sweets.BoardSpace;
import java.util.ArrayList;

public class SweetStateTest {

    // Incase anybody hasn't taken 1632, here's how you write a test
    // Also, if you haven't taken 1632, I HIGHLY suggest you do before you graduate.

	
	/**
    * Tests to make sure isDeckClicked is false at the start and true after deckClick()
    **/
	@Test
	public void testIsDeckClicked()
	{
		//ArrayList<Player> testPlayers = new ArrayList<Player>();
		//players.add(new Player(null, "test player 1", 0));
		//players.add(new Player(null, "test player 2", 0));
	
		SweetState gameState = new SweetState();
		assertFalse(gameState.isDeckClicked());
		
		gameState.clickDeck();
		assertTrue(gameState.isDeckClicked());
	}
    
    
    /**
    * Tests to ensure pickSpecialSpaces is always generating
    * numbers that are at least 5 squares away4
    * from each other
    * User Story 24 Special spaces
    **/
    @Test
    public void testPickSpecialSpaces() 
    {
        
        int specialSpaces[] = {-1,-1,-1,-1,-1};
        boolean valid = true;

        SweetState gameState = new SweetState();

        specialSpaces = gameState.pickSpecialSpaces(specialSpaces, 2, 50);
        
        for(int i = 0; i < specialSpaces.length; i++)
        {
            for(int j = 0; j < specialSpaces.length; j++)
            {
                if(i == j)
                {
                    continue;
                }
                else
                {
                    if(Math.abs(specialSpaces[i] - specialSpaces[j]) >= 5)
                    {
                        valid = true;
                    }
                    else
                    {
                        valid = false;
                    }

                    assertTrue(valid);
                }
            }
        }
       
    }

    /**
    * Tests to ensure that there are always 
    * exactly 5 special spaces generated in the game path.
    * User Story 24 Special spaces
    **/
    @Test
    public void testNumSpecialSpacesInPath() 
    {
        
        SweetState gameState = new SweetState();
        ArrayList<BoardSpace> spaces = gameState.storePath(1200,1000);
        int specialSpaces = 0;

        for(int i = 0; i < spaces.size() - 4;i++)
        {
            if(spaces.get(i).specialNum != -1)
            {
                specialSpaces++;
            }
        }

        assertEquals(specialSpaces,5);
    }


    /**
    * Tests to ensure that validNum
    * is not allowing numbers within
    * 5 units of each other to be picked
    * User Story 25 Random Special spaces
    **/
    @Test
    public void testValidNum() 
    {
        
        
        boolean valid = true;

        SweetState gameState = new SweetState();
        
        int specialSpaces[] = {35,-1,-1,-1,-1};
        assertFalse(gameState.validNum(specialSpaces,35, 1));

        specialSpaces[1] = 3;
        assertTrue(gameState.validNum(specialSpaces,40, 2));

        specialSpaces[2] = 10;
        assertFalse(gameState.validNum(specialSpaces,11, 3));

        specialSpaces[3] = 24;
        assertTrue(gameState.validNum(specialSpaces,30, 4));

        specialSpaces[4] = 50;
        assertFalse(gameState.validNum(specialSpaces,47, 0));

    }

    /**
    * Tests to ensure that searchForSpecialSquare
    * returns the correct value
    * User Story 25 Random Special spaces
    **/
    @Test
    public void testSearchForSpecialSquare() 
    {
        
        
        boolean valid = true;

        SweetState gameState = new SweetState();
        
        int specialSpaces[] = {35,45,12,5,20};
        
        assertEquals(gameState.searchForSpecialSquare(specialSpaces, 35), 0);
        assertEquals(gameState.searchForSpecialSquare(specialSpaces, 45), 1);
        assertEquals(gameState.searchForSpecialSquare(specialSpaces, 12), 2);
        assertEquals(gameState.searchForSpecialSquare(specialSpaces, 5), 3);
        assertEquals(gameState.searchForSpecialSquare(specialSpaces, 20), 4);
        assertEquals(gameState.searchForSpecialSquare(specialSpaces, 25), -1);

    }

    /**
    * Tests to ensure that when the static special squares option is selected 
    * that every time the sweetstate is initialize it contains the same locations for special squares.
    * User story 28 Static special tiles
    **/
    @Test
    public void testStaticSpaceOption() 
    {
       SweetState gameState = new SweetState();
       gameState.randomSpaces = false;
       ArrayList<BoardSpace> spaces = gameState.storePath(1200,1000);
       int specials[] = gameState.specialSpaces;


       SweetState gameState1 = new SweetState();
       gameState.randomSpaces = false;
       ArrayList<BoardSpace> spaces1 = gameState1.storePath(1200,1000);
       int specials1[] = gameState1.specialSpaces;

        
        int specialSpaces = 0;

        for(int i = 0; i < specials.length;i++)
        {
            assertEquals(specials[i], specials1[i]);
        }

    }


    /**
    * Tests to ensure that when the randomize special squares option is selected 
    * that every time the sweetstate is initialize it contains different locations for special squares.
    *  User story 28 Static special tiles
    **/
    @Test
    public void testRandomSpaceOption() 
    {
       SweetState gameState = new SweetState();
       gameState.randomSpaces = true;
       ArrayList<BoardSpace> spaces = gameState.storePath(1200,1000);
       int specials[] = gameState.specialSpaces;


       SweetState gameState1 = new SweetState();
       gameState.randomSpaces = true;
       ArrayList<BoardSpace> spaces1 = gameState1.storePath(1200,1000);
       int specials1[] = gameState1.specialSpaces;

        
        int specialSpaces = 0;

        for(int i = 0; i < specials.length;i++)
        {
            assertNotEquals(specials[i], specials1[i]);
        }

    }


    
}

