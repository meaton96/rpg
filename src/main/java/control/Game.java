package control;

import entities.Enemy;
import entities.Entity;
import entities.Player;
import items.Armor;
import items.Item;
import items.Weapon;
import javafx.stage.Stage;
import util.ArmorFileReader;
import util.FileUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * class to start the game
 */
public class Game implements Runnable{

    private final Player player;
    private final Map<String, Item> items;
    private final Stage primaryStage;
    private final List<Integer> sceneRotation;
    private final int sceneNumber = 0;
    private final String battleXMLPath = "src/main/resources/battle_stages.xml";
    private final String enemyXMLPath = "src/main/resources/images/enemies/enemies.xml";
    private final List<Enemy> enemyList;
    private final int ENEMY_BASE_HEALTH = 20; //adjust here for enemy base health

    
    
    /**
     * game constructor
     *
     * @param classNumber number for the class choice
     *                    1 - warrior
     *                    2 - rogue
     *                    3 - mage
     * @param name        name chosen by the user
     */
    public Game(Stage stage, int classNumber, String name) {
        primaryStage = stage;
        ArmorFileReader.init();
        items = ArmorFileReader.getItemMap();
        player = new Player(Entity.getClassFromNumber(classNumber), name, 20);
        
        enemyList = FileUtil.getEnemiesOfType("", enemyXMLPath, ENEMY_BASE_HEALTH);

        equipStartingGear();
        sceneRotation = new ArrayList<>();
        
        for (int x = 0; x < FileUtil.getNumBattleScenes(battleXMLPath); x++)
            sceneRotation.add(x);
        
        Collections.shuffle(sceneRotation);
        
    }
    
    /**
     * run the game
     */
    @Override
    public void run() {

        new WalkingStage(primaryStage, player, sceneRotation.get(sceneNumber), battleXMLPath, enemyList).run();

    }


    
    /**
     * Equip the starting gear for the player based on the class they chose
     */
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
