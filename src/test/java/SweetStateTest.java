import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import sweets.*;
import java.util.ArrayList;

public class SweetStateTest {

    // Incase anybody hasn't taken 1632, here's how you write a test
    // Also, if you haven't taken 1632, I HIGHLY suggest you do before you graduate.

	
	@Test
	public void testClickDeck()
	{
		SweetState gameState = new SweetState();
		assertFalse(gameState.isDeckClicked());
		
		gameState.clickDeck();
		assertTrue(gameState.isDeckClicked());
	}
    
	/**
    * Tests to make sure players are added correctly
    **/
	@Test
	public void testAddPlayers()
	{
		ArrayList<Player> testPlayers = new ArrayList<Player>();
		testPlayers.add(new Player(null, "test player 1", 0));
		testPlayers.add(new Player(null, "test player 2", 0));
	
		SweetState gameState = new SweetState();
		gameState.addPlayers(testPlayers);
		
		ArrayList<Player> returnedPlayers = gameState.getPlayers();
		assertEquals(testPlayers.get(0), returnedPlayers.get(0));
		assertEquals(testPlayers.get(1), returnedPlayers.get(1));
	}
	
	/**
    * Tests to make sure getPlayerInFirst returns the proper value when a single player is in first
    **/
	@Test
	public void testGetPlayerInFirstSingle()
	{
		ArrayList<Player> testPlayers = new ArrayList<Player>();
		testPlayers.add(new Player(null, "test player 1", 0));
		testPlayers.add(new Player(null, "test player 2", 1));
		testPlayers.add(new Player(null, "test player 3", 0));
	
		SweetState gameState = new SweetState();
		gameState.addPlayers(testPlayers);
		
		ArrayList<String> returnedPlayers = gameState.getPlayerInFirst();
		assertTrue(returnedPlayers.size() == 1);
		assertEquals(testPlayers.get(1).getName(), returnedPlayers.get(0));
	}
	
	/**
    * Tests to make sure getPlayerInFirst returns the proper value when two players are tied for first
    **/
	@Test
	public void testGetPlayerInFirstDouble()
	{
		ArrayList<Player> testPlayers = new ArrayList<Player>();
		testPlayers.add(new Player(null, "test player 1", 0));
		testPlayers.add(new Player(null, "test player 2", 1));
		testPlayers.add(new Player(null, "test player 3", 1));
	
		SweetState gameState = new SweetState();
		gameState.addPlayers(testPlayers);
		
		ArrayList<String> returnedPlayers = gameState.getPlayerInFirst();
		assertTrue(returnedPlayers.size() == 2);
		assertEquals(testPlayers.get(1).getName(), returnedPlayers.get(0));
		assertEquals(testPlayers.get(2).getName(), returnedPlayers.get(1));
	}
	
	/**
    * Tests to make sure getPlayerInFirst returns the proper value when all players are tied
    **/
	@Test
	public void testGetPlayerInFirstAll()
	{
		ArrayList<Player> testPlayers = new ArrayList<Player>();
		testPlayers.add(new Player(null, "test player 1", 0));
		testPlayers.add(new Player(null, "test player 2", 0));
		testPlayers.add(new Player(null, "test player 3", 0));
	
		SweetState gameState = new SweetState();
		gameState.addPlayers(testPlayers);
		
		ArrayList<String> returnedPlayers = gameState.getPlayerInFirst();
		assertTrue(returnedPlayers.size() == testPlayers.size());
	}
	
	/**
    * Tests to make sure startNextTurn will start the next player's turn
    **/
	@Test
	public void testStartNextTurn()
	{
		ArrayList<Player> testPlayers = new ArrayList<Player>();
		testPlayers.add(new Player(null, "test player 1", 0));
		testPlayers.add(new Player(null, "test player 2", 0));
		testPlayers.add(new Player(null, "test player 3", 0));
	
		SweetState gameState = new SweetState();
		gameState.addPlayers(testPlayers);
		
		assertTrue(gameState.getCurrentPlayerTurn() == testPlayers.get(0).getName());
		gameState.startNextTurn();
		assertTrue(gameState.getCurrentPlayerTurn() == testPlayers.get(1).getName());
	}
	
	/**
    * Tests to make sure startNextTurn will start the first player's turn after the last has finished their turn
    **/
	@Test
	public void testStartNextTurnFirstAgain()
	{
		ArrayList<Player> testPlayers = new ArrayList<Player>();
		testPlayers.add(new Player(null, "test player 1", 0));
		testPlayers.add(new Player(null, "test player 2", 0));
		testPlayers.add(new Player(null, "test player 3", 0));
	
		SweetState gameState = new SweetState();
		gameState.addPlayers(testPlayers);
		
		assertTrue(gameState.getCurrentPlayerTurn() == testPlayers.get(0).getName());
		gameState.startNextTurn();
		gameState.startNextTurn();
		gameState.startNextTurn();
		assertTrue(gameState.getCurrentPlayerTurn() == testPlayers.get(0).getName());
	}
	
	/**
    * Tests to make sure calculateDest will return the same value as the start position when a skip card is drawn
    **/
	@Test
	public void testCalculateDestSkipCard()
	{
		SweetState gameState = new SweetState();
		Card testCard = new Card(Card.SKIP_TURN, -1);
		
		gameState.storePath(1200, 1000);
		int startPos = 1;
		int resultDest = gameState.calculateDest(startPos, testCard);
		assertTrue(resultDest == startPos);
	}
	
	/**
    * Tests to make sure calculateReverseDest will return the same value as the start position when a skip card is drawn
    **/
	@Test
	public void testCalculateReverseDestSkipCard()
	{
		SweetState gameState = new SweetState();
		Card testCard = new Card(Card.SKIP_TURN, -1);
		
		gameState.storePath(1200, 1000);
		int startPos = 1;
		int resultDest = gameState.calculateReverseDest(startPos, testCard);
		assertTrue(resultDest == startPos);
	}
	
	/**
    * When a special card is drawn, calculateDest should return the location of the special tile corresponding to that card
	* This test will let the board generate normally, then attempt to find the destination of every special tile
    **/
	@Test
	public void testCalculateDestSpecialCard()
	{
		SweetState gameState = new SweetState();
		gameState.storePath(1200, 1000);
		int testSpecialSpaces[] = gameState.getSpecialSpaces();
		int resultDest = -1;
		
		for (int i = 0; i < testSpecialSpaces.length; i++) {
			resultDest = gameState.calculateDest(0, new Card(Card.SPECIAL_MOVE, i));
			assertTrue(resultDest == testSpecialSpaces[i]);
		}
	}
	
	/**
    * When a special card is drawn, calculateReverseDest should return the location of the special tile corresponding to that card
	* This test will let the board generate normally, then attempt to find the destination of every special tile
    **/
	@Test
	public void testCalculateReverseDestSpecialCard()
	{
		SweetState gameState = new SweetState();
		gameState.storePath(1200, 1000);
		int testSpecialSpaces[] = gameState.getSpecialSpaces();
		int resultDest = -1;
		
		for (int i = 0; i < testSpecialSpaces.length; i++) {
			resultDest = gameState.calculateReverseDest(0, new Card(Card.SPECIAL_MOVE, i));
			assertTrue(resultDest == testSpecialSpaces[i]);
		}
	}
	
	/**
    * Tests to make sure that special tiles have a color code of -1, preventing movement to that tile from normal cards.
    **/
	@Test
	public void testCalculateDestSpecialCardColor()
	{
		SweetState gameState = new SweetState();
		ArrayList<BoardSpace> testSpaces = gameState.storePath(1200, 1000);
		int testSpecialSpaces[] = gameState.getSpecialSpaces();
		
		for (int i = 0; i < testSpaces.size(); i++) {
			if (testSpaces.get(i).specialNum != -1) {
				assertTrue(testSpaces.get(i).getIntColorCode() == -1);
			}
		}
	}
	
	/**
    * Grandma's house counts as every color. This test will place a token on the tile just before grandma's house. 
	* For every non-skip and non-special card, calculateDest should return the location of grandma's house.
	* This will test the single and double versions of all 5 color cards.
    **/
	@Test
	public void testCalculateDestGrandmaHouse()
	{
		SweetState gameState = new SweetState();
		gameState.storePath(1200, 1000);
		int testGrandmaLoc = gameState.getGrandmaLoc();
		int startPos = testGrandmaLoc - 1;

		for(int i = 1; i < 32; i *= 2) {
			assertEquals(testGrandmaLoc, gameState.calculateDest(startPos, new Card(i, -1)));
			assertEquals(testGrandmaLoc, gameState.calculateDest(startPos, new Card(i | Card.DOUBLE, -1)));
		}
	}
	
	/**
    * The starting tile counts as every color. This test will place a token on the second tile. 
	* For every non-skip and non-special card, calculateReverseDest should return the location of the first tile.
	* This will test the single and double versions of all 5 color cards.
    **/
	@Test
	public void testCalculateReverseDestStartingTile()
	{
		SweetState gameState = new SweetState();
		gameState.storePath(1200, 1000);
		int startTile = 0;
		int startPos = 1;

		for(int i = 1; i < 32; i *= 2) {
			assertEquals(startTile, gameState.calculateReverseDest(startPos, new Card(i, -1)));
			assertEquals(startTile, gameState.calculateReverseDest(startPos, new Card(i | Card.DOUBLE, -1)));
		}
	}
	
	/**
    * If a player is on the starting tile and gets boomeranged, they should remain on the starting tile.
	* This will test the single and double versions of all 5 color cards.
    **/
	@Test
	public void testCalculateReverseDestAreadyStartingTile()
	{
		SweetState gameState = new SweetState();
		gameState.storePath(1200, 1000);
		int startTile = 0;
		int startPos = 0;

		for(int i = 1; i < 32; i *= 2) {
			assertEquals(startTile, gameState.calculateReverseDest(startPos, new Card(i, -1)));
			assertEquals(startTile, gameState.calculateReverseDest(startPos, new Card(i | Card.DOUBLE, -1)));
		}
	}
	
	/**
    * Tests to make sure the destination tile is the same color as the drawn card.
	* All colors are tested for both single and double cards.
    **/
	@Test
	public void testCalculateDestSameColor()
	{
		SweetState gameState = new SweetState();
		ArrayList<BoardSpace> testSpaces = gameState.storePath(1200, 1000);
		int startPos = 0;
		int resultDest;
		Card testCard;

		for(int i = 1; i < 32; i *= 2) {
			testCard = new Card(i, -1);
			resultDest = gameState.calculateDest(startPos, testCard);
			assertEquals(i, testSpaces.get(resultDest).getIntColorCode());
			
			testCard = new Card(i | Card.DOUBLE, -1);
			resultDest = gameState.calculateDest(startPos, testCard);
			assertEquals(i, testSpaces.get(resultDest).getIntColorCode());
		}
	}
	
	/**
    * Tests to make sure the destination tile is the same color as the drawn card.
	* All colors are tested for both single and double cards.
    **/
	@Test
	public void testCalculateReverseDestSameColor()
	{
		SweetState gameState = new SweetState();
		ArrayList<BoardSpace> testSpaces = gameState.storePath(1200, 1000);
		int startPos = 15;
		int resultDest;
		Card testCard;

		for(int i = 1; i < 32; i *= 2) {
			testCard = new Card(i, -1);
			resultDest = gameState.calculateReverseDest(startPos, testCard);
			assertEquals(i, testSpaces.get(resultDest).getIntColorCode());
			
			testCard = new Card(i | Card.DOUBLE, -1);
			resultDest = gameState.calculateReverseDest(startPos, testCard);
			assertEquals(i, testSpaces.get(resultDest).getIntColorCode());
		}
	}
	
	/**
    * Tests to make sure a token moves when a normal/double card is drawn. For example,
	* if a red card is drawn when the token is on a red tile, the token should move to 
	* the next tile instead of remaining in place
    **/
	@Test
	public void testCalculateDestDistanceGreaterThanZero()
	{
		SweetState gameState = new SweetState();
		gameState.storePath(1200, 1000);
		int startPos = 0;
		int resultDest;
		Card testCard;

		for(int i = 1; i < 32; i *= 2) {
			testCard = new Card(i, -1);
			resultDest = gameState.calculateDest(startPos, testCard);
			assertTrue(resultDest - startPos > 0);
			
			testCard = new Card(i | Card.DOUBLE, -1);
			resultDest = gameState.calculateDest(startPos, testCard);
			assertTrue(resultDest - startPos > 0);
		}
	}
	
	/**
    * Tests to make sure a token moves when a normal/double card is drawn. For example,
	* if a red card is drawn when the token is on a red tile, the token should move to 
	* the previous tile instead of remaining in place
    **/
	@Test
	public void testCalculateReverseDestDistanceGreaterThanZero()
	{
		SweetState gameState = new SweetState();
		gameState.storePath(1200, 1000);
		int startPos = 15;
		int resultDest;
		Card testCard;

		for(int i = 1; i < 32; i *= 2) {
			testCard = new Card(i, -1);
			resultDest = gameState.calculateReverseDest(startPos, testCard);
			assertTrue(startPos - resultDest > 0);
			
			testCard = new Card(i | Card.DOUBLE, -1);
			resultDest = gameState.calculateReverseDest(startPos, testCard);
			assertTrue(startPos - resultDest > 0);
		}
	}
	
	/**
    * Tests to make sure a token will move the correct amount of distance when a single card is drawn.
	* Because one of our features was implementing random locations for special tiles, we do not know exactly
	* how far away the first matching tile is going to be; however, we know that it is at most 6 spaces 
	* away since special tiles must be at least 5 squares apart.
    **/
	@Test
	public void testCalculateDestDistanceSingleCard()
	{
		SweetState gameState = new SweetState();
		gameState.storePath(1200, 1000);
		int startPos = 0;
		int resultDest;
		int distance;
		Card testCard;

		for(int i = 1; i < 32; i *= 2) {
			testCard = new Card(i, -1);
			resultDest = gameState.calculateDest(startPos, testCard);
			distance = resultDest - startPos;
			assertTrue(distance > 0 && distance < 7);
		}
	}
	
	/**
    * Tests to make sure a token will move the correct amount of distance when a single card is drawn.
	* Because one of our features was implementing random locations for special tiles, we do not know exactly
	* how far away the first matching tile is going to be; however, we know that it is at most 6 spaces 
	* away since special tiles must be at least 5 squares apart.
    **/
	@Test
	public void testCalculateReverseDestDistanceSingleCard()
	{
		SweetState gameState = new SweetState();
		gameState.storePath(1200, 1000);
		int startPos = 15;
		int resultDest;
		int distance;
		Card testCard;

		for(int i = 1; i < 32; i *= 2) {
			testCard = new Card(i, -1);
			resultDest = gameState.calculateReverseDest(startPos, testCard);
			distance = startPos - resultDest;
			assertTrue(distance > 0 && distance < 7);
		}
	}
	
	/**
    * Tests to make sure a token will move the correct amount of distance when a double card is drawn.
	* Because one of our features was implementing random locations for special tiles, we do not know exactly
	* how far away the second matching tile is going to be; however, we know that it is 6-11 spaces away
	* since special tiles must be at least 5 squares apart.
    **/
	@Test
	public void testCalculateDestDistanceDoubleCard()
	{
		SweetState gameState = new SweetState();
		gameState.storePath(1200, 1000);
		int startPos = 0;
		int resultDest;
		int distance;
		Card testCard;

		for(int i = 1; i < 32; i *= 2) {
			testCard = new Card(i | Card.DOUBLE, -1);
			resultDest = gameState.calculateDest(startPos, testCard);
			distance = resultDest - startPos;
			assertTrue(distance > 5 && distance < 12);
		}
	}
    
	/**
    * Tests to make sure a token will move the correct amount of distance when a double card is drawn.
	* Because one of our features was implementing random locations for special tiles, we do not know exactly
	* how far away the second matching tile is going to be; however, we know that it is 6-11 spaces away
	* since special tiles must be at least 5 squares apart.
    **/
	@Test
	public void testCalculateReverseDestDistanceDoubleCard()
	{
		SweetState gameState = new SweetState();
		gameState.storePath(1200, 1000);
		int startPos = 15;
		int resultDest;
		int distance;
		Card testCard;

		for(int i = 1; i < 32; i *= 2) {
			testCard = new Card(i | Card.DOUBLE, -1);
			resultDest = gameState.calculateReverseDest(startPos, testCard);
			distance = startPos - resultDest;
			assertTrue(distance > 5 && distance < 12);
		}
	}
	
	
	/**
	* Move player should change the value of testPlayer.position to endLoc and return the end destination
	**/
	@Test
	public void testMovePlayer() {
		SweetState gameState = new SweetState();
		gameState.storePath(1200, 1000);
		
		int startLoc = 0;
		int endLoc = 1;
		
		Player testPlayer = new Player(null, "test player 1", startLoc);
		
		
		assertEquals(startLoc, testPlayer.getPos());
		
		int result = gameState.movePlayer(testPlayer, startLoc, endLoc);
		
		assertEquals(endLoc, testPlayer.getPos());
		assertEquals(endLoc, result);
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
       int specials[] = gameState.getSpecialSpaces();


       SweetState gameState1 = new SweetState();
       gameState.randomSpaces = false;
       ArrayList<BoardSpace> spaces1 = gameState1.storePath(1200,1000);
       int specials1[] = gameState.getSpecialSpaces();

        
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
       int specials[] = gameState.getSpecialSpaces();

       SweetState gameState1 = new SweetState();
       gameState.randomSpaces = true;
       ArrayList<BoardSpace> spaces1 = gameState1.storePath(1200,1000);
       int specials1[] = gameState1.getSpecialSpaces();

        
        int specialSpacesEqual = 0;

        for(int i = 0; i < specials.length;i++)
        {
            if(specials[i] == specials1[i])
            {
            	specialSpacesEqual++;
            }
        }

        assertNotEquals(specialSpacesEqual, 4);

    }

    /**
    * Test to ensure that an AI player cannot throw bommarangs when they have none
    * and they decide to
    *  User story 36: AI Players
    **/
    @Test
    public void testAINotThrowNullBoomarang() 
    {
       SweetState testState = new SweetState();
       
       Player player = new Player();
       player.setBoomerangCount(0);
       testState.setBoomerangTarget(-1);

       assertFalse(testState.aiWillThrowBoomarang(player, 4));

    }

    /**
    * Test to ensure that an AI will throw bommarangs when they have 
    * boomarangs and the correct randomnum is generated
    *  User story 36: AI Players
    **/
    @Test
    public void testAIThrowBoomarang() 
    {
       SweetState testState = new SweetState();
       
       Player player = new Player();
       player.setBoomerangCount(3);
       testState.setBoomerangTarget(-1);


       assertTrue(testState.aiWillThrowBoomarang(player, 4));

    }

    /**
    * Test to ensure that an AI will not throw boomarangs when they have 
    * boomarangs and the incorrect randomnum is not generated
    *  User story 36: AI Players
    **/
    @Test
    public void testAINotThrowBoomarangDecision() 
    {
       SweetState testState = new SweetState();
       
       Player player = new Player();
       player.setBoomerangCount(3);
       testState.setBoomerangTarget(-1);


       assertFalse(testState.aiWillThrowBoomarang(player, 1));

    }

	/**
	* Tests to ensure that players are saved and loaded correctly
	**/
	/*@Test
	public void testPlayerSaving() {
		ArrayList<Player> testPlayers = new ArrayList<Player>();
		testPlayers.add(new Player(null, "test player 1", 0));
		testPlayers.add(new Player(null, "test player 2", 0));
		testPlayers.add(new Player(null, "test player 3", 0));
		
		WorldOfSweets w = new WorldOfSweets(testPlayers);
	
		w.getGameState.saveState("test.ser");
					
		SweetState gameState2 = w.loadState("test.ser");
		
		assertEquals(w.getGameState.getPlayers(), gameState2.getPlayers());
	}*/
    
}

