public class Card {
    
    public static final int COLOR_RED = 0;
    public static final int COLOR_GREEN = 1;
    public static final int COLOR_BLUE = 2;
    public static final int COLOR_YELLOW = 3;
    public static final int COLOR_ORANGE = 4;
    public static final int COLOR_PURPLE = 5;
    
    
    private int color;
    private boolean isDouble;
    
    public Card(int color, boolean isDouble) {
        this.color = color;
        this.isDouble = isDouble;
    }
    
    
}