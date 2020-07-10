package ui;

import animation.SpriteAnimation;
import control.Controller;
import entities.Player;
import items.Armor;
import items.Item;
import items.Weapon;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Backpack {
    
    private final List<Item> contents;
    private final Player player;
    
    private final int maxSize = 16;
    private final GridPane itemPane;
    private final Pane backpackPane;
    private final Pane characterWindow;
    private final VBox characterWindowItems;
    private Tooltip currentTooltip;
    @Setter private Group displayGroup;
    
    
    public Backpack(Player player) {
        contents = new ArrayList<>();
        this.player = player;
        
        characterWindow = new Pane();
        characterWindow.setLayoutX(10);
        characterWindow.setLayoutY(10);
        characterWindow.setMinHeight(470);
        characterWindow.setMinWidth(250);
        
        Rectangle rect = new Rectangle(250, 470);                       //background for character panel
        rect.setFill(Color.rgb(0, 0, 0, .5));
        characterWindow.getChildren().add(rect);
        
        characterWindowItems = new VBox(10);
        characterWindowItems.setLayoutY(10);
        characterWindowItems.setLayoutX(10);
        
        characterWindow.getChildren().add(characterWindowItems);
        SpriteAnimation characterModel = new SpriteAnimation(player.getIdleAnimation());
        ImageView imageView = characterModel.getImageView();
        imageView.setScaleX(2.5);
        imageView.setScaleY(2.5);
        imageView.setLayoutY(rect.getHeight() / 2 - (characterModel.getHeight() * 2.5 / 2.0));
        imageView.setLayoutX(150);
        characterWindow.getChildren().add(imageView);
       // characterModel.play();

        
        itemPane = new GridPane();                              //grid pane to add item buttons to
        itemPane.setHgap(10);
        itemPane.setVgap(10);


        backpackPane = new Pane();                                  //main pane for backpack to hold background rectangle and button grid
        backpackPane.setPadding(new Insets(20, 20, 20, 20));
        
        backpackPane.setId("background");
        backpackPane.setLayoutX(Controller.WIDTH - 480);
        backpackPane.setLayoutY(10);

        Rectangle background = new Rectangle(470, 470);                             //background for backpack
        background.setFill(Color.rgb(0, 0, 0, .5));
        background.setLayoutY(0);
        background.setLayoutX(0);
        itemPane.setLayoutX(20);
        itemPane.setLayoutY(20);
        backpackPane.getChildren().add(background);
        backpackPane.getChildren().add(itemPane);
        backpackPane.getStylesheets().addAll(getClass().getResource("/backpack.css").toExternalForm());
        characterWindow.getStylesheets().addAll(getClass().getResource("/backpack.css").toExternalForm());
        
    }
    public boolean add(Item i) {
        if (contents.size() < maxSize)
            return contents.add(i);
        return false;
    }
    public boolean remove(Item i) {
        return contents.remove(i);
    }
    
    public void show() {
        displayGroup.getChildren().add(characterWindow);
        displayGroup.getChildren().add(backpackPane);
    }
    public void hide() {
        displayGroup.getChildren().remove(characterWindow);
        displayGroup.getChildren().remove(backpackPane);
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
                    int finalCount = count;
                    button.setOnAction(e -> {
                        displayTooltip(contents.get(finalCount));
                    });
                    GridPane.setConstraints(button, y, x);
                    itemPane.getChildren().add(button);
                    count++;
                }
            }
        }
        
    }
    public void updateCharacterWindow() {
        characterWindowItems.getChildren().removeAll(characterWindowItems.getChildren());
        for (Item i : player.getEquippedItems()) {
            Button l = new Button();
            l.setId(i.getIconId());
            l.setPrefHeight(75);
            l.setPrefWidth(75);
            System.out.println(l.getId());
            characterWindowItems.getChildren().add(l);
        }
    }
    private void displayTooltip(Item item) {
        if (currentTooltip != null)
            displayGroup.getChildren().remove(currentTooltip.getTooltip());
        currentTooltip = new Tooltip(player, item);
        displayGroup.getChildren().add(currentTooltip.getTooltip());
    }
    private void displayToolTipButtons() {
        //todo add buttons for equipping dropping item
    }
    static class Tooltip {

        @Getter private final VBox tooltip;
        
        public Tooltip(Player player, Item item) {
            tooltip = new VBox(10);
            //todo insert background image for tooltip here
            tooltip.setLayoutY(10);
            tooltip.setLayoutX(Controller.WIDTH / 2.0 - 100);
            tooltip.getStylesheets().addAll(getClass().getResource("/backpack.css").toExternalForm());
            
            Label itemName = new Label(item.getName());
            //todo make item name larger
            int agi = item.getAgility();
            int inte = item.getIntellect();
            int stam = item.getStamina();
            int str = item.getStrength();
            Label stamLabel = new Label(stam + "");
            Label mainStatLabel = new Label();
            Label typeSpecifcStat = new Label();
            if (item instanceof Weapon) {
                int damHigh = (int)((Weapon) item).getDamageHigh();
                int damLow = (int) ((Weapon) item).getDamageLow();
                typeSpecifcStat.setText("Damage: " + damLow + " - " + damHigh);
            }
            else if (item instanceof Armor) {
                typeSpecifcStat.setText("Armor: " + ((int) ((Armor) item).getArmor()));
            }
            switch (player.getEntityClass()) {
                case WARRIOR: mainStatLabel.setText("Strength: " + str);
                    break;
                case MAGE: mainStatLabel.setText("Intellect: " + inte);
                    break;
                case ROGUE: mainStatLabel.setText("Agility: " + agi);
                    break;
            }
            tooltip.getChildren().addAll(itemName, typeSpecifcStat, mainStatLabel, stamLabel);
        }
    }
    
}
