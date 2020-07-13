package items;

import entities.Player;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * class representing an item object, base class to be extended
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class Item {

    private final String name;
    private final double weight;
    private final double durability;
    private final int level;
    private final int stamina, strength, agility, intellect;
    private final String iconId;

    public boolean typeMatchPlayer(Player p) {return true;}


}
