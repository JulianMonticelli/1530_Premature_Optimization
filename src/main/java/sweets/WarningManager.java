/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */
package sweets; // via SupremeMayor

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.LinkedList;
import java.io.Serializable;


/**
 * @author Julian
 */
public class WarningManager implements Serializable{
    
    private static WarningManager instance;
    
    private static int WARNING_X_POS = 200;
    private static int WARNING_Y_POS = 100;
    
    private static Font WARNING_FONT_DEFAULT = new Font("Verdana", Font.BOLD, 72);
    
    // Singleton system
    public static WarningManager getInstance() {
        if (instance == null)
            instance = new WarningManager();
        return instance;
    }
    
    
    // Linked list for warnings
    LinkedList<Warning> warningList;
    LinkedList<Warning> removeList;
    
    private WarningManager() {
        warningList = new LinkedList<>();
        removeList = new LinkedList<>();
    }
    
    /*******************************************************************
     * Creates a warning via the "standard" warning implementation
     * This should be cleaned up in the future
     * @param warning String that will be displayed on-screen
     */
    public void createWarning(String warning, int warningType) {
            createWarning(warning, warningType, WARNING_X_POS, WARNING_Y_POS);
    }
    
    /*******************************************************************
     * Creates a warning via the "standard" warning implementation
     * This should be cleaned up in the future
     * @param warning String that will be displayed on-screen
     */
    public void createWarning(String warning, int warningType, int x, int y) {
        switch (warningType) {
            case Warning.TYPE_WARNING:
                warningList.add(new Warning(warning, 60,  WARNING_FONT_DEFAULT, x, y, Color.RED));
                break;
            case Warning.TYPE_POSITIVE:
                warningList.add(new Warning(warning, 90,  WARNING_FONT_DEFAULT, x, y, Color.GREEN));
                break;
            case Warning.TYPE_ENDGAME:
                warningList.add(new Warning(warning, 90, WARNING_FONT_DEFAULT, x, y, Color.BLACK));
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
