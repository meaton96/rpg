package ui;

import control.Controller;
import entities.Player;
import items.Item;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Backpack {
    
    private final List<Item> contents;
    //private final List<Button> itemButtons;
    private final Player player;
    
    private final int maxSize = 16;
    private final GridPane itemPane;
    private final VBox box;
    
    
    public Backpack(Player player) {
        contents = new ArrayList<>();
       // itemButtons = new ArrayList<>();
        this.player = player;

        
        itemPane = new GridPane();
        itemPane.setHgap(10);
        itemPane.setVgap(10);


        box = new VBox();
        box.setPadding(new Insets(20, 20, 20, 20));
        box.getChildren().add(itemPane);
        box.setId("background");
        box.setLayoutX(Controller.WIDTH - 480);
        box.setLayoutY(10);

        Rectangle background = new Rectangle(470, 470);
        background.setFill(Color.BLACK);
        background.setOpacity(0.5);
        box.getChildren().add(background);
        box.getStylesheets()
                .addAll(getClass().getResource("/backpack.css").toExternalForm());
       // box.setPrefWidth(470);
      //  box.setPrefHeight(470);
    }
    public boolean add(Item i) {
        if (contents.size() < maxSize)
            return contents.add(i);
        return false;
    }
    public boolean remove(Item i) {
        return contents.remove(i);
    }
    
    public void show(Group group) {
        group.getChildren().add(box);
    }
    public void hide(Group group) {
        group.getChildren().remove(box);
    }
    public void updateBackpack() {
        int count = 0;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (contents.size() == count)
                    return;
                if (contents.get(count) != null) {
                    Button button = new Button();
                    button.setPrefHeight(100);
                    button.setPrefWidth(100);

                    button.setId(contents.get(count).getIconId());
                    GridPane.setConstraints(button, y, x);
                    //itemButtons.add(button);
                    itemPane.getChildren().add(button);
                    count++;
                }
            }
        }
    }
    public void displayTooltip(Item item) {
    
    }
    
}
