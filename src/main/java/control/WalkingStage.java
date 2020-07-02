package control;


import entities.Entity;
import entities.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import ui.GameScene;
import ui.HUD;
import util.FileUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class WalkingStage {
    
    private final GameScene scene;
    private final VBox mainPane;
    private final Player player;
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final List<Integer> enemyLocations;
    private HUD hud;
    
    public WalkingStage(Player player, int numScene, String xmlPath) {
        mainPane = new VBox(0);
        this.player = player;
        player.setInBattle(false);
        canvas = new Canvas(1440, 660);
        gc = canvas.getGraphicsContext2D();
        
        scene = FileUtil.getSceneFromXML(mainPane, numScene, xmlPath);
        assert scene != null;
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        enemyLocations = new ArrayList<>();
        initEnemyLocations();
        
        
    }
    public WalkingStage(WalkingStage otherStage) {
        mainPane = otherStage.mainPane;
        this.player = otherStage.player;
        player.setInBattle(false);
        canvas = otherStage.canvas;
        gc = otherStage.gc;
    
        scene = otherStage.scene;
        
        assert scene != null;
        enemyLocations = new ArrayList<>();
        
        hud = otherStage.hud;
        initEnemyLocations();
        run();
    }
    private void initEnemyLocations() {
        Random rand = new Random();
        for (int x = 0; x <= player.getLevel(); x++) {
            enemyLocations.add(rand.nextInt(Controller.WIDTH - 100) + 50);
        }
        Collections.sort(enemyLocations);
        System.out.println(enemyLocations.get(0));
    }
    public void run() {
        drawScene();
        initListeners();
    }
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
    private void initListeners() {
        mainPane.setOnKeyPressed(keyListener -> {
            if (keyListener.getCode() == KeyCode.RIGHT || keyListener.getCode() == KeyCode.D) {
                if (!player.isInBattle())
                    player.moveForward();
            }
        });
    }
    public void updateDraw() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(
                player.getModel(),
                player.getXLoc(),
                player.getYLoc()
        );
        hud.updateHUD();
    }
    private void drawScene() {
    
        Pane content = new Pane();
        content.setId(scene.getName().toLowerCase());
       
        player.setXLoc(-Entity.getXDrawOffset());
        player.setYLoc(canvas.getHeight() - player.getModel().getHeight());
        
        gc.drawImage(
                player.getModel(),
                player.getXLoc(),
                player.getYLoc()
        );
        content.getChildren().add(canvas);
        
        hud = new HUD(player);
        mainPane.getChildren().addAll(content, hud);
        scene.setRoot(mainPane);
    }
    
}
