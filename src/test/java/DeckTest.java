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

  @Test
  public void correctSize() {
    int expectedCards = 0;
    int numColors = 5;
    int cardsPerColor = 12;
    int specialCards = 5 + 5; //5 skip, 5 special
    expectedCards = numColors * cardsPerColor;
    expectedCards = expectedCards + specialCards;
    assertEquals(expectedCards, _d.getCapacity());
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

  @Test
  public void testDeckShuffles() {
     DeckFactory df = new DeckFactory();
     Deck d = df.makeDeck();
     d.reshuffleDeck();
     Card c1 = d.draw();
     Card c2 = d.draw();
     Card c3 = d.draw();

     // This test relies on the assumption that the deck
     // is at least reset every time you call reshuffle.

     d.reshuffleDeck();
     Card cc1 = d.draw();
     Card cc2 = d.draw();
     Card cc3 = d.draw();

     boolean comp1 = c1.equals(cc1);
     boolean comp2 = c2.equals(cc2);
     boolean comp3 = c3.equals(cc3);

     d.reshuffleDeck();

     boolean bool = comp1 && comp2 && comp3;
     assertTrue(!bool);
  }


}
