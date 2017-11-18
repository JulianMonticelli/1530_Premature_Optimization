/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */

package sweets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * @author Julian
 */
public class Warning {
    public static final int TYPE_WARNING = 0;
    public static final int TYPE_POSITIVE = 1;
    public static final int TYPE_ENDGAME = 2;
    
    int ticksTillKilled;
    int ticksSinceDispatch;
    String message;
    Font font;
    Color color;
    int xPos;
    int yPos;
    
    public Warning(String message, int limit, Font font, int absX, int absY, Color color) {
        this.ticksSinceDispatch = 0;
        this.ticksTillKilled = limit;
        this.message = message;
        this.xPos = absX;
        this.yPos = absY;
        this.font = font;
        this.color = color;
    }
    
    public void update() {
        ticksSinceDispatch++;
        color = new Color(color.getRed(), color.getBlue(), color.getGreen(), color.getAlpha() - (255/ticksTillKilled));
        yPos -= 1;
        if (ticksSinceDispatch == ticksTillKilled) {
            WarningManager.getInstance().remove(this);
        }
    }
    
    public void draw(Graphics g, int xOffs, int yOffs) {
        // draw code here
        //
        g.setFont(font);
        g.setColor(color);
        g.drawString(message, xPos, yPos);
    }
}
