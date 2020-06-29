package entities;

import javafx.scene.image.Image;
import lombok.*;
import spells.StatusEffect;

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
    private int curHealth, maxHealth;
    private StatusEffect statusEffect;
    
    public static Class getClassFromNumber(int i) {
        switch ( i ) {
            case 1 : return Class.WARRIOR;
            case 2 : return Class.ROGUE;
            case 3 : return Class.MAGE;
            default: return null;
        }
    }
    
    //these are here in case I need to overwrite them
    public static int getXDrawOffset() {
        return 21;
    }
    public static int getYDrawOffset() {
        //54 from top
        //17 from bottom
        return 37;
    }

}
