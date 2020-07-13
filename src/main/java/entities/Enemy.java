package entities;


import animation.SpriteAnimation;
import items.Item;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import spells.AutoAttack;
import spells.Spell;
import util.ArmorFileReader;

import java.util.ArrayList;
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
    
    /**
     * Enemy constructor
     * @param entityClass class type of enemy
     * @param name name of enemy
     * @param maxHealth maximum health of the enemy
     * @param level level of the enemy
     * @param model base png model image of the enemy
     * @param biome the biome the enemy belongs to ie. snow/forest/rock ect.
     * @param attackPath string path to the attack animation image
     * @param idlePath string path to the idle animation image
     * @param deathPath string path to the death animation image
     * @param attackFrames int number of frames in the attack animation
     * @param idleFrames int number of frames in the idle animation
     * @param deathFrames int number of frames inthe death animation
     */
    @Builder
    public Enemy(Class entityClass, String name, int maxHealth, int level, Image model, String biome, String attackPath, String idlePath,
                 String deathPath, int attackFrames, int idleFrames, int deathFrames) {
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
        
        attackAnimation = createClassSpriteAnimation(attackPath, attackFrames);
        idleAnimation = createClassSpriteAnimation(idlePath, idleFrames);
        deathAnimation = createClassSpriteAnimation(deathPath, deathFrames);
        attackAnimation.setCycleCount(1);
        idleAnimation.setCycleCount(Animation.INDEFINITE);
        deathAnimation.setCycleCount(1);
    }
    
    /**
     * setup the x,y draw locations for the enemy animations when a battle starts
     * @param group the group to add the animation to
     */
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
    
    /**
     * play the enemy idle animation
     */
    public void playIdleAnimation() {
        hideAttackAnimation();
        idleAnimation.unHide();
        idleAnimation.play();
    }
    
    /**
     * play the enemy attack animation
     */
    public void playAttackAnimation() {
        hideIdleAnimation();
        attackAnimation.unHide();
        attackAnimation.play();
    }
    
    /**
     * play the enemy death animation
     */
    public void playDeathAnimation() {
        hideIdleAnimation();
        deathAnimation.unHide();
        deathAnimation.play();
    }
    
    /**
     * hide the idle animation
     */
    public void hideIdleAnimation() {
        idleAnimation.hide();
    }
    
    /**
     * hide the attack animation
     */
    public void hideAttackAnimation() {
        attackAnimation.hide();
    }
    
    /**
     * helper method to create a sprite animation based on the parameters
     * @param imagePath path to the image of the animation
     * @return a SpriteAnimation instance
     */
    private SpriteAnimation createClassSpriteAnimation(String imagePath, int frames) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setViewport(new Rectangle2D(0, 0, 96, 96));
        return new SpriteAnimation(imageView, Duration.millis(frames * 150), frames, frames, 0, 0, 96, 96);
    }
    /**
     * randomize the enemy hp by the variance and level
     */
    private void randomizeHealth() {
        double variance = 0.3;                      //adjust here for health variance and level scaling
        double levelHealthScaling = 0.2;

        double levelScale = (getLevel() - 1) + (1 * levelHealthScaling);
        double base = getBaseHealth() * (1 + levelScale);

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
        
        final double damageMulti = 3,               //adjust for balancing
                     armorMulti = .35,
                     mageAbsorb = .28,
                     dodgeChance = .2;

        double damageDone = castSpell().getDamageDone() * damageMulti;                          //get base damage done
      //  System.out.println("Enemy base damage: " + damageDone);
        double armorReduction = player.getArmor() * armorMulti;                                 //apply armor reduction
      //  System.out.println("Armor reduced damage by: " + armorReduction);
        damageDone -= armorReduction;
        switch (player.getEntityClass()) {                                                      //if its a rogue or mage apply those damage reductions specific to the class
            case MAGE:
            //    System.out.println("Mage absorbed: " + mageAbsorb * damageDone);
                damageDone -= (mageAbsorb * damageDone);
                break;
            case ROGUE:
                if (rand.nextDouble() <= dodgeChance)
                    damageDone = 0;
             //   System.out.println("The rogue " + (damageDone == 0 ? "did" : "did not") + " dodge");
                break;
            default:
                break;
        }
        if (damageDone < 0)                                                 //no negative damage values
            damageDone = 0;
      //  System.out.println("Final damage done to player is: " + damageDone);
        return damageDone;
    }


}
