/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */
package sweets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.LinkedList;
import java.io.Serializable;


/**
 * @author Julian
 */
public class WarningManager implements Serializable{
    // This needs to exist to allow serializability
    private static final long serialVersionUID = 489021829075290170L;
    
    private static WarningManager instance;
    
    private double widthRatio;
    private double heightRatio;
    
    private final int warningXPos;
    private final int warningYPos;
    
    private Font warningFontDefault;
    private Font warningFontInformation;
    
    // Singleton system
    public static WarningManager getInstance() {
        if (instance == null)
            instance = new WarningManager(1.0, 1.0);
        return instance;
    }
    
    
    // Linked list for warnings
    LinkedList<Warning> warningList;
    LinkedList<Warning> removeList;
    
    public WarningManager(double widthRatio, double heightRatio) {
        warningFontDefault = new Font("Verdana", Font.BOLD, (int)(72*widthRatio));
        warningFontInformation  = new Font("Verdana", Font.BOLD, (int)(36*heightRatio));
        
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
        
        warningXPos = (int)(400*widthRatio);
        warningYPos = (int)(400*heightRatio);
        
        warningList = new LinkedList<>();
        removeList = new LinkedList<>();
        instance = this;
    }
    
    /*******************************************************************
     * Creates a warning via the "standard" warning implementation
     * This should be cleaned up in the future
     * @param warning String that will be displayed on-screen
     */
    public void createWarning(String warning, int warningType) {
            if (warningType == Warning.TYPE_ENDGAME) {
                int warnLength = warning.length();
                
                // TRY to center this to the screen...
                int letterDifferencePixel = (int)(25*widthRatio);
                int xDiff = (warnLength - 10) * letterDifferencePixel;
                                
                int actualX = warningXPos - xDiff;
                int actualY = warningYPos - (int)(10 * heightRatio);
                
                createWarning(warning, warningType, actualX, actualY);
            }
            else {
                createWarning(warning, warningType, warningXPos, warningYPos);
            }
    }
    
    /*******************************************************************
     * Creates a warning via the "standard" warning implementation
     * This should be cleaned up in the future
     * @param warning String that will be displayed on-screen
     */
    public void createWarning(String warning, int warningType, int x, int y) {
        switch (warningType) {
            case Warning.TYPE_WARNING:
                warningList.add(new Warning(warning, 60,  warningFontDefault, x, y, Color.RED));
                break;
            case Warning.TYPE_POSITIVE:
                warningList.add(new Warning(warning, 90,  warningFontDefault, x, y, Color.GREEN));
                break;
            case Warning.TYPE_ENDGAME:
                warningList.add(new Warning(warning, 90, warningFontDefault, x, y, Color.BLACK));
                break;
            case Warning.TYPE_INFORMATION:
                warningList.add(new Warning(warning, 51, warningFontInformation, x, y, Color.black));
                break;
            default:
                System.err.println("Warning " + warningType + " not available!");
                break;
        }
    }
    
    /*******************************************************************
     * Remove a specific warning from the WarningQueue
     * @param warning The warning we want to remove
     */
    public void remove(Warning warning) {
        removeList.add(warning);
    }
    
    /*******************************************************************
     * Remove a specific warning from the WarningQueue
     * @param warning The warning we want to remove
     */
    public void clearWarningList() {
        warningList.clear();
        removeList.clear();
    }
    
    /*******************************************************************
     * Dispatches updates to all warnings in the queue
     */
    public void update() {
        for (Warning w : warningList) {
            w.update();
        }
        
        // Remove all elemnts from warningList if they are in removeList
        warningList.removeAll(removeList);
        
        // Clear all elements from removeList
        removeList.clear();
        
    }
    
    /*******************************************************************
     * Dispatches drawing to all warnings on-screen
     * @param g Graphics context
     * @param xOffs offset of x
     * @param yOffs offset of y
     */
    public void draw(Graphics g, int xOffs, int yOffs) {
        for (Warning w : warningList) {
            w.draw(g, xOffs, yOffs);
        }
    }
}
