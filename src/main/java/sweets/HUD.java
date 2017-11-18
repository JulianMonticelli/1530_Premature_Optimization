package sweets;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;	
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
    private static BufferedImage SKIP_CARD;
    private static BufferedImage MIDDLE_CARD;
    
    private static BufferedImage CARD_DOUBLE_OVERLAY;
    
    private static BufferedImage HUD_BACKGROUND_IMAGE;
    private static BufferedImage HUD_BACKGROUND_IMAGE_2;
    private static BufferedImage HUD_BACKGROUND_IMAGE_3;
    private static BufferedImage HUD_TIMER_BACKGROUND_IMAGE;
    
    // colors
    private static final Color TEXT_BEHIND_COLOR = Color.decode("#FFBF00");
    private static final Color TEXT_MIDDLE_COLOR = Color.decode("#4C3900");
    private static final Color TEXT_MAIN_COLOR = Color.decode("#FFFFFF");
    
    private static final Color TIMER_TEXT_BEHIND_COLOR = Color.decode("#000000");
    private static final Color TIMER_TEXT_MIDDLE_COLOR = Color.decode("#A0A0A0");
    private static final Color TIMER_TEXT_MAIN_COLOR = Color.decode("#FFFFFF");
    
    
    private static final int HUD_ELEMENT_SIZE = 128;
    private static final int HUD_OFFSET_HEIGHT = HUD_ELEMENT_SIZE-10; // 128 pixel images, drawn an extra 20 pixels down the bottom
    private static final int HUD_OFFSET_WIDTH = HUD_ELEMENT_SIZE;
    
    // used ABSOLUTE pixel resolution
    private static final int TIMER_BACKGROUND_OFFSET_X = 964;
    private static final int TIMER_BACKGROUND_OFFSET_Y = 801;
    
    
    private static final int TIMER_TEXT_OFFSET_X = TIMER_BACKGROUND_OFFSET_X + 72;
    private static final int TIMER_TEXT_OFFSET_Y = TIMER_BACKGROUND_OFFSET_Y + 62;
    
    private static final Font fontHUD = new Font("Arial", Font.PLAIN|Font.BOLD, 36);
    
    
    private BufferedImage hudBackground = null;
    
    private static final int HUD_BACKGROUND_MARGIN = 2;
    
    private int backgroundDrawX = -1, backgroundDrawY = -1;
    
    private BufferedImage currentDeckImage;
    private BufferedImage lastCardPicked;
    
    private String timerTimeStamp = "Not Initialized";
    
    private boolean wasLastCardPickedDouble;
    
    private String playerFirstPlace;
    private String playerTurn;
    
    private int screenWidth = -1;
    private int screenHeight = -1;
    
    public HUD(int screenWidth, int screenHeight) {
        try {
            DECK_FULL = ImageIO.read(new File(Main.getAssetLocale() + "cards/deck_full.png"));
            DECK_75 = ImageIO.read(new File(Main.getAssetLocale() + "cards/deck_75.png"));
            DECK_HALF = ImageIO.read(new File(Main.getAssetLocale() + "cards/deck_50.png"));
            DECK_25 = ImageIO.read(new File(Main.getAssetLocale() + "cards/deck_25.png"));
            CARD_RED = ImageIO.read(new File(Main.getAssetLocale() + "cards/card_red.png"));
            CARD_GREEN = ImageIO.read(new File(Main.getAssetLocale() + "cards/card_green.png"));
            CARD_BLUE = ImageIO.read(new File(Main.getAssetLocale() + "cards/card_blue.png"));
            CARD_YELLOW = ImageIO.read(new File(Main.getAssetLocale() + "cards/card_yellow.png"));
            CARD_PURPLE = ImageIO.read(new File(Main.getAssetLocale() + "cards/card_purple.png"));
            CARD_ORANGE = ImageIO.read(new File(Main.getAssetLocale() + "cards/card_orange.png"));
            CARD_DOUBLE_OVERLAY = ImageIO.read(new File(Main.getAssetLocale() + "cards/card_double_overlay.png"));
            
            SKIP_CARD = ImageIO.read(new File(Main.getAssetLocale() + "cards/skip_card.png"));
            MIDDLE_CARD = ImageIO.read(new File(Main.getAssetLocale() + "cards/middle_card.png"));
            
            HUD_BACKGROUND_IMAGE = ImageIO.read(new File(Main.getAssetLocale() + "hud/choco_bar.png"));
            HUD_BACKGROUND_IMAGE_2 = ImageIO.read(new File(Main.getAssetLocale() + "hud/choco_bar_bite.png"));
            HUD_BACKGROUND_IMAGE_3 = ImageIO.read(new File(Main.getAssetLocale() + "hud/choco_bar_wrapper.png"));
            HUD_TIMER_BACKGROUND_IMAGE = ImageIO.read(new File(Main.getAssetLocale() + "hud/timer_background.png"));
            
            HUD_BACKGROUND_IMAGE.getHeight();
            
            this.screenWidth = screenWidth;
            this.screenHeight = screenHeight;
            
            backgroundDrawY = screenHeight - HUD_BACKGROUND_MARGIN - HUD_BACKGROUND_IMAGE.getHeight();
            backgroundDrawX = HUD_BACKGROUND_MARGIN;
            
            hudBackground = HUD_BACKGROUND_IMAGE_3;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        currentDeckImage = DECK_FULL; // Deck starts as full
    }
    
    
    
    public void update(SweetState gameState) {
        updateFirstPlace(gameState);
        updatePlayerTurn(gameState);
        updateDeckDisplay(gameState.getDeck());
        updateLastCardPicked(gameState.getDeck());
        updateTimer(gameState);
    }
    
    public String updateTimer(SweetState gameState) {
        // Update Timer timestamp
        timerTimeStamp = gameState.getMultithreadedTimer().getTimerString();
        return timerTimeStamp;
    } 
    
    public String updateFirstPlace(SweetState gameState) {
        ArrayList<String> playerNames = gameState.getPlayerInFirst();
        
        // WARNING: Requires Java 1.8 - but this is wayyyyyy cool:
        playerFirstPlace = String.join(", ", playerNames);
        
        return playerFirstPlace;
    }

    public String updatePlayerTurn(SweetState gameState) {
        // TODO: Fix player turn to make it more obvious that it is the player's turn
        playerTurn = gameState.getCurrentPlayerTurn();
        return playerTurn;
    }
    
    
    public void draw(Graphics g, int screenWidth, int screenHeight) {
        
        // HUD background
        g.drawImage(hudBackground, backgroundDrawX, backgroundDrawY, null);
        
        // Deck
        g.drawImage(currentDeckImage, screenWidth-HUD_OFFSET_WIDTH, screenHeight-HUD_OFFSET_HEIGHT, null);

        drawHUDTimer(g, timerTimeStamp);
        
        // Last Card
        if (lastCardPicked != null) {
            g.drawImage(lastCardPicked, screenWidth-HUD_OFFSET_WIDTH-HUD_ELEMENT_SIZE, screenHeight-HUD_OFFSET_HEIGHT, null);
            if (wasLastCardPickedDouble) {
                g.drawImage(CARD_DOUBLE_OVERLAY, screenWidth-HUD_OFFSET_WIDTH - HUD_ELEMENT_SIZE, screenHeight-HUD_OFFSET_HEIGHT, null);
            }
        }
        
        int y = 965;
        
        // Player's Turn
        drawHUDString(g, playerTurn + "'s turn!", 20, y);
        
        // Players in first
        drawHUDString(g, playerFirstPlace, 400, y);
    }
    
    private void drawHUDTimer(Graphics g, String timerTimeStamp) {
        g.drawImage(HUD_TIMER_BACKGROUND_IMAGE, TIMER_BACKGROUND_OFFSET_X, TIMER_BACKGROUND_OFFSET_Y, null);
        
        g.setFont(fontHUD);
        
        g.setColor(TIMER_TEXT_BEHIND_COLOR);
        g.drawString(timerTimeStamp, TIMER_TEXT_OFFSET_X-2, TIMER_TEXT_OFFSET_Y-2);
        
        g.setColor(TIMER_TEXT_MIDDLE_COLOR);
        g.drawString(timerTimeStamp, TIMER_TEXT_OFFSET_X-1, TIMER_TEXT_OFFSET_Y-1);
        
        g.setColor(TIMER_TEXT_MAIN_COLOR);
        g.drawString(timerTimeStamp, TIMER_TEXT_OFFSET_X, TIMER_TEXT_OFFSET_Y);
        
    }
    
    // FEEL FREE to change some colors if you want to boys, I'm not convinced these colors are great
    private void drawHUDString(Graphics g, String str, int x, int y) {
    	g.setColor(TEXT_BEHIND_COLOR);
        g.setFont(fontHUD);
        g.drawString(str, x, y);
        
        g.setColor(TEXT_MIDDLE_COLOR);
        g.drawString(str, x+1, y+1);
        
        g.setColor(TEXT_MAIN_COLOR);
        g.drawString(str, x+2, y+2);
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
        
        if (c == null) {
            return null;
        }
		
		// Check this first to avoid overlay image bugs
        wasLastCardPickedDouble = c.isDouble(); 
        
        if (c.isSkipTurn()) {
            lastCardPicked = SKIP_CARD;
            return c;
        }
        
        if (c.isMiddleCard()) {
            lastCardPicked = MIDDLE_CARD;
            return c;
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
            default:
                System.out.println(c.getColor());
                System.err.println("Not a valid card color. ");
        }
        
        return c;
    }
}
