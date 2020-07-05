package control;

import entities.Enemy;
import entities.Player;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import lombok.Getter;
import ui.HUD;

/**
 * class representing one battle between the player and a single enemy
 */
@Getter
public class Battle {
    
    private final WalkingStage initScene;
    private final Enemy enemy;
    private final Player player;
    private static final int ENEMY_X_DRAW_OFFSET = 55;
    private static final int ENEMY_Y_DRAW_OFFSET = 15;
    private HUD hud;
    // for balancing
    
    private final double enemyHealthMulti = 1.2;
    private final double enemyDamageMulti = 2;
    
    
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
        hud = initScene.getHud();                               //get the heads up display from the scene and pass it the enemy for drawing healthbars
        hud.initEnemy(enemy);

        int numSpells = 0;                                                          //initialize buttons
        for (int x = 0; x < player.getEquippedSpells().length; x++) {
            if (player.getEquippedSpells()[x] != null) {
                numSpells++;
            }
        }
        player.setTurn(true);
        switch (numSpells) {
            case 1 : setButtonListeners("");
            break;
            case 2 : setButtonListeners("", player.getEquippedSpells()[1].getName());
            break;
            case 3 : setButtonListeners("", player.getEquippedSpells()[1].getName(), player.getEquippedSpells()[2].getName());
            break;
            case 4 : setButtonListeners("", player.getEquippedSpells()[1].getName(), player.getEquippedSpells()[2].getName(), player.getEquippedSpells()[3].getName());
            break;
        }
        
        
    }
    private void cleanUpBattle() {
        player.setInBattle(false);
        hud.endBattle();
        hud.updateHUD();
        initScene.updateDraw();
        initScene.getMainPane().getChildren().removeAll(
                player.getAttackAnimation().getImageView(),
                player.getIdleAnimation().getImageView()
        );
        player.unHideWalkingAnimation();
        
    }
    

    
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
                        System.out.println("player hp: " + player.getCurHealth());
                        int playerDamageDone = player.getDamFromSpellName("Auto Attack");           //if its the players turn get the damage of the spell (basic attack)
                        player.setTurn(false);                                                      //this stuff should be moved to a method to call ie. pass in spell and then run the rest
    
                        
                        player.playAttackAnimation(initScene.getMainPane());                   //currently doesnt play at the end of a fight
                        
                        
                        
                        
                        enemy.reduceHealth(playerDamageDone);           //reduce enemy hp and update the hud
                        hud.updateHUD();
                        
                        if (enemy.isAlive())  {                          //do enemy turn if enemy didnt die from player attack
                            //enemyTurn();
                            new EnemyTurn().start();
                            
                        }
                        else {
                            cleanUpBattle();
                            //play some end battle message display the dropped gear ect
                        }
                    }
                });
                
            }
            //add animations for spells here
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
        initScene.getGc().drawImage(
                enemy.getModel(),
                enemy.getXLoc(),
                enemy.getYLoc());

        hud.updateHUD();
        
    }
    class EnemyTurn implements Runnable {
    
        
        
        @Override
        public void run() {
            int enemyDamageDone = (int) (enemy.castSpell().getDamageDone() * enemyDamageMulti);
            player.reduceHealth(enemyDamageDone);       //reduce the hp of the player by that amount and update hud
            
            if (player.isAlive()) {
                Platform.runLater(() -> hud.updateHUD());
                                //check to make sure player is alive
                player.setTurn(true);
                
            }
            else {
                //todo add end game screen
            }
        }
        public void start() {
            new Thread(this).start();
        }
    }
    /*
    /**
     * process an enemy turn
     
    public void enemyTurn() {
        System.out.println("Enemy taking turn :(");
        int enemyDamageDone = (int) (enemy.castSpell().getDamageDone() * enemyDamageMulti);     //get damage of enemy attack
        try {
            Thread.sleep(500);
            player.reduceHealth(enemyDamageDone);       //reduce the hp of the player by that amount and update hud
            hud.updateHUD();
            if (player.isAlive()) {                     //check to make sure player is alive
                player.setTurn(true);
            }
            else {
                //todo add end game screen
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

}
