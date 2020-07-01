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
import util.ArmorFileReader;
import util.FileUtil;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

public class Game {

    private final Player player;
    private final Map<String, Item> items;
    private final Stage primaryStage;
    private final List<Integer> sceneRotation;
    private int sceneNumber = 0;
    private final String battleXMLPath = "src/main/resources/battle_stages.xml";
    private final String enemyXMLPath = "src/main/resources/images/enemies/enemies.xml";
    private final List<Enemy> enemyList;
    private final int ENEMY_BASE_HEALTH = 100; //adjust here for enemy base health
    
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
        enemyList = FileUtil.getEnemiesOfType("", enemyXMLPath, ENEMY_BASE_HEALTH);
        equipStartingGear();
        
        sceneRotation = new ArrayList<>();
        
        for (int x = 0; x < FileUtil.getNumBattleScenes(battleXMLPath); x++)
            sceneRotation.add(x);
        
        Collections.shuffle(sceneRotation);
        run();
        
    }
    public void run() {

        WalkingStage wStage = new WalkingStage(player, sceneRotation.get(sceneNumber), battleXMLPath);
        primaryStage.setScene(wStage.getScene());
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);
    
        //final long timeStart = System.currentTimeMillis();
    
        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.0333),
                event -> {
                    if (!wStage.isInBattle())
                        wStage.updateDraw();
                    if (wStage.checkForEnemy()) {
                        
                        wStage.setInBattle(true);
                        List<Enemy> enemies = enemyList.stream()
                                .filter(x -> x.getLevel() == player.getLevel())
                                .collect(Collectors.toList());
                        Random r = new Random();
                        Battle b = new Battle(wStage, enemies.get(r.nextInt(enemies.size())));

                        while (player.getCurHealth() > 0 && b.getEnemy().getCurHealth() > 0) {
                            b.playerTurn(); //todo
                            b.enemyTurn();
                        }
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



}
