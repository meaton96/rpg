package spells;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@EqualsAndHashCode
@Builder
@NonFinal
public class Spell {

    enum DamageType {
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

}
