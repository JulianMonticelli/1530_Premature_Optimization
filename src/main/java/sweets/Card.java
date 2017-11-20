package sweets;

public class Card {
    //Bitwise flags for various card attributes
    //Defined as powers of 2
    public static final int COLOR_RED = 1;
    public static final int COLOR_YELLOW = 2;
    public static final int COLOR_BLUE = 4;
    public static final int COLOR_GREEN = 8;
    public static final int COLOR_ORANGE = 16;
    public static final int DOUBLE = 32;
    public static final int SPECIAL_MOVE = 64;
    public static final int SKIP_TURN = 128;

    //The sum of each relevant flag, will be 1 for each flag bit and zero for others.
    private static final int ALL_FLAGS = 255;
    private static final int ALL_COLORS = 31;

    private int flags;
    private int specialMoveNumber;

    public Card(int flags, int specialMoveNumber) {
        //Check the flag of a card by bitwise and-ing it with the bit string where each flag is 1
        //This will result in a non-zero value if at least one recognized flag is 1
        //Cannot create a card if the provided flags do not include at least one recognized flag
        if((flags & ALL_FLAGS) == 0) throw new IllegalStateException();
        if((flags & SPECIAL_MOVE) == 0) this.specialMoveNumber = -1;
        else this.specialMoveNumber = specialMoveNumber;
        this.flags = flags;
    }

    public boolean isDouble() { return ((flags & DOUBLE) != 0); }
    public boolean isSkipTurn() { return ((flags & SKIP_TURN) != 0); }
    //This method has been replaced by isSpecialMove
    @Deprecated
    //public boolean isMiddleCard() { return ((flags & SPECIAL_MOVE) != 0); }
    public boolean isSpecialMoveCard() { return ((flags & SPECIAL_MOVE) != 0); }
    public int getSpecialMoveNumber() { return specialMoveNumber;}

    public int getColor() {
        //Check the color with bitwise and
        int c = flags & ALL_COLORS;
        //There is code somewhere that assumes every card has a color
        //For special cards, a color of red is assumed
        if(c == 0) {
            c = 1;
        }
        return  c;
    }
}
