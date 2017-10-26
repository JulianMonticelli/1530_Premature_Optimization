package sweets;

import java.util.ArrayList;

public class DeckFactory {
  public static final int COLOR_RED = 0;
  public static final int COLOR_GREEN = 1;
  public static final int COLOR_BLUE = 2;
  public static final int COLOR_YELLOW = 3;
  public static final int COLOR_ORANGE = 4;
  public static final int COLOR_PURPLE = 5;

  public DeckFactory() {}

  public Deck makeDeck() {
    ArrayList<Card> cardList = new ArrayList<Card>();
    for(int i = 0; i < 6; i++) {
      for(int c = 0; c < 10; c++)
        cardList.add(new Card(i, false));
      for(int d = 0; d < 2; d++)
        cardList.add(new Card(i, true));
    }
    return new Deck(cardList);
  }
}
