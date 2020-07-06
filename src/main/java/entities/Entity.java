package entities;

import javafx.scene.image.Image;
import lombok.*;
import spells.StatusEffect;

/**
 * class representing a character in the game
 */
@AllArgsConstructor
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public abstract class Entity {
    


    public enum Class {
        ROGUE,
        MAGE,
        WARRIOR,
        NONE
    }

    private final Class entityClass;
    private final String name;
    private int curHealth, baseHealth;
    private StatusEffect statusEffect;
    
    public boolean isAlive() {
        return curHealth > 0;
    }
    
    
    public static Class getClassFromNumber(int i) {
        switch ( i ) {
            case 1 : return Class.WARRIOR;
            case 2 : return Class.ROGUE;
            case 3 : return Class.MAGE;
            default: return null;
        }
    }
    
    //offsets for drawing purposes
    //these are here in case I need to overwrite them
    public static int getXDrawOffset() {
        return -21;
    }
    public static int getYDrawOffset() {
        return 111;
    }
    public void reduceHealth(int amt) {
        System.out.println("Reducing " + name + "'s health by: " + amt);
        curHealth -= amt;
    }
    

}
