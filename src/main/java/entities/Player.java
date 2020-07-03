package entities;

import animation.SpriteAnimation;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import spells.AutoAttack;
import util.FileUtil;
import items.*;
import javafx.scene.image.Image;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import resource.Resource;
import spells.Spell;

import java.util.ArrayList;
import java.util.List;

/**
 * class representing the player character
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Player extends Entity {
    
    public final double MAIN_STAT_SCALING = 1.0;
    public final double STAMINA_SCALING = 3;
    private final static int HEALTH_LEVEL_SCALING = 8;

    private Chest equippedChest;
    private Helm equippedHelm;
    private Legs equippedLegs;
    private Boots equippedBoots;

    private Weapon equippedWeapon;
    private Weapon equippedOffHand;
    
    private List<Item> backPack;
    private Spell[] equippedSpells;
    private List<Spell> learnedSpells;
    private Resource resource;
    private Image model;
    
    private double xLoc, yLoc;
    private boolean faceForward;
    private int level;
    private boolean inBattle = false;
    private boolean isTurn;
    
    
    //animation constants
    private static final int ATTACK_COL = 5;
    private static final int IDLE_COL = 18;
    private static final int ANIM_X_OFFSET = 0;
    private static final int ANIM_Y_OFFSET = 0;
    private static final int ANIM_WIDTH = 128;
    private static final int ANIM_HEIGHT = 128;
    private final SpriteAnimation attackAnimation;
    private final SpriteAnimation idleAnimation;
    
    
    public Player(Class chosenClass, String name, int health) {
        super(chosenClass, name, health, health, null);
    
        backPack = new ArrayList<>();
        learnedSpells = new ArrayList<>();
        equippedSpells = new Spell[4];
        faceForward = true;
        level = 1;
        switch (chosenClass) {
            case MAGE:
                model = FileUtil.getResourceStreamFromClass(getClass(), "/images/Mage/mage.png");
                resource = new Resource(Resource.Type.MANA);
                attackAnimation = createClassSpriteAnimation("/images/Mage/Attack/attack.png", 7, Duration.millis(1000), 7);
                idleAnimation = null;
                break;
            case ROGUE:
                model = FileUtil.getResourceStreamFromClass(getClass(), "/images/Rogue/rogue.png");
                resource = new Resource(Resource.Type.ENERGY);
                attackAnimation = createClassSpriteAnimation("/images/Rogue/Attack/attack.png", 7, Duration.millis(1000), 7);
                idleAnimation = createClassSpriteAnimation("/images/Rogue/Idle/idle.png", 18, Duration.millis(2400), IDLE_COL);
                break;
            case WARRIOR:
                model = FileUtil.getResourceStreamFromClass(getClass(), "/images/Knight/knight.png");
                resource = new Resource(Resource.Type.RAGE);
                attackAnimation = createClassSpriteAnimation("/images/Knight/Attack/attack.png", 5, Duration.millis(1000), ATTACK_COL);
                idleAnimation = null;
                break;
            default:
                model = null;
                attackAnimation = null;
                idleAnimation = null;
                break;
        }

        attackAnimation.setCycleCount(2);
        idleAnimation.setCycleCount(Animation.INDEFINITE);
    
        
        
        
    }
    
    /**
     * helper method to create a sprite animation based on the parameters
     * @param imagePath path to the image of the animation
     * @param frameCount number of frames of the animation
     * @param duration how long the frame takes to play
     * @return a SpriteAnimation instance
     */
    private SpriteAnimation createClassSpriteAnimation(String imagePath, int frameCount, Duration duration, int numCol) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setViewport(new Rectangle2D(0, 0, ANIM_WIDTH, ANIM_HEIGHT));
        return new SpriteAnimation(imageView, duration, frameCount, numCol, ANIM_X_OFFSET, ANIM_Y_OFFSET, ANIM_WIDTH, ANIM_HEIGHT);
    }
    
    /**
     * tell player to play their attack animation
     */
    public void playAttackAnimation(Group group) {

        /*
        ImageView imageView = attackAnimation.getImageView();
        imageView.setLayoutY(yLoc);
        imageView.setLayoutX(xLoc);
        group.getChildren().add(imageView);
        attackAnimation.play();*/

        attackAnimation.play(group, xLoc, yLoc);
    }
    public void playIdleAnimation(Group group) {/*
        ImageView imageView = idleAnimation.getImageView();
        imageView.setLayoutX(xLoc);
        imageView.setLayoutY(yLoc);
        group.getChildren().add(imageView);
        idleAnimation.play();*/

        idleAnimation.play(group, xLoc, yLoc);
    }
    
    /**
     * swap the player direction
     * for future implementation if you want the player to ever move the other direction
     */
    public void swapDirections() {
        faceForward = !faceForward;
    }
    
    /**
     * attempt to equip a weapon to the player, also sets the auto attack
     * @param weapon the weapon to equip
     */
    public void equipWeapon(Weapon weapon) {
        switch (getEntityClass()) {
            case MAGE:
                if (weapon.getType() != Weapon.Type.STAFF) {
                    
                    System.out.println("Weapon was wrong type");
                    return;
                }
                break;
            case ROGUE:
                if (weapon.getType() != Weapon.Type.DAGGER) {
                    System.out.println("Weapon was wrong type");
                    return;
                }
                break;
            case WARRIOR:
                if (weapon.getType() != Weapon.Type.SWORD) {
                    System.out.println("Weapon was wrong type");
                    return;
                }
                break;
        }
        if (equippedWeapon != null)
            backPack.add(equippedWeapon);
        equippedWeapon = weapon;
        equippedSpells[0] = new AutoAttack(Spell.DamageType.PHYSICAL, equippedWeapon);
    }
    
    /**
     * level up the player
     */
    public void levelUp() {
        setBaseHealth(getBaseHealth() + HEALTH_LEVEL_SCALING);
        level++;
    }
    
    /**
     * attempt to equip the piece of armor
     * @param item the armor to equip
     */
    public void equipArmor(Armor item) {
        switch (getEntityClass()) {
            case MAGE:
                if (item.getType() != Armor.Type.CLOTH) {
                    System.out.println("Item is wrong armor type");
                    return;
                }
                break;
            case ROGUE:
                if (item.getType() != Armor.Type.LEATHER) {
                    System.out.println("Item is wrong armor type");
                    return;
                }
                break;
            case WARRIOR:
                if (item.getType() != Armor.Type.PLATE) {
                    System.out.println("Item is wrong armor type");
                    return;
                }
                break;
        }
        if (item instanceof Chest) {
            if (equippedChest != null)
                backPack.add(equippedChest);
            equippedChest = (Chest) item;
        } else if (item instanceof Helm) {
            if (equippedHelm != null)
                backPack.add(equippedHelm);
            equippedHelm = (Helm) item;
        } else if (item instanceof Legs) {
            if (equippedLegs != null)
                backPack.add(equippedLegs);
            equippedLegs = (Legs) item;
        } else if (item instanceof Boots) {
            if (equippedBoots != null)
                backPack.add(equippedBoots);
            equippedBoots = (Boots) item;
        }
    }
    
    public List<Item> getEquippedItems() {
        return List.of(equippedWeapon, equippedHelm, equippedChest, equippedLegs, equippedBoots);
    }
    
    
    public int getMaxResource() {
        return resource.getMaxAmount();
    }
    public int getCurrentResource() {
        return resource.getCurrentAmount();
    }
    
    public void moveForward() {
        xLoc += 5;
    }
    public void moveForward(int amt) {
        xLoc =+ amt;
    }
    
    public String toString() {
        return "Name: " + getName() +
                "\nEquipped Gear: " +
                "\nChest: " + equippedChest.getName() +
                "\nHelm: " + equippedHelm.getName() +
                "\nLegs: " + equippedLegs.getName() +
                "\nBoots: " + equippedBoots.getName() +
                "\nWeapon: " + equippedWeapon.getName() +
                "\nSpells learned: \n" + learnedSpells +
                "\n\nBackpack items: " + backPack;
    }
    
    /**
     * gets the amount of damage that a spell of the given name will do
     * this is based off the base damage of the spell and the main stat of the player
     * @param s String spellname
     * @return amount of damage the spell does
     */
    public int getDamFromSpellName(String s) {
        int baseDamage = -1;
        System.out.println(equippedSpells[0]);
        System.out.println(s);
        for (Spell spell : equippedSpells) {
            if (spell != null) {
                if (spell.getName().equals(s)) {
                    baseDamage = spell.getDamageDone();
                }
            }
        }
        switch (getEntityClass()) {
            case ROGUE: baseDamage += getAgility() * MAIN_STAT_SCALING;
            break;
            case MAGE: baseDamage += getIntellect() * MAIN_STAT_SCALING;
            break;
            case WARRIOR: baseDamage += getStrength() * MAIN_STAT_SCALING;
            break;
            default:
            break;
        }
        return baseDamage;
    }
    
    public int getStamina() {
        int stamina = 0;
        stamina += equippedBoots.getStamina();
        stamina += equippedChest.getStamina();
        stamina += equippedHelm.getStamina();
        stamina += equippedLegs.getStamina();
        stamina += equippedWeapon.getStamina();
        return stamina;
    }
    public int getStrength() {
        int str = 0;
        str += equippedBoots.getStrength();
        str += equippedChest.getStrength();
        str += equippedHelm.getStrength();
        str += equippedLegs.getStrength();
        str += equippedWeapon.getStrength();
        return str;
    }
    public int getAgility() {
        int agi = 0;
        agi += equippedBoots.getAgility();
        agi += equippedChest.getAgility();
        agi += equippedHelm.getAgility();
        agi += equippedLegs.getAgility();
        agi += equippedWeapon.getAgility();
        return agi;
    }
    public int getIntellect() {
        int intel = 0;
        intel += equippedBoots.getIntellect();
        intel += equippedChest.getIntellect();
        intel += equippedHelm.getIntellect();
        intel += equippedLegs.getIntellect();
        intel += equippedWeapon.getIntellect();
        return intel;
    }
    public void heal() {
        setCurHealth(getMaxHealth());
    }
    
    
    public int getMaxHealth() {
        return super.getBaseHealth() + (int)(getStamina() * STAMINA_SCALING);
    }
    
}
