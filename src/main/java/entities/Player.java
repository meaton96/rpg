package entities;

import control.FileUtil;
import items.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import resource.Resource;
import spells.Spell;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Player extends Entity {

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
                break;
            case ROGUE:
                System.out.println(getClass().getName());
                model = FileUtil.getResourceStreamFromClass(getClass(), "/images/Rogue/rogue.png");
                resource = new Resource(Resource.Type.ENERGY);
                break;
            case WARRIOR:
                model = FileUtil.getResourceStreamFromClass(getClass(), "/images/Knight/knight.png");
                resource = new Resource(Resource.Type.RAGE);
                break;
            default:
                model = null;
                break;
        }
        
    }
    public void swapDirections() {
        faceForward = !faceForward;
    }
    public void equipWeapon(Weapon weapon) {
        switch (getEntityClass()) {
            case MAGE:
                if (weapon.getType() == Weapon.Type.DAGGER || weapon.getType() == Weapon.Type.SWORD) {
                    System.out.println("Weapon was wrong type");
                    return;
                }
                break;
            case ROGUE:
                if (weapon.getType() == Weapon.Type.STAFF || weapon.getType() == Weapon.Type.SWORD) {
                    System.out.println("Weapon was wrong type");
                    return;
                }
                break;
            case WARRIOR:
                if (weapon.getType() == Weapon.Type.STAFF || weapon.getType() == Weapon.Type.DAGGER) {
                    System.out.println("Weapon was wrong type");
                    return;
                }
                break;
        }
        if (equippedWeapon != null)
            backPack.add(equippedWeapon);
        equippedWeapon = weapon;
    }
    public void levelUp() {
        level++;
    }
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
    public boolean reduceHealth(int amt) {
        setCurHealth(getCurHealth() - amt);
        return getCurHealth() > 0;
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
    public boolean getFaceDir() { return faceForward; }
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
}
