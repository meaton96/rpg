package entities;


import animation.SpriteAnimation;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
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

/**
 * class representing an enemy entity
 */
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
    
    private static final int ANIM_WIDTH = 96, ANIM_HEIGHT = 96;
    
    private final SpriteAnimation attackAnimation;
    private final SpriteAnimation idleAnimation;
    private final SpriteAnimation deathAnimation;

    /*public Enemy(Class entityClass, String name, int maxHealth, List<Spell> availableSpells, List<Item> drops, int level, Image model, String biome) {
        super(entityClass, name, maxHealth, maxHealth, null);
        this.availableSpells = Collections.unmodifiableList(availableSpells);
        this.drops = drops;
        this.level = level;
        this.biome = biome;
        this.model = model;
        rand = new Random();
    }*/
    @Builder
    public Enemy(Class entityClass, String name, int maxHealth, int level, Image model, String biome, String attackPath, String idlePath, String deathPath) {
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
        
        attackAnimation = createClassSpriteAnimation(attackPath);
        idleAnimation = createClassSpriteAnimation(idlePath);
        deathAnimation = createClassSpriteAnimation(deathPath);
        attackAnimation.setCycleCount(1);
        idleAnimation.setCycleCount(Animation.INDEFINITE);
        deathAnimation.setCycleCount(1);
    }
    public void initAnimationLocation(Group group) {
        attackAnimation.setScene(group);
        attackAnimation.setLoc(xLoc, yLoc);
        deathAnimation.setScene(group);
        deathAnimation.setLoc(xLoc, yLoc);
        idleAnimation.setScene(group);
        idleAnimation.setLoc(xLoc, yLoc);
        attackAnimation.hide();
        deathAnimation.hide();
        idleAnimation.hide();
        
        
    }
    public void playIdleAnimation() {
        hideAttackAnimation();
        idleAnimation.unHide();
        idleAnimation.play();
    }
    public void playAttackAnimation() {
        hideIdleAnimation();
        attackAnimation.unHide();
        attackAnimation.play();
    }
    public void playDeathAnimation() {
        hideIdleAnimation();
        deathAnimation.unHide();
        deathAnimation.play();
    }
    public void hideIdleAnimation() {
        idleAnimation.hide();
    }
    public void hideAttackAnimation() {
        attackAnimation.hide();
    }
    
    /**
     * helper method to create a sprite animation based on the parameters
     * @param imagePath path to the image of the animation
     * @return a SpriteAnimation instance
     */
    private SpriteAnimation createClassSpriteAnimation(String imagePath) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setViewport(new Rectangle2D(0, 0, 48, 48));
        return new SpriteAnimation(imageView, Duration.millis(600), 4, 4, 0, 0, 48, 48);
    }
    /**
     * randomize the enemy hp by the variance and level
     */
    private void randomizeHealth() {
        double variance = 0.3;                      //adjust here for health variance and level scaling
        double levelHealthScaling = 0.2;

        double levelScale = (getLevel() - 1) + (1 + levelHealthScaling);
        double base = levelScale * getBaseHealth();

        double healthMin = getBaseHealth() - variance * base;
        double healthMax = getBaseHealth() + variance * base;

        double health = rand.nextInt((int)Math.round(healthMax - healthMin)) + healthMin;


        setBaseHealth((int)health);
        setCurHealth((int)health);
    }
    
    /**
     * cast a random spell the enemy has, currently just will always be a basic attack
     * @return the spell to be cast
     */
    public Spell castSpell() {
        return availableSpells.get(rand.nextInt(availableSpells.size()));
    }
    public double getDamageDone(Player player) {
        final double damageMulti = 30;
        final double armorMulti = .35;
        final double mageAbsorb = .28;
        final double dodgeChance = .4;

        double damageDone = castSpell().getDamageDone() * damageMulti;
        System.out.println("Enemy base damage: " + damageDone);
        double armorReduction = player.getArmor() * armorMulti;
        System.out.println("Armor reduced damage by: " + armorReduction);
        damageDone -= armorReduction;
        switch (player.getEntityClass()) {
            case MAGE:
                System.out.println("Mage absorbed: " + mageAbsorb * damageDone);
                damageDone -= (mageAbsorb * damageDone);
                break;
            case ROGUE:
                Random rand = new Random();
                if (rand.nextDouble() <= dodgeChance)
                    damageDone = 0;
                System.out.println("The rogue " + (damageDone == 0 ? "did" : "did not") + " dodge");
                break;
            default:
                break;
        }
        if (damageDone < 0)
            damageDone = 0;
        System.out.println("Final damage done to player is: " + damageDone);
        return damageDone;
    }


}
