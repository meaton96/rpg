package items;

import entities.Player;
import javafx.scene.image.Image;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * parent class for all different types of equip able armor pieces
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
public abstract class Armor extends Item {

    public Armor(String name, double weight, double durability, int level, double armor, Type type,
                 int stamina, int strength, int agility, int intellect, String iconId) {
        super(name, weight, durability, level, stamina, strength, agility, intellect, iconId);
        this.armor = armor;
        this.type = type;
    }

    public enum Type {
        CLOTH,
        LEATHER,
        PLATE
    }

    private final double armor;
    private final Type type;

    public static Type getTypeFromString(String typeString) {
        typeString = typeString.toLowerCase();
        if (typeString.equals("cloth"))
            return Type.CLOTH;
        if (typeString.equals("leather"))
            return Type.LEATHER;
        if (typeString.equals("plate"))
            return Type.PLATE;
        return null;
    }
    public boolean typeMatchPlayer(Player p) {
        switch (p.getEntityClass()) {
            case WARRIOR: return type == Type.PLATE;
            case MAGE: return type == Type.CLOTH;
            case ROGUE: return type == Type.LEATHER;
        }
        return false;
    }
    
}
