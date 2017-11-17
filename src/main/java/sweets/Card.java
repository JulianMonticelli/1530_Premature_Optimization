package sweets;

public class Card {

    public static final int COLOR_RED = 1;
    public static final int COLOR_GREEN = 8;
    public static final int COLOR_BLUE = 4;
    public static final int COLOR_YELLOW = 2;
    public static final int COLOR_ORANGE = 16;
    public static final int DOUBLE = 32;
    public static final int GO_TO_MIDDLE = 128;
    public static final int SKIP_TURN = 256;


    private int data;

    public Card(int data) {
        if((data & 511) == 0) throw new IllegalStateException();
        this.data = data;
    }

    public boolean isDouble() { return ((data & DOUBLE) != 0); }
    public boolean isSkipTurn() { return ((data & SKIP_TURN) != 0); }
    public boolean isMiddleCard() { return ((data & GO_TO_MIDDLE) != 0); }
    public int getColor() {
        int c = data & 31;

        if(c == 0)
        {
            c = 1;
        }

     return  c;
    }
}
