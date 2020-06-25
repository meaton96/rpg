package spells;


import lombok.Value;

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


}
