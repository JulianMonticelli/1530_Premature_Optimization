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
    Card c = new Card(Card.COLOR_RED, -1);
    assertEquals(Card.COLOR_RED, c.getColor());
  }

  @Test
  public void setSpecialsCorrectly() {
    Card d = new Card(Card.DOUBLE, -1);
    Card g = new Card(Card.SPECIAL_MOVE, -1);
    Card s = new Card(Card.SKIP_TURN, -1);
    assertEquals(true, d.isDouble());
    assertEquals(false, d.isSkipTurn());
    assertEquals(true, g.isSpecialMoveCard());
    assertEquals(true, s.isSkipTurn());
    assertEquals(false, s.isSpecialMoveCard());
  }

  @Test
  public void doubleOfColor() {
    Card c = new Card(Card.COLOR_RED | Card.DOUBLE, -1);
    assertEquals(Card.COLOR_RED, c.getColor());
    assertEquals(true, c.isDouble());
  }
}
