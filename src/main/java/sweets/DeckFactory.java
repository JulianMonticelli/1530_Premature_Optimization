package sweets;

import java.util.ArrayList;
import java.io.Serializable;

public class DeckFactory implements Serializable{
  public DeckFactory() {}

  public Deck makeDeck() {
    ArrayList<Card> cardList = new ArrayList<Card>();
    for(int i = 1; i < 32; i *= 2) {
      for(int c = 0; c < 10; c++)
        cardList.add(new Card(i, -1));
      for(int d = 0; d < 2; d++)
        cardList.add(new Card(i | Card.DOUBLE, -1));
    }
    for(int i = 0; i < 5; i++)
      cardList.add(new Card(Card.SKIP_TURN, -1));
    for(int i = 0; i < 5; i++)
      cardList.add(new Card(Card.SPECIAL_MOVE, i));

    return new Deck(cardList);
  }
}
