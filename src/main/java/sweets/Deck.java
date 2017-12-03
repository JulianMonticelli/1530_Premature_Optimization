package sweets;

import java.util.ArrayList;
import java.util.Random;
import java.io.Serializable;

public class Deck implements Serializable{
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
		return theDeck.get(top);
    }

    public Card dadDraw(BoardSpace currentSpace) {
		draw(); //Deal with deck state update, but don't return
        int worstCardIndex = top;
        for (int i = top + 1; i < theDeck.size(); i++) {
            if (moveDistance(theDeck.get(worstCardIndex), currentSpace)
                > moveDistance(theDeck.get(i), currentSpace)) {
                worstCardIndex = i;
            }
        }
        swap(theDeck, top, worstCardIndex);
        return theDeck.get(top);
    }

    public int moveDistance(Card aCard, BoardSpace currentSpace) {
        return 0;
    }

    public void swap(ArrayList<Card> theDeck, int first, int second) {
        Card c = theDeck.get(first);
        theDeck.set(first, theDeck.get(second));
        theDeck.set(second, c);
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

	// Deprecated
	/*
    public boolean wasLastCardMiddleCard() {
        return getLastCard().isMiddleCard();
    }
	*/

    private void swap(int a, int b) {
        Card c = theDeck.get(a);
        theDeck.set(a, theDeck.get(b));
        theDeck.set(b, c);
    }
}
