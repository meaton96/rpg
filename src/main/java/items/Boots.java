package items;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * class representing a boots armor piece
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Boots extends Armor {

    @Builder
    public Boots(String name, double weight, double durability, int level, double armor, Type type,
                 int stamina, int strength, int agility, int intellect, String iconId) {
        super(name, weight, durability, level, armor, type, stamina, strength, agility, intellect, iconId);
    }
    
    @Override
    public Boots scaleItem(int level) {
        double armor = 0;
        double stamina = 0;
        double agility = 0;
        double strength = 0;
        double intellect = 0;
        
        super.scaleItem(level);
        armor = getArmor() + 1.2 * (level - 1);
        stamina = getStamina() + 1.1 * (level - 1);
        if (getAgility() != 0)
            agility = getAgility() + 1.4 * (level - 1);
        else if (getIntellect() != 0)
            intellect = getIntellect() + 1.4 * (level - 1);
        else if (getStrength() != 0)
            strength = getStrength() * 1.4 + (level - 1);
        
        return Boots.builder()
                .agility((int)(Math.round(agility)))
                .armor(armor)
                .strength((int)(Math.round(strength)))
                .durability(getDurability())
                .weight(getWeight())
                .intellect((int)Math.round(intellect))
                .level(level)
                .iconId(getIconId())
                .type(getType())
                .name("Level " + level + " " + getType().name().toLowerCase() + " boots")
                .stamina((int)Math.round(stamina))
                .build();
    }
}
