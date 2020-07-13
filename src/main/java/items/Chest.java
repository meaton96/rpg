package items;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * class representing a chest type armor piece
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Chest extends Armor {

    @Builder
    public Chest(String name, double weight, double durability, int level, double armor, Type type,
                 int stamina, int strength, int agility, int intellect, String iconId) {
        super(name, weight, durability, level, armor, type, stamina, strength, agility, intellect, iconId);
    }

}
