package control;

import entities.Entity;
import entities.Player;
import items.Armor;
import items.Item;
import items.Weapon;

import java.util.Map;

public class Game {

    private final Player player;
    private final Map<String, Item> items;

    /**
     * game constructor
     *
     * @param classNumber number for the class choice
     *                    1 - warrior
     *                    2 - rogue
     *                    3 - mage
     * @param name        name chosen by the user
     */
    public Game(int classNumber, String name) {

        ArmorFileReader.init();
        items = ArmorFileReader.getItemMap();
        player = new Player(Entity.getClassFromNumber(classNumber), name, 100, null);
        equipStartingGear();

    }
    
    private void equipStartingGear() {
        switch (player.getEntityClass()) {
            case WARRIOR:
                player.equipArmor((Armor) items.get("Starting Plate Chest"));
                player.equipArmor((Armor) items.get("Starting Plate Helm"));
                player.equipArmor((Armor) items.get("Starting Plate Legs"));
                player.equipArmor((Armor) items.get("Starting Plate Boots"));
                player.equipWeapon((Weapon) items.get("Starting Sword"));
            break;
            case ROGUE:
                player.equipArmor((Armor) items.get("Starting Leather Chest"));
                player.equipArmor((Armor) items.get("Starting Leather Helm"));
                player.equipArmor((Armor) items.get("Starting Leather Legs"));
                player.equipArmor((Armor) items.get("Starting Leather Boots"));
                player.equipWeapon((Weapon) items.get("Starting Dagger"));
            break;
            case MAGE:
                player.equipArmor((Armor) items.get("Starting Cloth Chest"));
                player.equipArmor((Armor) items.get("Starting Cloth Helm"));
                player.equipArmor((Armor) items.get("Starting Cloth Legs"));
                player.equipArmor((Armor) items.get("Starting Cloth Boots"));
                player.equipWeapon((Weapon) items.get("Starting Staff"));
            break;
                
        }
    }


}
