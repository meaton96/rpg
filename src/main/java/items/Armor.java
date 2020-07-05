package items;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
public abstract class Armor extends Item {

    public Armor(String name, double weight, double durability, int level, double armor, Type type,
                 int stamina, int strength, int agility, int intellect) {
        super(name, weight, durability, level, stamina, strength, agility, intellect);
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
    

    public static Type getTypeFromNumber(int typeNum) {
        switch (typeNum) {
            case 1:
                return Type.CLOTH;
            case 2:
                return Type.LEATHER;
            case 3:
                return Type.PLATE;
            default:
                return null;
        }
    }
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
}
