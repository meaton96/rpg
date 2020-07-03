package ui;

import control.Controller;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class GameScene extends Scene {
    
    private final String name;
    private final int entityDrawY;
    private final String filePath;
    
    @Builder
    public GameScene(Pane pane, String name, int entityDrawY, String filePath) {
        super(pane, Controller.WIDTH, Controller.HEIGHT);
        this.name = name;
        this.filePath = filePath;
        this.entityDrawY = entityDrawY;
    }
}
