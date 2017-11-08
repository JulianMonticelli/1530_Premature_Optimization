import org.junit.Test;
import static org.junit.Assert.*;

import sweets.Card;

public class CardTest {

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
  @Test
  public void setColorCorrectly() {
    Card c = new Card(Card.COLOR_RED);
    assertEquals(Card.COLOR_RED, c.getColor());
  }

  @Test
  public void setSpecialsCorrectly() {
    Card d = new Card(Card.DOUBLE);
    Card g = new Card(Card.GO_TO_MIDDLE);
    Card s = new Card(Card.SKIP_TURN);
    assertEquals(true, d.isDouble());
    assertEquals(false, d.isSkipTurn());
    assertEquals(true, g.isMiddleCard());
    assertEquals(true, s.isSkipTurn());
    assertEquals(true, s.isMiddleCard());
  }

  @Test
  public void doubleOfColor() {
    Card c = new Card(Card.COLOR_RED | Card.DOUBLE);
    assertEquals(Card.COLOR_RED, c.getColor());
    assertEquals(true, c.isDouble());
  }
}
