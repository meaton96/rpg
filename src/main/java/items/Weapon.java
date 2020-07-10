package items;

import entities.Player;
import javafx.scene.image.Image;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.NonFinal;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@NonFinal
public class Weapon extends Item {

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
    public boolean typeMatchPlayer(Player p) {
        switch (p.getEntityClass()) {
            case WARRIOR: return type == Type.SWORD;
            case MAGE: return type == Type.STAFF;
            case ROGUE: return type == Type.DAGGER;
        }
        return false;
    }

}
