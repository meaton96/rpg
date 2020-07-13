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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;
import util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the backpack of the player
 * also contains the character window to show equipped gear as well as tooltips for gear
 * and gives the ability to equip/drop gear from backpack
 */
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
    private Tooltip equippedTooltip;
    private final VBox toolTipButtonBox;
    private final Button equip, drop;
    @Setter private Group displayGroup;

    /**
     * constructor
     * @param player the player that the backpack belongs to
     */
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


        ImageView imageView = new ImageView(player.getModel());
        imageView.setScaleY(2.5);
        imageView.setScaleX(2.5);
        imageView.setLayoutY(rect.getHeight() / 2 - (imageView.getImage().getHeight() * 2.5 / 2.0));
        imageView.setLayoutX(150);
        characterWindow.getChildren().add(imageView);

        
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
        backpackPane.getStylesheets().addAll(getClass().getResource("/backpack.css").toExternalForm());         //setup css style sheets
        characterWindow.getStylesheets().addAll(getClass().getResource("/backpack.css").toExternalForm());

        toolTipButtonBox = new VBox(10);
        toolTipButtonBox.getStylesheets().addAll(getClass().getResource("/backpack.css").toExternalForm());

        equip = new Button("Equip");                    //setup equip and drop buttons
        drop = new Button("Drop");

        equip.setPrefWidth(240);
        drop.setPrefWidth(240);
        equip.setPrefHeight(40);
        drop.setPrefHeight(40);
        toolTipButtonBox.setLayoutX(Tooltip.LAYOUT_X_BACKPACK);
        toolTipButtonBox.setLayoutY(Tooltip.TOOLTIP_HEIGHT + 20);

        equip.setOnAction(e -> attemptEquip(currentTooltip.getItem()));
        drop.setOnAction(e -> remove(currentTooltip.getItem()));

        toolTipButtonBox.getChildren().addAll(equip, drop);
        
    }

    /**
     * attempt to add an item to the backpack, ability determined by the max backpack size
     * @param i the item to be added
     * @return boolean true if the item can be added false otherwise
     */
    public boolean add(Item i) {
        if (contents.size() < maxSize)
            return contents.add(i);
        return false;
    }

    /**
     * attempt to remove an item from the backpack
     * @param i the item to remove
     * @return true if the item was in the backpack false otherwise
     */
    public boolean remove(Item i) {
        boolean b = contents.remove(i);
        updateBackpack();
        hideTooltip();
        return b;
    }

    /**
     * add the backpack and character window to the display group main pane
     */
    public void show() {
        displayGroup.getChildren().add(characterWindow);
        displayGroup.getChildren().add(backpackPane);
    }

    /**
     * remove every ui element from the display group
     */
    public void hide() {
        displayGroup.getChildren().remove(characterWindow);
        displayGroup.getChildren().remove(backpackPane);
        hideTooltip();
        hideEquippedTooltip();
    }

    /**
     * hide the current tooltip and the equip/drop buttons
     */
    public void hideTooltip() {
        displayGroup.getChildren().remove(toolTipButtonBox);
        if (currentTooltip != null)
            displayGroup.getChildren().remove(currentTooltip.getTooltip());
    }

    /**
     * hide only the quipped tooltip
     */
    public void hideEquippedTooltip() {
        if (equippedTooltip != null)
        displayGroup.getChildren().remove(equippedTooltip.getTooltip());
    }

    /**
     * update the buttons to display on the backpack by iterating through the items and creating new buttons
     * to be more efficient this should really only update buttons that chnaged
     */
    public void updateBackpack() {
        itemPane.getChildren().clear();
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
                    button.setOnAction(e -> displayTooltip(contents.get(finalCount), false));
                    GridPane.setConstraints(button, y, x);
                    itemPane.getChildren().add(button);
                    count++;
                }
            }
        }
        
    }

    /**
     * update the character window by iterating through the player's equipped items and creating new buttons
     * once again to be more efficient this should only check for differences
     */
    public void updateCharacterWindow() {
        characterWindowItems.getChildren().removeAll(characterWindowItems.getChildren());
        for (Item i : player.getEquippedItems()) {
            Button l = new Button();
            l.setId(i.getIconId());
            l.setPrefHeight(75);
            l.setPrefWidth(75);
            l.setOnAction(e -> displayTooltip(i, true));
            characterWindowItems.getChildren().add(l);
        }
    }
    public boolean contains(Item i) { return contents.contains(i); }


    /**
     * display the item information on the screen
     * @param item the item to display the info of
     * @param isEquipped if the item is an equipped item or if its in the backpack
     */
    private void displayTooltip(Item item, boolean isEquipped) {
        if (isEquipped) {
            if (equippedTooltip != null)
                displayGroup.getChildren().remove(equippedTooltip.getTooltip());
            equippedTooltip = new Tooltip(item, true, player.getLevel());
            displayGroup.getChildren().add(equippedTooltip.getTooltip());
        }
        else {
            if (currentTooltip != null)
                displayGroup.getChildren().remove(currentTooltip.getTooltip());
            currentTooltip = new Tooltip(item, false, player.getLevel());
            displayGroup.getChildren().add(currentTooltip.getTooltip());
            displayTooltipButtons();
        }

    }

    /**
     * display the buttons to allow equipping/dropping gear from the backpack
     */
    private void displayTooltipButtons() {
        if (!displayGroup.getChildren().contains(toolTipButtonBox))
            displayGroup.getChildren().add(toolTipButtonBox);
    }

    /**
     * attempt to equip an item from the backpack
     * updates the character window and the backpack if successfull, removes the item from the backpack
     * and equips it to the player, also updates the players hp by the amount increased by the new equipped item
     * @param item item to equip
     */
    private void attemptEquip(Item item) {
        if (player.canEquip(item)) {
            int curHP = player.getCurHealth();
            int maxHpBefore = player.getMaxHealth();            //get these values for updating hp difference
            if (item instanceof Armor)
                player.equipArmor((Armor)item);
            else
                player.equipWeapon((Weapon) item);
            int newMaxHp = player.getMaxHealth();
            curHP += newMaxHp - maxHpBefore;                    //update hp
            player.setCurHealth(curHP);
            updateCharacterWindow();                            //update the backpack and character window display
            updateBackpack();
            hideEquippedTooltip();                              //remove it form the backpack and hide the tooltip
            remove(item);
        }
        System.out.println("Cannot equip: " + item.getName());
    }

    /**
     * static helper class representing a tooltip to display item information
     */
    static class Tooltip {

        @Getter private final VBox tooltip;
        @Getter private final Item item;

        public final static double LAYOUT_X_BACKPACK = Controller.WIDTH / 2.0 - 100;
        public final static double LAYOUT_X_EQUIPPED = Controller.WIDTH / 2.0 - 338;
        public final static double TOOLTIP_HEIGHT = 240;

        public Tooltip(Item item, boolean isEquipped, int playerLevel) {
            this.item = item;
            tooltip = new VBox(10);

            tooltip.setLayoutY(10);

            tooltip.setLayoutX(LAYOUT_X_BACKPACK);
            if (isEquipped)
                tooltip.setLayoutX(LAYOUT_X_EQUIPPED);

            tooltip.getStylesheets().addAll(getClass().getResource("/backpack.css").toExternalForm());
            tooltip.setId("tooltip");
            tooltip.setPadding(new Insets(5,10,10,10));
            
            Label itemName = new Label(item.getName());
            itemName.setId("tooltip_itemName");
            itemName.setWrapText(true);

            int agi = item.getAgility();                                    //get the item information to display
            int inte = item.getIntellect();
            int stam = item.getStamina();
            int str = item.getStrength();
            Label stamLabel = new Label("Stamina: " + stam);
            Label mainStatLabel = new Label();
            Label typeSpecifcStat = new Label();
            Label levelReq = new Label();
            levelReq.setText("Level: " + item.getLevel());
            if (item.getLevel() > playerLevel)
                levelReq.setTextFill(Color.RED);
            if (item instanceof Weapon) {
                int damHigh = (int)((Weapon) item).getDamageHigh();
                int damLow = (int) ((Weapon) item).getDamageLow();
                typeSpecifcStat.setText("Damage: " + damLow + " - " + damHigh);
                switch (((Weapon) item).getType()) {
                    case SWORD: mainStatLabel.setText("Strength: " + str);
                    break;
                    case STAFF: mainStatLabel.setText("Intellect: " + inte);
                    break;
                    case DAGGER: mainStatLabel.setText("Agility: " + agi);
                    break;
                }
            }
            else if (item instanceof Armor) {
                typeSpecifcStat.setText("Armor: " + ((int) ((Armor) item).getArmor()));
                switch (((Armor) item).getType()) {
                    case PLATE: mainStatLabel.setText("Strength: " + str);
                        break;
                    case CLOTH: mainStatLabel.setText("Intellect: " + inte);
                        break;
                    case LEATHER: mainStatLabel.setText("Agility: " + agi);
                        break;
                }
            }
            tooltip.getChildren().addAll(itemName, typeSpecifcStat, mainStatLabel, stamLabel, levelReq);  //add all the labels to the main tooltip vbox
            if (isEquipped)
                tooltip.getChildren().add(new Label("(equipped)"));
        }
    }
    
}
