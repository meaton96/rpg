package entities;


import control.ArmorFileReader;
import items.Item;
import javafx.scene.image.Image;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import spells.AutoAttack;
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
    
    private final Image model;
    private double xLoc, yLoc;

    @Builder
    public Enemy(Class entityClass, String name, int maxHealth, List<Spell> availableSpells, List<Item> drops, int level, Image model) {
        super(entityClass, name, maxHealth, maxHealth, null);
        this.availableSpells = Collections.unmodifiableList(availableSpells);
        this.drops = drops;
        this.level = level;
        this.model = model;
        rand = new Random();
    }
    public Enemy(Class entityClass, String name, int maxHealth, int level, Image model) {
        super(entityClass, name, maxHealth, maxHealth, null);
        this.availableSpells = new ArrayList<>();
        Spell.DamageType damageType;
        switch (entityClass) {
            case MAGE: damageType = Spell.DamageType.AIR;
            break;
            case ROGUE:
            case WARRIOR:
            default: damageType = Spell.DamageType.PHYSICAL;
        }
        availableSpells.add(new AutoAttack(damageType, ArmorFileReader.getWeaponByName("Starting Sword")));
        this.drops = ArmorFileReader.getArmorForLevel(level);
        this.level = level;
        this.model = model;
        rand = new Random();
    }

    public Spell castSpell() {
        return availableSpells.get(rand.nextInt(availableSpells.size()));
    }


}
