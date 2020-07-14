package entities;

import animation.SpriteAnimation;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import spells.AutoAttack;
import ui.Backpack;
import util.FileUtil;
import items.*;
import javafx.scene.image.Image;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import resource.Resource;
import spells.Spell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * class representing the player character
 */

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
    
    
    @Getter private Spell[] equippedSpells;
    private List<Spell> learnedSpells;
    private Resource resource;
    @Getter  private Image model;
    
    @Getter @Setter private double xLoc, yLoc;
    private boolean faceForward;
    @Getter private int level;
    @Getter @Setter private boolean inBattle = false;
    @Getter @Setter private boolean isTurn;
    @Getter private final Backpack backPack;
    
    
    //animation constants
    private static final int ANIM_X_OFFSET = 0;
    private static final int ANIM_Y_OFFSET = 0;
    private static final int ANIM_WIDTH = 128;
    private static final int ANIM_HEIGHT = 128;

    private final HashMap<String, SpriteAnimation> animations;
    
    
    public Player(Class chosenClass, String name, int health) {
        super(chosenClass, name, health, health, null);
        animations = new LinkedHashMap<>();
        
        learnedSpells = new ArrayList<>();
        equippedSpells = new Spell[4];
        faceForward = true;
        level = 1;
        switch (chosenClass) {
            case MAGE:
                model = FileUtil.getResourceStreamFromClass(getClass(), "/images/Mage/mage.png");
                resource = new Resource(Resource.Type.MANA);
                animations.put("attack", createClassSpriteAnimation("/images/Mage/Attack/attack.png", 7, Duration.millis(1000), 7));
                animations.put("idle", createClassSpriteAnimation("/images/Mage/Idle/idle.png", 14, Duration.millis(2000), 14));
                animations.put("walking", createClassSpriteAnimation("/images/Mage/Walk/walk.png", 6, Duration.millis(700), 6));
                animations.put("death", createClassSpriteAnimation("/images/Mage/Death/death.png", 10, Duration.millis(1100), 10));
                animations.put("hurt", createClassSpriteAnimation("images/Mage/Hurt/hurt.png", 4, Duration.millis(550), 4));
                break;
            case ROGUE:
                model = FileUtil.getResourceStreamFromClass(getClass(), "/images/Rogue/rogue.png");
                animations.put("attack", createClassSpriteAnimation("/images/Rogue/Attack/attack.png", 7, Duration.millis(1000), 7));
                animations.put("idle", createClassSpriteAnimation("/images/Rogue/Idle/idle.png", 18, Duration.millis(2500), 18));
                animations.put("walking", createClassSpriteAnimation("/images/Rogue/Walk/walk.png", 6, Duration.millis(700), 6));
                animations.put("death", createClassSpriteAnimation("/images/Rogue/Death/death.png", 10, Duration.millis(1100), 10));
                animations.put("hurt", createClassSpriteAnimation("images/Rogue/Hurt/hurt_t.png", 6, Duration.millis(800), 6));
                resource = new Resource(Resource.Type.ENERGY);
                break;
            case WARRIOR:
                model = FileUtil.getResourceStreamFromClass(getClass(), "/images/Knight/knight.png");
                resource = new Resource(Resource.Type.RAGE);
                animations.put("attack", createClassSpriteAnimation("/images/Knight/Attack/attack.png", 7, Duration.millis(1000), 7));
                animations.put("idle", createClassSpriteAnimation("/images/Knight/Idle/idle.png", 12, Duration.millis(1700), 12));
                animations.put("walking", createClassSpriteAnimation("/images/Knight/Walk/walk.png", 6, Duration.millis(700), 6));
                animations.put("death", createClassSpriteAnimation("/images/Knight/Death/death.png", 10, Duration.millis(1100), 10));
                animations.put("hurt", createClassSpriteAnimation("images/Knight/Hurt/hurt.png", 4, Duration.millis(550), 4));
                break;
            default:
                model = null;
                break;
        }
        backPack = new Backpack(this);
        animations.get("attack").setCycleCount(1);
        animations.get("idle").setCycleCount(Animation.INDEFINITE);

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
    public SpriteAnimation getAnimationByName(String name) {
        return animations.get(name);
    }
    public ImageView getImageViewByName(String name) {
        return animations.get(name).getImageView();
    }
    
    /**
     * animation helpers
     * @param group animation
     */
    public void playAttackAnimation(Group group) {
        animations.get("idle").hide();

        animations.get("attack").setScene(group);
        animations.get("attack").setLoc(xLoc, yLoc);
        animations.get("attack").play();

    }
    public void playDeathAnimation(Group group) {
        animations.get("idle").hide();
        animations.get("death").setScene(group);
        animations.get("death").setLoc(xLoc, yLoc);
        animations.get("death").play();
    }
    public void initWalkingAnim(Group group) {
        animations.get("walking").setScene(group);
        animations.get("walking").setLoc(xLoc, yLoc);
        animations.get("hurt").setOnFinished(e -> {
            animations.get("hurt").hide();
            playIdleFromStart();
        });
    }
    public void startWalking() {
        animations.get("walking").play();
    }
    public void pauseWalking() {
        animations.get("walking").pause();
    }
    public void hideWalkingAnimation() {
        animations.get("walking").hide();
    }
    public void unHideWalkingAnimation() {
        animations.get("walking").unHide();
    }
    public void unHideBattleAnimations() {
        animations.get("attack").unHide();
    }
    public void hideAttackAnimation() { animations.get("attack").hide(); }
    public void playIdleFromStart() {

        animations.get("idle").unHide();                     //restart idle animation
        hideAttackAnimation();
        animations.get("idle").playFromStart();
    }
    public void setAttackOnFinish(EventHandler<ActionEvent> event) {
        animations.get("attack").setOnFinished(event);
    }
    public void playIdleAnimation(Group group) {

        animations.get("idle").setLoc(xLoc, yLoc);
        animations.get("idle").setScene(group);
        animations.get("idle").unHide();
        animations.get("idle").play();
    }
    public void playHurtAnimation(Group group) {
        animations.get("hurt").setScene(group);
        animations.get("hurt").setLoc(xLoc, yLoc);
        animations.get("idle").hide();
        animations.get("hurt").unHide();
        animations.get("hurt").playFromStart();
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
     * determines whether or not an item is equippable
     * @param i the item to be tested
     * @return true if the item is the right type for the class and is not too high level
     */
    public boolean canEquip(Item i) {
        if (i.getLevel() > level)
            return false;
        return i.typeMatchPlayer(this);
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
        xLoc += 3.5;
        animations.get("walking").setLoc(xLoc, yLoc);
    }
    public void moveForward(int amt) {
        xLoc =+ amt;
    }
    public String getWeaponIconId() {
        return equippedWeapon.getIconId();
    }
    public Resource.Type getResorceType() {
        return resource.getType();
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
                    resource.generateResource(10);
            break;
            default:
            break;
        }
        return baseDamage;
    }
    public void giveItem(Item i) {
        backPack.add(i);
    }
    public void updateBackpack() {
        backPack.updateBackpack();
        backPack.updateCharacterWindow();
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
    public int getArmor() {
        int armor = 0;
        armor += equippedBoots.getArmor();
        armor += equippedChest.getArmor();
        armor += equippedHelm.getArmor();
        armor += equippedLegs.getArmor();
        return armor;
    }
    public void heal() {
        setCurHealth(getMaxHealth());
    }
    
    
    public int getMaxHealth() {
        return super.getBaseHealth() + (int)(getStamina() * STAMINA_SCALING);
    }
    
}
