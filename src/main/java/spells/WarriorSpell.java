package spells;

import lombok.Builder;
import lombok.EqualsAndHashCode;

/**
 * child class of spell only difference is that it has a rage generation amount
 */
@EqualsAndHashCode(callSuper = true)
public class WarriorSpell extends Spell {

    private final int rageGen;

    @Builder(builderMethodName = "warriorSpellBuilder")
    public WarriorSpell(String name, int requiredLevel, DamageType damageType, StatusEffect statusEffect, double damageLow, double damageHigh,
                        double setStatusChance, int cost, int rageGen, String styleID) {
        super(name, requiredLevel, damageType, statusEffect, damageLow, damageHigh, setStatusChance, cost, styleID);
        this.rageGen = rageGen;
    }
    

}
