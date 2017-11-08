package sweets;

import java.util.ArrayList;

public class DeckFactory {
  public DeckFactory() {}

  public Deck makeDeck() {
    ArrayList<Card> cardList = new ArrayList<Card>();
    for(int i = 1; i < 64; i *= 2) {
      for(int c = 0; c < 10; c++)
        cardList.add(new Card(i));
      for(int d = 0; d < 2; d++)
        cardList.add(new Card(i | Card.DOUBLE));
    }
    for(int i = 0; i < 3; i++)
      cardList.add(new Card(Card.GO_TO_MIDDLE));
    for(int i = 0; i < 5; i++)
      cardList.add(new Card(Card.SKIP_TURN));
    return new Deck(cardList);
  }
}
