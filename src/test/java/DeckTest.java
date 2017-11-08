import org.junit.*;
import static org.junit.Assert.*;

import sweets.Deck;
import sweets.DeckFactory;
import sweets.Card;

public class DeckTest {
  DeckFactory _df = new DeckFactory();
  Deck _d;

  @Before
  public void setup() {
    _d = _df.makeDeck();
  }

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

  //Deck should have 6 colors * (10 single + 2 double) = 72 + 5 skip + 3 go_to_middle
  @Test
  public void correctSize() {
    assertEquals(80, _d.getCapacity());
  }

  @Test
  public void drawForever() {
    for(int i = 0; i < 1000; i++)
      _d.draw();
  }

  @Test
  public void lastIsRight() {
    Card c = _d.draw();
    assertEquals(c, _d.getLastCard());
  }


}
