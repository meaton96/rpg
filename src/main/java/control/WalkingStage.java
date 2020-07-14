package control;


import entities.Enemy;
import entities.Entity;
import entities.Player;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import ui.GameScene;
import ui.HUD;
import util.ArmorFileReader;
import util.FileUtil;

import java.awt.*;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * class creating and running the part of the game where you walk across the scene and encounter random enemies
 */
@Getter
@Setter
public class WalkingStage {
    
    private final GameScene scene;
    private final Group mainPane;
    private final Player player;
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final List<Integer> enemyLocations;
    private HUD hud;
    private List<Enemy> enemyList;
    private int numScene;
    private String xmlPath;
    private Stage primaryStage;
    private static int numStages = 0;
    private final static int MAX_SCENE_NUMBER = 10;
    private boolean backPackShown = false;
    
    /**
     * stage constructor
     * @param primaryStage application stage from javafx
     * @param player instance of the player of the game
     * @param numScene scene number for choosing the background image
     * @param xmlPath path to xml document containing the background images and styles
     * @param enemyList list of all the enemies in the game
     */
    public WalkingStage(Stage primaryStage, Player player, int numScene, String xmlPath, List<Enemy> enemyList) {
        this.primaryStage = primaryStage;
        this.numScene = numScene;
        if (numScene >= FileUtil.getNumBattleScenes(xmlPath))
            numScene = 0;
        this.xmlPath = xmlPath;
        this.enemyList = enemyList;
        mainPane = new Group();                                                             //create a new vbox to add the canvas (game scene) and hud to
        this.player = player;
        player.setInBattle(false);
        canvas = new Canvas(1440, 659);
        gc = canvas.getGraphicsContext2D();                                                 //init graphics objects for drawing images
        player.heal();
        scene = FileUtil.getSceneFromXML(mainPane, numScene, xmlPath);                      //get the scene from the xml file
        assert scene != null;                                                               //removes warnings but this wont be null unless the xml path is incorrect
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());  //set styles
        enemyLocations = new ArrayList<>();
        initEnemyLocations();
        primaryStage.setScene(getScene());
        player.getBackPack().setDisplayGroup(mainPane);

        player.getBackPack().updateCharacterWindow();
    }
    
    /**
     * create an arraylist of integers for where the enemies will be encountered on this stage
     */
    private void initEnemyLocations() {
        Random rand = new Random();
        for (int x = 0; x <= player.getLevel(); x++) {
            enemyLocations.add(rand.nextInt(Controller.WIDTH - 150) + 50);
        }
        Collections.sort(enemyLocations);
    }
    
    /**
     * run the scene draw it and initialize the listeners
     */
    public void run() {
        drawScene();
        initListeners();
    }
    
    /**
     * checks player location against +/- 6 pixels to find when the player gets an encounter with the enemies
     * @return true if the player encounters an enemy false otherwise
     */
    public boolean checkForEnemy() {
        if (enemyLocations.size() == 0) {
            return false;
        }
        List<Double> xLocCheckList = new ArrayList<>();
        for (int x = -6; x <=6; x++) {
            xLocCheckList.add((double)(enemyLocations.get(0) + x));
        }
        if (xLocCheckList.contains(player.getXLoc())) {
            enemyLocations.remove(enemyLocations.get(0));
            return true;
        }
        return false;
    }
    
    /**
     * create listeners for player movement also checks for the player encountering enemies or edge of the screen
     */
    private void initListeners() {
        mainPane.setOnKeyPressed(keyListener -> {
            if (keyListener.getCode() == KeyCode.D && !backPackShown) {
                if (!player.isInBattle()) {                         //player is allowed to move foward if they arent in a battle
                    player.startWalking();
                    player.moveForward();                           //move the player and update the draw
                    updateDraw();
                    if (checkForEnemy()) {
                        startBattle();                              //player encountered an enemy so start the battle
                    }
                    if (endStage()) {
                        if (numStages % 2 == 0 && numStages != 0)                     //level player up every other stage
                            player.levelUp();
                        numScene++;                                 //player got to the end of the scene so make a new scene
                        numStages++;
                        new WalkingStage(primaryStage, player, numScene, xmlPath, enemyList).run();
                    }
                }
            }
            
        });
        mainPane.setOnKeyReleased(keyListener -> {
            if (keyListener.getCode() == KeyCode.D) {
                if (!player.isInBattle()) {
                    player.pauseWalking();
                }
            }
            if (keyListener.getCode() == KeyCode.B) {               //show or hide the backpack depending if its already
                                                                    //on the screen or not
                if (!player.isInBattle()) {
                    if (!backPackShown) {
                        backPackShown = true;
                        player.getBackPack().show();
                    }
                    else {
                        backPackShown = false;
                        player.getBackPack().hide();
                        updateDraw();
                    }
                }
            }
        });
    }
    
    /**
     * check if the player gets to the end of the stage
     * @return true if the player has moved off the screen false otherwise
     */
    private boolean endStage() {
        return player.getXLoc() > canvas.getWidth() - 21;
    }
    
    /**
     * start a battle, finds an enemy of the same level as the player and starts a battle between the player and the enemy
     */
    private void startBattle() {
        List<Enemy> enemies = enemyList.stream()
                .filter(x -> x.getLevel() == player.getLevel())
                .collect(Collectors.toList());
        Random r = new Random();
        Enemy enemy = enemies.get(r.nextInt(enemies.size()));
        enemyList.remove(enemy);
        new Battle(this, enemy);
    }
    
    /**
     * update the screen, clears canvas and updates the hud
     */
    public void updateDraw() {
        clearDraw();
        hud.updateHUD();
    }
    public void clearDraw() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    
    /**
     * draw the initial scene
     */
    private void drawScene() {
    
        Pane content = new Pane();
        content.setId(scene.getName().toLowerCase());
       
        player.setXLoc(Entity.getXDrawOffset());
        player.setYLoc(scene.getEntityDrawY() - Entity.getYDrawOffset());
        
        content.getChildren().add(canvas);
        
        hud = new HUD(player);
        hud.setLayoutX(0);
        hud.setLayoutY(canvas.getHeight() + 1);
        mainPane.getChildren().addAll(content, hud);
        player.initWalkingAnim(mainPane);
        scene.setRoot(mainPane);
    }

    
}
