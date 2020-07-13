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

}
