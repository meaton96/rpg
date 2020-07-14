package items;

import entities.Player;
import javafx.scene.image.Image;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.NonFinal;

/**
 * class representing a weapon object
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@NonFinal
public class Weapon extends Item {
    
    /**
     * Weapon constructor
     * @param name of the weapon
     * @param weight weight of the weapon
     * @param durability durability of the weapon
     * @param level level requirement to equip the weapon
     * @param damageLow the minimum damage of the weapon
     * @param damageHigh the maximum damage of the weapon
     * @param type the type of the weapon either staff dagger or sword
     * @param agility the agility value of the weapon only found on daggers
     * @param stamina the stamina value of the weapon found on all types
     * @param strength the strength value of the weapon found on swords
     * @param intellect the intellect value of the weapon found only on staves
     * @param iconId the id of the weapon, used to drawing the icon, icon id matches id from the css file
     */
    @Builder
    public Weapon(String name, double weight, double durability, int level, double damageLow, double damageHigh,
                  Type type, int agility, int stamina, int strength, int intellect, String iconId) {
        super(name, weight, durability, level, stamina,strength,agility,intellect, iconId);
        this.damageHigh = damageHigh;
        this.damageLow = damageLow;
        this.type = type;
    }

    public enum Type {
        STAFF,
        DAGGER,
        SWORD
    }

    private final double damageLow, damageHigh;
    private final Type type;

    public static Type getTypeFromNumber(int typeNum) {
        switch (typeNum) {
            case 1:
                return Type.STAFF;
            case 2:
                return Type.DAGGER;
            case 3:
                return Type.SWORD;
            default:
                return null;
        }
    }
    public static Type getTypeFromString(String typeString) {
        typeString = typeString.toLowerCase();
        if (typeString.equals("staff"))
            return Type.STAFF;
        if (typeString.equals("dagger"))
            return Type.DAGGER;
        if (typeString.equals("sword"))
            return Type.SWORD;
        return null;
    }
    
    /**
     * check if the type matches the player class
     * @param p the player to check the class of
     * @return true if the player can equip the weapon
     */
    public boolean typeMatchPlayer(Player p) {
        switch (p.getEntityClass()) {
            case WARRIOR: return type == Type.SWORD;
            case MAGE: return type == Type.STAFF;
            case ROGUE: return type == Type.DAGGER;
        }
        return false;
    }
    @Override
    public Weapon scaleItem(int level) {
        double stamina = 0;
        double agility = 0;
        double strength = 0;
        double intellect = 0;
        double damageLow = 0;
        double damageHigh = 0;
        super.scaleItem(level);
        
        damageLow = getDamageLow() + ((level - 1) * 1.2);
        damageHigh = getDamageHigh() + ((level - 1) * 2.4);
        
        String name = "Level " + level + " " + getType().name().toLowerCase();
        switch (getType()) {
            case STAFF: name = name + " staff";
            break;
            case SWORD: name = name + " sword";
            break;
            case DAGGER: name = name + " dagger";
            break;
        }
        
        stamina = getStamina() + 1.1 * (level - 1);
        if (getAgility() != 0)
            agility = getAgility() + 1.1 * (level - 1);
        else if (getIntellect() != 0)
            intellect = getIntellect() + 1.1 * (level - 1);
        else if (getStrength() != 0)
            strength = getStrength() * 1.1 + (level - 1);
        
        return Weapon.builder()
                .agility((int)(Math.round(agility)))
                .strength((int)(Math.round(strength)))
                .durability(getDurability())
                .weight(getWeight())
                .intellect((int)Math.round(intellect))
                .level(level)
                .iconId(getIconId())
                .type(getType())
                .name(name)
                .stamina((int)Math.round(stamina))
                .damageHigh(damageHigh)
                .damageLow(damageLow)
                .build();
    }

}
