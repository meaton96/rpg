package items;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Helm extends Armor {

    @Builder
    public Helm(String name, double weight, double durability, int level, double armor, Type type,
                int stamina, int strength, int agility, int intellect) {
        super(name, weight, durability, level, armor, type, stamina, strength, agility, intellect);
    }
}
