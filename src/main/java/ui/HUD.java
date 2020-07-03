package ui;

import control.Controller;
import entities.Enemy;
import entities.Player;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import spells.Spell;

@Getter
public class HUD extends Pane {
    
    public final static double HEALTH_Y = 7.5;
    public final static double SPELL_Y = 25;

    public final static int HEALTH_X = 30;
    public final static int MANA_X = 1320;
    public final static int SPELL_ONE_X = 525;
    public final static int SPELL_TWO_X = 715;
    public final static int SPELL_THREE_X = 906;
    public final static int AUTO_ATTACK_X = 385;
    
    public final static int SPELL_ICON_SIZE = 100;
    
    public final static int HEALTH_BAR_WIDTH = 90;
    public final static int HEALTH_BAR_HEIGHT = 135;
    
    public final static int ENEMY_HEALTH_BAR_HEIGHT = 30;
    public final static int ENEMY_HEALTH_BAR_MAX_WIDTH = 800;
    
    private final static int CANVAS_WIDTH = 1440;
    
    
    private final static int HUD_HEIGHT = 150;
    private final Player player;
    private Rectangle health, mana;
    private Button aaButton, spellOneButton, spellTwoButton, spellThreeButton;
    private Label healthAmtLabel, manaAmtLabel, healthLabel, manaLabel;
    private Label enemyNameLabel, enemyHealthLabel;
    private Rectangle enemyHealthBar, enemyHealthBackdrop, enemyTextBackdrop;
    private Enemy enemy;
    
    public HUD(Player player) {
        super();
        this.player = player;
        setWidth(Controller.WIDTH);
        setMaxHeight(HUD_HEIGHT);
        getStylesheets().add(getClass().getResource("/hud_styles.css").toExternalForm());
        setId("hud_style");
        drawBars();
        initLabels();
        getChildren().addAll(health, mana, healthAmtLabel, manaAmtLabel, healthLabel, manaLabel);
        initButtons();
        
    }
    
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
    public void endBattle() {
       getChildren().removeAll(enemyHealthBar, enemyHealthBackdrop, enemyNameLabel, enemyHealthLabel, enemyTextBackdrop);
    }
    public void updateHUD() {
        updateButtons();
        updateHealth();
        updateMana();
        if (player.isInBattle())
            updateEnemyHealth();
    }
    private void updateEnemyHealth() {
        double scale = 1.0 * enemy.getCurHealth() / enemy.getBaseHealth();
        enemyHealthBar.setWidth(scale * ENEMY_HEALTH_BAR_MAX_WIDTH);
        enemyHealthLabel.setText(enemy.getCurHealth() + "/" + enemy.getBaseHealth());
    }
    private void updateHealth() {
        healthAmtLabel.setText(player.getCurHealth() + "/" + player.getMaxHealth());
        double scale = (double) player.getCurHealth() / player.getMaxHealth();
        health.setHeight(scale * HEALTH_BAR_HEIGHT);
        health.setLayoutY((1.0f - scale) * HEALTH_BAR_HEIGHT);
    }
    private void updateMana() {
        manaAmtLabel.setText(player.getCurrentResource() + "/" + player.getMaxResource());
        double scale = (double) player.getCurrentResource() / player.getMaxResource();
        mana.setHeight(scale * HEALTH_BAR_HEIGHT);
        mana.setLayoutY((1.0f - scale) * HEALTH_BAR_HEIGHT);
    }
    private void initButtons() {
        aaButton = new Button();
        spellOneButton = new Button();
        spellTwoButton = new Button();
        spellThreeButton = new Button();
        switch (player.getEntityClass()) {
            case WARRIOR: aaButton.setId("warrior_auto");
                break;
            case ROGUE: aaButton.setId("rogue_auto");
                break;
            case MAGE: aaButton.setId("mage_auto");
                break;
        }
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
    public void updateButtons() {
        getChildren().removeAll(aaButton, spellOneButton, spellThreeButton, spellTwoButton);        //updated
        if (player.getEquippedSpells()[1] != null && !getChildren().contains(spellOneButton)) {
            getChildren().add(spellOneButton);
        }
        if (player.getEquippedSpells()[2] != null && !getChildren().contains(spellTwoButton)) {
            getChildren().add(spellTwoButton);
        }
        if (player.getEquippedSpells()[3] != null && !getChildren().contains(spellThreeButton)) {
            getChildren().add(spellThreeButton);
        }
        getChildren().add(aaButton);                                //updated
    }
    public void changeSpellButtonId(int spellNum, String ID) {
        switch (spellNum) {
            case 1: spellOneButton.setId(ID);
            break;
            case 2: spellTwoButton.setId(ID);
            break;
            case 3: spellThreeButton.setId(ID);
            break;
        }
    }
    
    
}
