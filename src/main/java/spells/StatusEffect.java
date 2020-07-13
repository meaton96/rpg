package spells;


import lombok.Value;

/**
 * class representing a status effect ie. freeze/set fire
 * not currently implemented
 */
@Value
public class StatusEffect {

    public enum Type {
        KNOCKDOWN,
        FREEZE,
        SET_FIRE,
        STUN,
        FEAR,
        NONE
    }

    Type type;
    int duration;

    public static StatusEffect NONE() {
        return new StatusEffect(Type.NONE, 0);
    }
}
