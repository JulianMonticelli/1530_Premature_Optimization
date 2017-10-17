
import java.util.LinkedList;

public class Deck {
    
    private static final int NUM_CARDS = 20; // 20 for now.
    
    private Card[] cards;
    private boolean[] cardInDeck;
    private Card lastCard;
    
    private int cardsLeftInDeck;
    
    
    // Deck could be a linked list
    private LinkedList<Card> deck;
    
    
    public Deck() {
        cards = new Card()[NUM_CARDS];
        deck = new LinkedList<>();
        
        // TODO: Create cards and shuffle deck
        
        cardsLeftInDeck = NUM_CARDS;
    }
    
    public void reshuffleDeck() {
        // Reshuffle deck
    }
    
    private void resetDeck() {
        // Reset the deck, reshuffle
    }
    
    
    
}