import org.junit.Test;
import static org.junit.Assert.*;

import sweets.SweetState;
import sweets.StateFile;

public class StateFileTest {

    
    @Test
    // Tests that a StateFile verifies correct game content correctly.
    public void testStateFileVerifyContentVerifiesCorrectly() {
        StateFile sf = new StateFile(new SweetState());
        assert(sf.verifyContent());
    }
    
    @Test
    // Determines that the StateFile messageDigest is different than a modified
    // game state.
    public void testStateFileVerifyContentDoesNotVerifyDifferentGameStates() {
        SweetState ss1 = new SweetState();
        SweetState ss2 = new SweetState();
        ss2.setGameModeIsStrategicMode(true);
        StateFile sf = new StateFile(ss1);
        sf.setGameState(ss2);
        assert(!sf.verifyContent());
    }
    
    
}

