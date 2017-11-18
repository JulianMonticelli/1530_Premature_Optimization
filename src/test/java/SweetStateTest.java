import org.junit.Test;
import static org.junit.Assert.*;

import sweets.SweetState;

public class SweetStateTest {

    // Incase anybody hasn't taken 1632, here's how you write a test
    // Also, if you haven't taken 1632, I HIGHLY suggest you do before you graduate.

    
    /**
    * Tests to ensure pickSPecialSpaces is always generating
    * numbers that are at least 5 squares away
    * from each other
    *
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
    * Tests to ensure that validNum
    * is not allowing numbers within
    * 5 units of each other to be picked
    *
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
    
    
}

