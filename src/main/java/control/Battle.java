package control;

import entities.Enemy;
import entities.Player;
import items.Item;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import lombok.Getter;
import ui.HUD;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * class representing one battle between the player and a single enemy
 */
@Getter
public class Battle {
    
    private final WalkingStage initScene;
    private final Enemy enemy;
    private final Player player;
    private static final int ENEMY_X_DRAW_OFFSET = 55;
    private static final int ENEMY_Y_DRAW_OFFSET = 15;  //gap between the player model feet and bottom of the .png image
    private HUD hud;
    // for balancing
    
    private final double enemyHealthMulti = 1.2;
    
    
    /**
     * constructor
     * @param initScene scene of the battle
     * @param enemy enemy to battle
     */
    public Battle(WalkingStage initScene, Enemy enemy) {
        this.initScene = initScene;
        this.enemy = enemy;
        player = initScene.getPlayer();
        init();
        draw();
    }
    
    /**
     * initialize many variables used in the battle
     */
    private void init() {
        player.hideWalkingAnimation();
        player.unHideBattleAnimations();
        player.setInBattle(true);                           //set the player to be in battle and set the enemy location to be in front of the player
        enemy.setYLoc(player.getYLoc() + ENEMY_Y_DRAW_OFFSET);
        enemy.setXLoc(player.getXLoc() + ENEMY_X_DRAW_OFFSET);
        
        enemy.initAnimationLocation(initScene.getMainPane());
        
        
        hud = initScene.getHud();                               //get the heads up display from the scene and pass it the enemy for drawing healthbars
        hud.initEnemy(enemy);

        int numSpells = 0;                                                          //initialize buttons
        for (int x = 0; x < player.getEquippedSpells().length; x++) {
            if (player.getEquippedSpells()[x] != null) {
                numSpells++;
            }
        }
        player.setTurn(true);
        switch (numSpells) {                    //fill up the hud with the spells that the player has equipped
            case 1 : setButtonListeners("");
            break;
            case 2 : setButtonListeners("", player.getEquippedSpells()[1].getName());
            break;
            case 3 : setButtonListeners("", player.getEquippedSpells()[1].getName(), player.getEquippedSpells()[2].getName());
            break;
            case 4 : setButtonListeners("", player.getEquippedSpells()[1].getName(), player.getEquippedSpells()[2].getName(), player.getEquippedSpells()[3].getName());
            break;
        }
        player.setAttackOnFinish(event -> {        //after player has attacked

            player.playIdleFromStart();
            if (!player.isInBattle()) {                             //if the battle ended call the cleanup method
                cleanUpBattle();
            }
            else {

                new EnemyTurn().start();                        //otherwise the enemy starts a turn
            }
        });
        enemy.getAttackAnimation().setOnFinished(event -> {     //when the enemy animation has finished

            hud.updateHUD();                                        //update hud
            if (enemy.isAlive())
                enemy.playIdleAnimation();                      //if enemy is alive go back to their idle animation otherwise play the death animation
            else
                enemy.playDeathAnimation();
            if (!player.isAlive()) {                            //if the player is killed play their death animation
                player.playDeathAnimation(initScene.getMainPane());
            }
        });


        FadeTransition fadeIn = new FadeTransition();
        fadeIn.setDuration(Duration.millis(4000));                              //setup transition animation for the death screen
        Rectangle rec = new Rectangle(Controller.WIDTH, Controller.HEIGHT);
        rec.setFill(Color.BLACK);
        rec.setOpacity(0);
        
        fadeIn.setNode(rec);
        fadeIn.setToValue(1);
        fadeIn.setFromValue(0);
        
        fadeIn.setOnFinished(e -> initScene.getPrimaryStage().setScene(getDeathScreen()));
        player.getAnimationByName("death").setOnFinished(event -> {                 //fade to black
            player.removeAllAnimations(initScene.getMainPane());
            initScene.getMainPane().getChildren().add(rec);
            fadeIn.play();

        });
        
    }

    /**
     * clean up extra elements/lingering animations from the battle and remove them from the scene
     * add back walking animation
     */
    private void cleanUpBattle() {
        
        enemy.getDeathAnimation().setOnFinished(event -> {              //when the enemy has finished the death animation
            hud.endBattle();                                            //update hud
            hud.updateHUD();
            initScene.updateDraw();
            initScene.getMainPane().getChildren().removeAll(                        //remove all animations
                    player.getImageViewByName("attack"),
                    player.getImageViewByName("idle"),
                    enemy.getAttackAnimation().getImageView(),
                    enemy.getDeathAnimation().getImageView(),
                    enemy.getIdleAnimation().getImageView()
            );
            player.unHideWalkingAnimation();                //add the walking animation back
        });
        Random r = new Random();
        List<Item> possibleDrops = enemy.getDrops()
                .stream()
                .filter(x -> x.typeMatchPlayer(player))
                .collect(Collectors.toList());

        Item i = possibleDrops.get(r.nextInt(possibleDrops.size()));
        
        player.giveItem(i);
        player.updateBackpack();
        enemy.playDeathAnimation();
    }
    
//
    
    /**
     * set listeners for ability buttons on the hud
     * @param spellNames variable size array of string arguments for spell names to tell the buttons what to do
     */
    public void setButtonListeners(String ... spellNames) {
        for (int x = 0; x < spellNames.length; x++) {
            if (x == 0) {
                hud.getAaButton().setOnAction(event -> {
                    if (player.isTurn()) {
                        hud.updateHUD();
                        int playerDamageDone = player.getDamFromSpellName("Auto Attack");           //if its the players turn get the damage of the spell (basic attack)
                        player.setTurn(false);
                        
                        player.playAttackAnimation(initScene.getMainPane());

                        enemy.reduceHealth(playerDamageDone);           //reduce enemy hp and update the hud
                        hud.playDamageToast(initScene.getMainPane(), enemy, playerDamageDone);
                        hud.updateHUD();
                        
                        if (!enemy.isAlive())                         //do enemy turn if enemy didnt die from player attack
                            player.setInBattle(false);
                    }
                });
                
            }
            //add other spells here
            if (x == 1) {
                hud.getSpellOneButton().setOnAction(event -> {
                
                });
            }
            if (x == 2) {
                hud.getSpellTwoButton().setOnAction(event -> {
                
                });
            }
            if (x == 3) {
                hud.getSpellThreeButton().setOnAction(event -> {
                
                });
            }
            
        }
    }
    
    /**
     * draw the enemy model
     */
    private void draw() {
        initScene.clearDraw();
        player.playIdleAnimation(initScene.getMainPane());
        enemy.playIdleAnimation();
        hud.updateHUD();
    }

    /**
     * Threadable class to run an enemy turn
     */
    class EnemyTurn implements Runnable {

        @Override
        public void run() {
            int enemyDamageDone = (int) Math.round(enemy.getDamageDone(player));
            try {
                Thread.sleep(1000);
            } catch (Exception e) { e.printStackTrace(); }
            Platform.runLater(() -> {
                enemy.playAttackAnimation();
                if (enemyDamageDone != 0)
                    Platform.runLater(() -> player.playHurtAnimation(initScene.getMainPane()));
            });

            Platform.runLater(() -> hud.playDamageToast(initScene.getMainPane(), player, enemyDamageDone));
            player.reduceHealth(enemyDamageDone);       //reduce the hp of the player by that amount and update hud
            
            if (player.isAlive())
                player.setTurn(true);
        }
        
        public void start() {
            new Thread(this).start();
        }
    }

    /**
     * create the scene/controls to display the screen when your character dies
     * @return the death scene
     */
    public Scene getDeathScreen() {
        VBox deathPane = new VBox(300);
        deathPane.setAlignment(Pos.CENTER);
        deathPane.setId("death_screen");

        javafx.scene.control.Button mainMenu = new javafx.scene.control.Button("Main Menu");
        HBox hBox = new HBox(100);
        hBox.setAlignment(Pos.CENTER);
        mainMenu.setId("start_button");
        mainMenu.setOnAction(event -> new Controller(initScene.getPrimaryStage()).start());
        mainMenu.setMinSize(Controller.WIDTH / 8.0, Controller.HEIGHT / 8.0);
        javafx.scene.control.Button exit = new javafx.scene.control.Button("Exit");
        exit.setMinSize(Controller.WIDTH / 8.0, Controller.HEIGHT / 8.0);
        exit.setId("start_button");
        exit.setOnAction(event -> System.exit(0));
        hBox.getChildren().addAll(mainMenu, exit);
        deathPane.getChildren().addAll(new Label(), hBox);
        Scene deathScene = new Scene(deathPane);
        deathScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        return deathScene;
    }

}
