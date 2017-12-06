package sweets;

import java.util.ArrayList;
import java.util.Random;
import java.io.Serializable;

public class Deck implements Serializable{
    // This needs to exist to allow serializability
    private static final long serialVersionUID = 489021829075290170L;

    private SweetState gameState = null;
    private ArrayList<Card> theDeck;
    private int top;

    // TODO: This constructor should construct and shuffle a deck!
    public Deck() {
        throw new IllegalStateException(); //Picked randomly. Just didn't want to do an import.
    }

    public Deck(ArrayList<Card> d) {
        theDeck = d; //Could convert to array.
        reshuffleDeck();
    }

    public void addSweetState(SweetState s) {
        gameState = s;
    }

    //NKD: Would prefer to call "shuffle"
    public void reshuffleDeck() {
        Random RNG = new Random();
        for(int i = theDeck.size(); i > 0; i--) {
            int target = RNG.nextInt(i);
            swap(i - 1, target);
        }
        top = -1;
    }

    public int getNumCards() {
        return theDeck.size() - top;
    }

    public int getCapacity() {
        return theDeck.size();
    }

    public boolean empty() {
        return top == (theDeck.size() - 1);
    }

    public Card draw() {
		if(empty())
			reshuffleDeck();
		top++;
		return new Card(Card.SKIP_TURN, -1);
    }

    public Card dadDraw(int playerPosition) {
	    draw(); //Deal with deck state update, but don't return
        int worstCardIndex = top;
        int worstMoveDistance = getDistance(playerPosition, theDeck.get(i));
        for (int i = top + 1; i < theDeck.size(); i++) {
            int currentMoveDistance = getDistance(playerPosition,
                                                  theDeck.get(i));
            if (currentMoveDistance < worstMoveDistance) {
                worstCardIndex = i;
                worstMoveDistance = currentMoveDistance;
            }
        }
        swap(top, worstCardIndex);
        return theDeck.get(top);
    }

    private int getDistance(int start, Card aCard) {
        return gameState.calculateDest(start, aCard) - start;
    }

    //NKD: Need to define error handling for case where no card was drawn
    //Currently, returns null
    public Card getLastCard() {
        if(top < 0 || top >= theDeck.size())
            return null;
        return theDeck.get(top);
    }

    public boolean wasLastCardDouble() {
        return getLastCard().isDouble();
    }

    public boolean wasLastCardSkipCard() {
        return getLastCard().isSkipTurn();
    }

    private void swap(int a, int b) {
        Card c = theDeck.get(a);
        theDeck.set(a, theDeck.get(b));
        theDeck.set(b, c);
    }
}
