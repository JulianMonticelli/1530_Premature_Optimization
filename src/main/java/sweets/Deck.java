package sweets;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
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
        top = 0;
    }

    public int getNumCards() {
        return theDeck.size() - top;
    }

    public int getCapacity() {
        return theDeck.size();
    }

    public boolean empty() {
        return top == theDeck.size();
    }

    public Card draw() {
		if(empty())
			reshuffleDeck();
        return theDeck.get(top++);
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

    private void swap(int a, int b) {
        Card c = theDeck.get(a);
        theDeck.set(a, theDeck.get(b));
        theDeck.set(b, c);
    }
}
