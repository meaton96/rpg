package entities;


import util.ArmorFileReader;
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
    private final String biome;
    private final Image model;
    private double xLoc, yLoc;

    public Enemy(Class entityClass, String name, int maxHealth, List<Spell> availableSpells, List<Item> drops, int level, Image model, String biome) {
        super(entityClass, name, maxHealth, maxHealth, null);
        this.availableSpells = Collections.unmodifiableList(availableSpells);
        this.drops = drops;
        this.level = level;
        this.biome = biome;
        this.model = model;
        rand = new Random();
    }
    @Builder
    public Enemy(Class entityClass, String name, int maxHealth, int level, Image model, String biome) {
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
        this.biome = biome;
        rand = new Random();
        randomizeHealth();
    }
    private void randomizeHealth() {
        double variance = 0.3;                      //adjust here for health variance and level scaling
        double levelHealthScaling = 0.2;

        double healthMin = getMaxHealth() - (variance * getMaxHealth());
        double healthMax = getMaxHealth() + variance * getMaxHealth();

        double health = rand.nextInt((int)Math.round(healthMax - healthMin)) + healthMin;
        double levelScale = (getLevel() - 1) * (1 + levelHealthScaling) * health;
        health += levelScale;
        setMaxHealth((int)health);
        setCurHealth((int)health);
    }

    public Spell castSpell() {
        return availableSpells.get(rand.nextInt(availableSpells.size()));
    }
    public boolean reduceHealth(int amt) {
        setCurHealth(getCurHealth() - amt);
        if (getCurHealth() <= 0)
            return false;
        return true;
    }


}
