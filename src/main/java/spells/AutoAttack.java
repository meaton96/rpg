package spells;

import items.Weapon;

/**
 * subclass of spell difference the damage of the spell is based on a weapon's damage
 * the weapon is passed in to the constructor and the damage of the weapon is passed to the parent class
 */
public class AutoAttack extends Spell{
    

    
    public AutoAttack(DamageType damageType, Weapon weapon){
        super("Auto Attack", 0, damageType, StatusEffect.NONE(), weapon.getDamageLow(), weapon.getDamageHigh(), 0, 0, "");
    }
}
