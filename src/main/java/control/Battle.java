package control;

import entities.Enemy;
import entities.Player;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import lombok.Getter;
import ui.HUD;

@Getter
public class Battle {
    
    private final WalkingStage initScene;
    private final Timeline gameLoop;
    private Enemy enemy;
    private Player player;
    private final int ENEMY_X_DRAW_OFFSET = 50;
    private HUD hud;
    // for balancing
    
    private double enemyHealthMulti;
    private double enemyDamageMulti;



    public Battle(WalkingStage initScene, Enemy enemy, Timeline loop) {
        System.out.println("Battle started");
        gameLoop = loop;
        this.initScene = initScene;
        this.enemy = enemy;
        player = initScene.getPlayer();
        init();
        draw();
    }
    private void init() {
        gameLoop.pause();
        enemy.setYLoc(player.getYLoc() );
        enemy.setXLoc(player.getXLoc() + ENEMY_X_DRAW_OFFSET);
        hud = initScene.getHud();
        hud.initEnemy(enemy);
    }
    private void draw() {

        System.out.println("Drawing enemy: " + enemy.getName());
        
        initScene.getGc().drawImage(
                enemy.getModel(),
                enemy.getXLoc(),
                enemy.getYLoc());
        
        hud.updateHUD();
        
    }
    public void run() {
    
    }
    public void playerTurn() {
        System.out.println("Player taking turn :)");
    }
    public void enemyTurn() {
        System.out.println("Enemy taking turn :(");
        enemy.reduceHealth(1);
    }
    

}
