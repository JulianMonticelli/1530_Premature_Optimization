package sweets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StateFile implements Serializable {
    // This needs to exist to allow serializability
    private static final long serialVersionUID = 489021829075290170L;
    
    
    // WARNING: gameState should be final.
    // It is not because one of the methods which tests it will change
    // the gameState in order to test that the verifyContent() method fails.
    private SweetState gameState;
    private byte[] messageDigest;
    
    public StateFile(SweetState gameState) {
        this.gameState = gameState;
        messageDigest = prepareMessageDigest();
    }
    
    
    // WARNING: This method is used ONLY for testing!
    // Allowing the program to access the game state in this StateFile object
    // nullfies the point of having the prepareMessageDigest() called in the
    // constructor.
    public void setGameState(SweetState gameState) {
        this.gameState = gameState;
    }
    
    public byte[] prepareMessageDigest() {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        
        
        byte[] gameStateMessageDigest = null;
        
        try {
            gameStateMessageDigest = md.digest(serializeGameState());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return gameStateMessageDigest;
    }
    
    public byte[] serializeGameState() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(gameState);
        return out.toByteArray();
    }
    
    
    
    public boolean verifyContent() {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        
        byte[] gameStateMessageDigest = null;
        
        try {
            gameStateMessageDigest = md.digest(serializeGameState());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return MessageDigest.isEqual(gameStateMessageDigest, messageDigest);
    }
    
    

    public static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }


    
    public SweetState getGameState() {
        return gameState;
    }
    
    
}