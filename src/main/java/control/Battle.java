package control;

import entities.Enemy;
import entities.Player;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;

@Getter
public class Battle {
    
    private final WalkingStage initScene;
    private Enemy enemy;
    private Player player;
    private final int ENEMY_X_DRAW_OFFSET = 30;
    // for balancing
    
    private double enemyHealthMulti;
    private double enemyDamageMulti;



    public Battle(WalkingStage initScene, Enemy enemy) {
        System.out.println("Battle started");
        this.initScene = initScene;
        this.enemy = enemy;
        player = initScene.getPlayer();
        init();
        draw();
    }
    private void init() {
        enemy.setYLoc(player.getYLoc());
        enemy.setXLoc(player.getXLoc() + ENEMY_X_DRAW_OFFSET);

    }
    private void draw() {
        initScene.getGc().drawImage(
                enemy.getModel(),
                enemy.getXLoc(),
                enemy.getYLoc());
    }
    public void playerTurn() {
        System.out.println("Player taking turn :)");
    }
    public void enemyTurn() {
        System.out.println("Enemy taking turn :(");
    }
    

}
