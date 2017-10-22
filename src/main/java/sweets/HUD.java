package sweets;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class HUD {
    private static BufferedImage DECK_FULL;
    private static BufferedImage DECK_75;
    private static BufferedImage DECK_HALF;
    private static BufferedImage DECK_25;
    
    private static BufferedImage CARD_RED;
    private static BufferedImage CARD_GREEN;
    private static BufferedImage CARD_BLUE;
    private static BufferedImage CARD_YELLOW;
    private static BufferedImage CARD_PURPLE;
    private static BufferedImage CARD_ORANGE;
    
    private static BufferedImage CARD_DOUBLE_OVERLAY;
    
    
    private static final int HUD_ELEMENT_SIZE = 128;
    private static final int HUD_OFFSET_HEIGHT = HUD_ELEMENT_SIZE-20; // 128 pixel images, drawn an extra 20 pixels down the bottom
    private static final int HUD_OFFSET_WIDTH = HUD_ELEMENT_SIZE;
    
    private BufferedImage currentDeckImage;
    private BufferedImage lastCardPicked;
    private boolean wasLastCardPickedDouble;
    
    
    public HUD() {
        try {
            DECK_FULL = ImageIO.read(new File("sweets/assets/cards/deck_full.png"));
            DECK_75 = ImageIO.read(new File("sweets/assets/cards/deck_75.png"));
            DECK_HALF = ImageIO.read(new File("sweets/assets/cards/deck_50.png"));
            DECK_25 = ImageIO.read(new File("sweets/assets/cards/deck_25.png"));
            CARD_RED = ImageIO.read(new File("sweets/assets/cards/card_red.png"));
            CARD_GREEN = ImageIO.read(new File("sweets/assets/cards/card_green.png"));
            CARD_BLUE = ImageIO.read(new File("sweets/assets/cards/card_blue.png"));
            CARD_YELLOW = ImageIO.read(new File("sweets/assets/cards/card_yellow.png"));
            CARD_PURPLE = ImageIO.read(new File("sweets/assets/cards/card_purple.png"));
            CARD_ORANGE = ImageIO.read(new File("sweets/assets/cards/card_orange.png"));
            CARD_DOUBLE_OVERLAY = ImageIO.read(new File("sweets/assets/cards/card_double_overlay.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        currentDeckImage = DECK_FULL; // Deck starts as full
    }
    
    
    
    public void update(Deck deck) {
        updateDeckDisplay(deck);
        updateLastCardPicked(deck);
    }
    
    
    
    public void draw(Graphics g, int screenWidth, int screenHeight) {
        // Deck
        g.drawImage(currentDeckImage, screenWidth-HUD_OFFSET_WIDTH, screenHeight-HUD_OFFSET_HEIGHT, null);
        
        // Last Card
        if (lastCardPicked != null) {
            g.drawImage(lastCardPicked, screenWidth-HUD_OFFSET_WIDTH, screenHeight-HUD_OFFSET_HEIGHT - HUD_ELEMENT_SIZE, null);
            if (wasLastCardPickedDouble) {
                g.drawImage(CARD_DOUBLE_OVERLAY, screenWidth-HUD_OFFSET_WIDTH, screenHeight-HUD_OFFSET_HEIGHT - HUD_ELEMENT_SIZE, null);
            }
        }
        
    }
    
    
    
    private int updateDeckDisplay(Deck deck) {
        // Deck from 76% full to 100% full will display 100% full
        if (deck.getNumCards() / (float)deck.getCapacity() >= 0.76f) {
            currentDeckImage = DECK_FULL;
            return 100;
        } else if (deck.getNumCards() / (float)deck.getCapacity() >= 0.51f) {
            currentDeckImage = DECK_75;
            return 75;
        } else if (deck.getNumCards() / (float)deck.getCapacity() >= 0.26f) {
            currentDeckImage = DECK_HALF;
            return 50;
        } else {
            currentDeckImage = DECK_25;
            return 25;
        }
    }
    
    private Card updateLastCardPicked(Deck deck) {
        Card c = deck.getLastCard();
        
        if(c == null) {
            return null;
        }
        
        switch(c.getColor()) {
            case Card.COLOR_RED:
                lastCardPicked = CARD_RED;
                break;
            case Card.COLOR_GREEN:
                lastCardPicked = CARD_GREEN;
                break;
            case Card.COLOR_BLUE:
                lastCardPicked = CARD_BLUE;
                break;
            case Card.COLOR_YELLOW:
                lastCardPicked = CARD_YELLOW;
                break;
            case Card.COLOR_ORANGE:
                lastCardPicked = CARD_ORANGE;
                break;
            case Card.COLOR_PURPLE:
                lastCardPicked = CARD_PURPLE;
                break;
        }
        
        wasLastCardPickedDouble = c.isDouble();       
        
        
        return c;
    }
}