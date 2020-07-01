package control;

import entities.Enemy;
import entities.Entity;
import entities.Player;
import items.Armor;
import items.Item;
import items.Weapon;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Game {

    private final Player player;
    private final Map<String, Item> items;
    private final Stage primaryStage;
    private final List<Integer> sceneRotation;
    private int sceneNumber = 0;
    private final String battleXMLPath = "src/main/resources/battle_stages.xml";
    private final List<Enemy> enemyList;
    
    
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
        player = new Player(Entity.getClassFromNumber(classNumber), name, 100);
        enemyList = new ArrayList<>();
        initEnemyList();
        equipStartingGear();
        
        sceneRotation = new ArrayList<>();
        
        for (int x = 0; x < FileUtil.getNumBattleScenes(battleXMLPath); x++)
            sceneRotation.add(x);
        
        Collections.shuffle(sceneRotation);
        
        
        run();
        
    }
    public void run() {
        Enemy enemy = enemyList.get(0); //change eventually
        
        WalkingStage wStage = new WalkingStage(player, sceneRotation.get(sceneNumber), battleXMLPath);
        primaryStage.setScene(wStage.getScene());
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);
    
        //final long timeStart = System.currentTimeMillis();
    
        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                event -> {
                    wStage.updateDraw();
                    if (wStage.checkForEnemy()) {
                    
                    }
                }
        );
        gameLoop.getKeyFrames().add(kf);
        gameLoop.play();
        
        if (sceneNumber == sceneRotation.size()) {
            sceneNumber = 0;
        }
        else
            sceneNumber++;
    
    
    }
    
    private void equipStartingGear() {
        System.out.println(player.getEntityClass());
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
    private void initEnemyList() {
        
        //change to read enemy list in from file and randomize then sort by level
        //maybe split into different lists or list<list<Enemy>> to keep enemy levels seperate
        for (int x = 0; x < 1; x++) {
            enemyList.add(new Enemy(
                    Entity.Class.MAGE,
                    "Test Enemy",
                    100,
                    1,
                    FileUtil.getResourceStreamFromClass(getClass(), "/images/Mage/mage.png")
            ));
        }
    }


}
