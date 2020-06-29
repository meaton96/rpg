package spells;

import items.Weapon;

public class AutoAttack extends Spell{
    
    /*String name;
    int requiredLevel;
    DamageType damageType;
    StatusEffect statusEffect;
    double damageLow, damageHigh, setStatusChance;
    int cost;
    */
    
    public AutoAttack(DamageType damageType, Weapon weapon){
        super("Auto Attack", 0, damageType, StatusEffect.NONE(), weapon.getDamageLow(), weapon.getDamageHigh(), 0, 0, "");
    }
}
