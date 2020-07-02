package spells;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.Random;

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
    
    public double getDamageDone() {
        Random r = new Random();
        return (r.nextInt((int)Math.round(damageHigh - damageLow)) + damageLow);
    }

}
