package entities;

import items.*;
import javafx.scene.image.Image;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import resource.Resource;
import spells.Spell;

import java.util.ArrayList;
import java.util.Collections;
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
    private List<Spell> equippedSpells;
    private List<Spell> learnedSpells;
    private Resource resource;

    @Builder
    public Player(Chest chest, Helm helm, Legs legs, Boots boots, Weapon weapon, int health, String name, Class chosenClass, Image model) {
        super(chosenClass, name, health, health, null, model);
        equippedBoots = boots;
        equippedChest = chest;
        equippedHelm = helm;
        equippedLegs = legs;
        equippedWeapon = weapon;

        backPack = new ArrayList<>();
        learnedSpells = new ArrayList<>();
        equippedSpells = new ArrayList<>();

        switch (chosenClass) {
            case MAGE:
                resource = new Resource(Resource.Type.MANA);
                break;
            case ROGUE:
                resource = new Resource(Resource.Type.ENERGY);
                break;
            case WARRIOR:
                resource = new Resource(Resource.Type.RAGE);
                break;
            default:
                break;
        }

    }
    public Player(Class chosenClass, String name, int health, Image model) {
        super(chosenClass, name, health, health, null, model);
    
        backPack = new ArrayList<>();
        learnedSpells = new ArrayList<>();
        equippedSpells = new ArrayList<>();
        
        switch (chosenClass) {
            case MAGE:
                resource = new Resource(Resource.Type.MANA);
                break;
            case ROGUE:
                resource = new Resource(Resource.Type.ENERGY);
                break;
            case WARRIOR:
                resource = new Resource(Resource.Type.RAGE);
                break;
            default:
                break;
        }
    }
    public void equipWeapon(Weapon weapon) {
        switch (getEntityClass()) {
            case MAGE:
                if (weapon.getType() == Weapon.Type.DAGGER || weapon.getType() == Weapon.Type.SWORD) {
                    System.out.println("Weapon was wrong type");
                    return;
                }
            case ROGUE:
                if (weapon.getType() == Weapon.Type.STAFF || weapon.getType() == Weapon.Type.SWORD) {
                    System.out.println("Weapon was wrong type");
                    return;
                }
            case WARRIOR:
                if (weapon.getType() == Weapon.Type.STAFF || weapon.getType() == Weapon.Type.DAGGER) {
                    System.out.println("Weapon was wrong type");
                    return;
                }
        }
        backPack.add(equippedWeapon);
        equippedWeapon = weapon;
    }
    
    public void equipArmor(Armor item) {
        switch (getEntityClass()) {
            case MAGE:
                if (item.getType() == Armor.Type.LEATHER || item.getType() == Armor.Type.PLATE) {
                    System.out.println("Item is wrong armor type");
                    return;
                }
            case ROGUE:
                if (item.getType() == Armor.Type.CLOTH || item.getType() == Armor.Type.PLATE) {
                    System.out.println("Item is wrong armor type");
                    return;
                }
            case WARRIOR:
                if (item.getType() == Armor.Type.LEATHER || item.getType() == Armor.Type.CLOTH) {
                    System.out.println("Item is wrong armor type");
                    return;
                }
        }
        if (item instanceof Chest) {
            backPack.add(equippedChest);
            equippedChest = (Chest) item;
        } else if (item instanceof Helm) {
            backPack.add(equippedHelm);
            equippedHelm = (Helm) item;
        } else if (item instanceof Legs) {
            backPack.add(equippedLegs);
            equippedLegs = (Legs) item;
        } else if (item instanceof Boots) {
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
}
