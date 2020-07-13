package ui;

import control.Controller;
import entities.Enemy;
import entities.Entity;
import entities.Player;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Worker;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import lombok.Getter;
import spells.Spell;

/**
 * Class for displaying the heads up display showing information on
 * player health, resources, and spells as well as
 * enemy health bar
 */
@Getter
public class HUD extends Pane {
    
    private final static double HEALTH_Y = 7.5;
    private final static double SPELL_Y = 25;
    
    private final static int HEALTH_X = 30;
    private final static int MANA_X = 1320;
    private final static int SPELL_ONE_X = 525;
    private final static int SPELL_TWO_X = 715;
    private final static int SPELL_THREE_X = 906;
    private final static int AUTO_ATTACK_X = 385;
    
    private final static int SPELL_ICON_SIZE = 100;
    
    private final static int HEALTH_BAR_WIDTH = 90;
    private final static int HEALTH_BAR_HEIGHT = 135;
    
    private final static int ENEMY_HEALTH_BAR_HEIGHT = 30;
    private final static int ENEMY_HEALTH_BAR_MAX_WIDTH = 800;
    
    private final static int CANVAS_WIDTH = 1440;
    
    
    private final static int HUD_HEIGHT = 151;
    private final Player player;
    private Rectangle health, mana;
    private Button aaButton, spellOneButton, spellTwoButton, spellThreeButton;
    private Label healthAmtLabel, manaAmtLabel, healthLabel, manaLabel;
    private Label enemyNameLabel, enemyHealthLabel;
    private Rectangle enemyHealthBar, enemyHealthBackdrop, enemyTextBackdrop;
    private Enemy enemy;
    
    /**
     * Hud constructor
     * @param player the player of the game
     */
    public HUD(Player player) {
        super();
        this.player = player;
        setWidth(Controller.WIDTH);
        setMaxHeight(HUD_HEIGHT);
        getStylesheets().add(getClass().getResource("/hud_styles.css").toExternalForm());
        getStylesheets().add(getClass().getResource("/backpack.css").toExternalForm());
        setId("hud_style");
        drawBars();
        initLabels();
        getChildren().addAll(health, mana, healthAmtLabel, manaAmtLabel, healthLabel, manaLabel);
        initButtons();
        
    }
    
    /**
     * initialize the text labels on the hud
     */
    private void initLabels() {
        healthAmtLabel = new Label(player.getMaxHealth() + "/" + player.getMaxHealth());
        healthAmtLabel.setId("hud_label");
        healthAmtLabel.setLayoutX(HEALTH_X + 10);
        healthAmtLabel.setLayoutY(67);
        
        manaAmtLabel = new Label(player.getCurrentResource() + "/" + player.getMaxResource());
        manaAmtLabel.setId("hud_label");
        manaAmtLabel.setLayoutY(67);
        manaAmtLabel.setLayoutX(MANA_X);
        
        healthLabel = new Label("HP");
        switch (player.getEntityClass()) {
            case MAGE:
                manaLabel = new Label("Mana");
                manaAmtLabel.setTextFill(Color.WHITE);
            break;
            case ROGUE:
                manaLabel = new Label("Energy");
                manaAmtLabel.setTextFill(Color.BLACK);
            break;
            case WARRIOR:
                manaLabel = new Label("Rage");
                manaAmtLabel.setTextFill(Color.WHITE);
            break;
        }
        healthLabel.setLayoutX(HEALTH_X + health.getWidth());
        healthLabel.setLayoutY(67);
        
        manaLabel.setLayoutX(MANA_X - 60);
        manaLabel.setLayoutY(67);
        
        healthLabel.setId("hud_label");
        manaLabel.setId("hud_label");
        healthAmtLabel.setTextFill(Color.WHITE);
        
        healthLabel.setTextFill(Color.WHITE);
        manaLabel.setTextFill(Color.WHITE);
        
    }
    
    /**
     * setup the information about the enemy during a battle
     * @param enemy the enemy object
     */
    public void initEnemy(Enemy enemy) {
        this.enemy = enemy;
        enemyNameLabel = new Label("Level " + enemy.getLevel() + " " + enemy.getName());
        enemyHealthLabel = new Label(enemy.getCurHealth() + "/" + enemy.getBaseHealth());
        enemyHealthBar = new Rectangle(ENEMY_HEALTH_BAR_MAX_WIDTH, ENEMY_HEALTH_BAR_HEIGHT);
        enemyHealthBar.setLayoutX(CANVAS_WIDTH / 2.0 - ENEMY_HEALTH_BAR_MAX_WIDTH / 2.0);
        enemyHealthBar.setLayoutY(-650);
        enemyHealthBar.setFill(Color.rgb(146,28,29));
        enemyHealthBar.setArcHeight(15);
        enemyHealthBar.setArcWidth(15);
        
        enemyHealthBackdrop = new Rectangle(ENEMY_HEALTH_BAR_MAX_WIDTH, ENEMY_HEALTH_BAR_HEIGHT);
        enemyHealthBackdrop.setLayoutX(CANVAS_WIDTH / 2.0 - ENEMY_HEALTH_BAR_MAX_WIDTH / 2.0);
        enemyHealthBackdrop.setLayoutY(-650);
        enemyHealthBackdrop.setArcHeight(15);
        enemyHealthBackdrop.setArcWidth(15);
        enemyHealthBackdrop.setFill(Color.BLACK);
        enemyHealthBackdrop.setOpacity(0.4);
        
        enemyTextBackdrop = new Rectangle(ENEMY_HEALTH_BAR_MAX_WIDTH, 25);
        enemyTextBackdrop.setLayoutX(CANVAS_WIDTH / 2.0 - ENEMY_HEALTH_BAR_MAX_WIDTH / 2.0);
        enemyTextBackdrop.setLayoutY(-617);
        enemyTextBackdrop.setArcHeight(15);
        enemyTextBackdrop.setArcWidth(15);
        enemyTextBackdrop.setFill(Color.BLACK);
        enemyTextBackdrop.setOpacity(0.4);
        
        enemyNameLabel.setLayoutX(enemyHealthBar.getLayoutX() + 10);
        enemyNameLabel.setLayoutY(enemyHealthBar.getLayoutY() + ENEMY_HEALTH_BAR_HEIGHT + 5);
        enemyNameLabel.setId("hud_label");
        enemyNameLabel.setTextFill(Color.WHITE);
        
        enemyHealthLabel.setLayoutX(enemyHealthBar.getLayoutX() + ENEMY_HEALTH_BAR_MAX_WIDTH - 70); //adjust health bar X here
        enemyHealthLabel.setLayoutY(enemyHealthBar.getLayoutY() + ENEMY_HEALTH_BAR_HEIGHT + 5);
        enemyHealthLabel.setId("hud_label");
        enemyHealthLabel.setTextFill(Color.WHITE);
        
        getChildren().addAll(enemyTextBackdrop,enemyHealthBackdrop, enemyHealthBar, enemyHealthLabel, enemyNameLabel);
        
    }
    
    /**
     * remove all the enemy information labels/bars from the hud
     */
    public void endBattle() {
       getChildren().removeAll(enemyHealthBar, enemyHealthBackdrop, enemyNameLabel, enemyHealthLabel, enemyTextBackdrop);
    }
    
    /**
     * update the hud, updates the text and bars for the player and the enemy
     */
    public void updateHUD() {
        updateButtons();
        updateHealth();
        updateMana();
        if (player.isInBattle())
            updateEnemyHealth();
    }
    
    /**
     * scales enemy health bar to be a % of the max size based on health
     * also updates the text
     */
    private void updateEnemyHealth() {
        double scale = 1.0 * enemy.getCurHealth() / enemy.getBaseHealth();
        enemyHealthBar.setWidth(scale * ENEMY_HEALTH_BAR_MAX_WIDTH);
        enemyHealthLabel.setText(enemy.getCurHealth() + "/" + enemy.getBaseHealth());
    }
    
    /**
     * scales the player health bar to a % of max size based on the health of the player
     * also updates the text
     */
    private void updateHealth() {
        healthAmtLabel.setText(player.getCurHealth() + "/" + player.getMaxHealth());
        double scale = (double) player.getCurHealth() / player.getMaxHealth();
        health.setHeight(scale * HEALTH_BAR_HEIGHT);
        health.setLayoutY((1.0f - scale) * HEALTH_BAR_HEIGHT);
    }
    
    /**
     * update the player resource bar to a % of max size based on the resource of the player
     */
    private void updateMana() {
        manaAmtLabel.setText(player.getCurrentResource() + "/" + player.getMaxResource());
        double scale = (double) player.getCurrentResource() / player.getMaxResource();
        mana.setHeight(scale * HEALTH_BAR_HEIGHT);
        mana.setLayoutY((1.0f - scale) * HEALTH_BAR_HEIGHT);
    }
    
    /**
     * initialize the spell buttons the player can use to interact with
     */
    private void initButtons() {
        aaButton = new Button();
        spellOneButton = new Button();
        spellTwoButton = new Button();
        spellThreeButton = new Button();
        aaButton.setId(player.getEquippedWeapon().getIconId());
        aaButton.setPrefHeight(100);
        aaButton.setPrefWidth(100);

        aaButton.setLayoutX(AUTO_ATTACK_X);
        aaButton.setLayoutY(SPELL_Y);
        
        spellOneButton.setLayoutY(SPELL_Y);
        spellTwoButton.setLayoutY(SPELL_Y);
        spellThreeButton.setLayoutY(SPELL_Y);
        spellOneButton.setLayoutX(SPELL_ONE_X);
        spellTwoButton.setLayoutX(SPELL_TWO_X);
        spellThreeButton.setLayoutX(SPELL_THREE_X);

        
        getChildren().add(aaButton);
    }
    
    /**
     * draw player health and mana bars when the hud is created
     */
    private void drawBars() {
        health = new Rectangle();
        health.setFill(Color.rgb(146,28,29));
        health.setHeight(HEALTH_BAR_HEIGHT);
        health.setWidth(HEALTH_BAR_WIDTH);
        health.setX(HEALTH_X);
        health.setY(HEALTH_Y);
        
        mana = new Rectangle();
        mana.setWidth(HEALTH_BAR_WIDTH);
        mana.setHeight(HEALTH_BAR_HEIGHT);
        mana.setX(MANA_X);
        mana.setY(HEALTH_Y);
        
        switch (player.getResource().getType()) {
            case MANA:
                mana.setFill(Color.BLUE);
            break;
            case RAGE:
                mana.setFill(Color.rgb(146,28,29));
                mana.setHeight(HEALTH_BAR_HEIGHT);
            break;
            case ENERGY:
                mana.setFill(Color.YELLOW);
            break;
        }
    }
    
    /**
     * updates all the buttons based on what the player has for equipped spells
     */
    public void updateButtons() {
        getChildren().removeAll(aaButton, spellOneButton, spellThreeButton, spellTwoButton);
        if (player.getEquippedSpells()[1] != null && !getChildren().contains(spellOneButton)) {
            getChildren().add(spellOneButton);
        }
        if (player.getEquippedSpells()[2] != null && !getChildren().contains(spellTwoButton)) {
            getChildren().add(spellTwoButton);
        }
        if (player.getEquippedSpells()[3] != null && !getChildren().contains(spellThreeButton)) {
            getChildren().add(spellThreeButton);
        }
        getChildren().add(aaButton);
    }
    
    /**
     * show and animate a damage number display over the entities head for damage taken during a turn in battle
     * @param group the display group to draw the label on
     * @param entity the entity to set the x and y draw of the label
     * @param damage the damage number to display
     */
    public void playDamageToast(Group group, Entity entity, int damage) {
        Label display = new Label("-" + damage);
        if (entity instanceof Player) {
            if (damage == 0 && entity.getEntityClass() == Entity.Class.ROGUE)
                display.setText("Dodge");
            display.setLayoutY(((Player)entity).getYLoc() + 5);
            display.setLayoutX(((Player)entity).getXLoc() + 15);
        }
        else {
            display.setLayoutY(((Enemy)entity).getYLoc() + 5);
            display.setLayoutX(((Enemy)entity).getXLoc() + 30);
        }
        display.setTextFill(Color.RED);
        display.setId("toastLabel");
        
        FadeTransition fadeOut = new FadeTransition();
        fadeOut.setDuration(Duration.millis(1500));
        fadeOut.setToValue(0);
        fadeOut.setFromValue(1);
        fadeOut.setNode(display);

        group.getChildren().add(display);
        fadeOut.play();
        fadeOut.setOnFinished(e -> group.getChildren().remove(display));
       
    }
}
