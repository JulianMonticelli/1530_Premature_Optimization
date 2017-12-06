import org.junit.Test;
import static org.junit.Assert.*;

import sweets.Player;

public class PlayerTest {

    // Incase anybody hasn't taken 1632, here's how you write a test
    // Also, if you haven't taken 1632, I HIGHLY suggest you do before you graduate.

    /*
    @Test
    public void testThisOrThat() {
        value = "lol hey";
        value2 = "soup bruh";
        assertEquals(value, value2); // This test will fail.
    }


    */
    
	/* 
	*  Tests to make sure that if a player has boomerangs, throwBoomerang() will decrement the # of boomerangs they have by 1
	*/ 
	@Test
	public void throwBoomerangTestWithBoomerang() {
		Player testPlayer = new Player(null, "test player 1", 0, 2, 1);	// Player created with 2 boomerangs
		int countBefore = testPlayer.getBoomerangCount();
		testPlayer.throwBoomerang();
		int countAfter = testPlayer.getBoomerangCount();

		assertTrue(countBefore - countAfter == 1);
	}
	
	/* 
	*  Tests to make sure that if a player has no boomerangs, throwBoomerang() will not decrement the # of boomerangs
	*/ 
	@Test
	public void throwBoomerangTestNoBoomerang() {
		Player testPlayer = new Player(null, "test player 1", 0, 0, 1);	// Player created with 0 boomerangs
		int countBefore = testPlayer.getBoomerangCount();
		testPlayer.throwBoomerang();
		int countAfter = testPlayer.getBoomerangCount();
		
		assertTrue(countBefore == countAfter);
	}
    
}

