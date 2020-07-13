package spells;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.Random;

/**
 * class representing a single spell/ability that the player or enemy can use
 */
@Value
@EqualsAndHashCode
@Builder
@NonFinal
public class Spell {

    public enum DamageType {
        PHYSICAL,
        FROST,
        FIRE,
        SHADOW,
        AIR
    }

    String name;
    int requiredLevel;
    DamageType damageType;
    StatusEffect statusEffect;
    double damageLow, damageHigh, setStatusChance;
    int cost;
    String styleID;
    
    public int getDamageDone() {
        Random r = new Random();
        return (int)(r.nextInt((int)Math.round(damageHigh - damageLow)) + damageLow);
    }
    public String toString() {
        return name + ": " + damageLow + "-" + damageHigh;
    }

}
