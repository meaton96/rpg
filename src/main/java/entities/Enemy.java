package entities;


import items.Item;
import javafx.scene.image.Image;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import spells.Spell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class Enemy extends Entity {

    private List<Spell> availableSpells;
    private List<Item> drops;
    private int level;
    private final Random rand;

    @Builder
    public Enemy(Class entityClass, String name, int maxHealth, ArrayList<Spell> availableSpells, ArrayList<Item> drops, int level, Image model) {
        super(entityClass, name, maxHealth, maxHealth, null, model);
        this.availableSpells = Collections.unmodifiableList(availableSpells);
        this.drops = Collections.unmodifiableList(drops);
        this.level = level;
        rand = new Random();
    }

    public Spell castSpell() {
        return availableSpells.get(rand.nextInt(availableSpells.size()));
    }

}
