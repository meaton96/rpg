package control;

import entities.Player;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jdk.dynalink.beans.StaticClass;
import lombok.RequiredArgsConstructor;

/**
 * class to start up the game run the main menu and character select
 */
@RequiredArgsConstructor
public class Controller {

    public static final int WIDTH = 1440;
    public static final int HEIGHT = 810;
    
    Font font;

    private final Stage primaryStage;
    private String styleSheet;
    
    
    /**
     * start the controller, get the menu font and init the style sheet
     */
    public void start() {
        
        font = Font.font("/immortal.ttf");
        styleSheet = getClass().getResource("/style.css").toExternalForm();
        mainMenu();
        primaryStage.show();
    }
    
    /**
     * make controls and add them to the main menu
     */
    private void mainMenu() {
        VBox mainMenuPane = new VBox(150);
        mainMenuPane.setId("main_menu");
        mainMenuPane.setAlignment(Pos.CENTER);


        Button start = new Button("Start");
        start.setId("start_button");
        start.setPrefSize(WIDTH / 8.0, HEIGHT / 8.0);
        
        
        Label rules = makeInstructionLabel();
        mainMenuPane.getChildren().addAll(new Label(), new Label(), start, rules);


        Scene menuScene = new Scene(mainMenuPane, WIDTH, HEIGHT);
        menuScene.getStylesheets().addAll(styleSheet);
        primaryStage.setScene(menuScene);

        start.setOnAction(actionEvent -> {
            characterSelect(menuScene);
        });
    }
    private Label makeInstructionLabel() {
        String instructions = "Welcome to my mini RPG game. Use the menu to begin and select a class to play with."
                + "\nAfter selecting a class enter the name for your character and hit begin."
                + "\nUse the D key to move across the screen. You will encounter enemies along the way."
                + "\nCombat is turn based. Use the buttons at the bottom of the screen to use your abilities to defeat the enemies"
                + "\nHitting B while not in combat will open up the backpack and skill tree to allow you to equip new items (drop from enemies)"
                + "\nand new spells (drop from bosses and gained when you level up) (Backpack NYI)";
        Label label = new Label(instructions);
        label.setId("instruction_label");
        label.setLayoutY(HEIGHT - 150);

        return label;
    }
    
    /**
     * run character select
     * @param scene scene of the main menu for style options
     */
    private void characterSelect(Scene scene) {
        scene.getStylesheets().add(getClass().getResource("/characterSelectStyle.css").toExternalForm());

        HBox hBox = new HBox(100);

        VBox warriorVBox = new VBox(20);
        VBox rogueVBox = new VBox(20);
        VBox mageVBox = new VBox(20);

        hBox.setId("main_menu");
        hBox.setAlignment(Pos.CENTER);

        warriorVBox.setAlignment(Pos.CENTER);
        rogueVBox.setAlignment(Pos.CENTER);
        mageVBox.setAlignment(Pos.CENTER);


        Label warriorName = new Label("Warrior");
        warriorName.setId("warrior_name_label");
        Label rogueName = new Label("Rogue");
        rogueName.setId("rogue_name_label");
        Label mageName = new Label("Mage");
        mageName.setId("mage_name_label");


        Button warriorButton = new Button();
        warriorButton.setId("warrior_button");
        warriorButton.setPrefSize(WIDTH / 8.0, HEIGHT / 8.0);
        warriorButton.setOnAction(actionEvent -> {
            customizeCharacter(scene, 1, warriorName);
        });

        Button rogueButton = new Button("");
        rogueButton.setId("rogue_button");
        rogueButton.setPrefSize(WIDTH / 8.0, HEIGHT / 8.0);
        rogueButton.setOnAction(actionEvent -> {
            customizeCharacter(scene, 2, rogueName);
        });

        Button mageButton = new Button("");
        mageButton.setId("mage_button");
        mageButton.setPrefSize(WIDTH / 8.0, HEIGHT / 8.0);
        mageButton.setOnAction(actionEvent -> {
            customizeCharacter(scene, 3, mageName);
        });

        Label warriorDesc = new Label("Warriors use swords and physical \ndamage attacks to defeat their enemies."
                                        + "\nThey use rage as a resource \nthat they generate with attacks." +
                                            "\nWarriors use plate armor and \nare the strongest class defensively but they \ndo less damage because of this");
        warriorDesc.setId("info_label");
        Label rogueDesc = new Label("Rogues use daggers and deal physical and \npoison damage" +
                                    "They spend energy to deal \nlarge damage. Energy regenerates between \nturns Rogues use leather armor and therefore" +
                                    "\ntake more damage than warriors but \nhave a chance to dodge");
        rogueDesc.setId("info_label");
        Label mageDesc = new Label("Mages use staves and magical attacks.\n"+
                                    "They use mana to unleash spells against \ntheir enemies. Mages use cloth armor\n" +
                                    "which is the weakest armor available \n but they use magic to absorb a portion of damage" +
                                    "\nto compensate for this");

        mageDesc.setId("info_label");

        warriorVBox.getChildren().addAll(warriorName, warriorButton, warriorDesc);
        rogueVBox.getChildren().addAll(rogueName, rogueButton, rogueDesc);
        mageVBox.getChildren().addAll(mageName, mageButton, mageDesc);
        
        hBox.getChildren().addAll(warriorVBox, rogueVBox, mageVBox);

        scene.setRoot(hBox);

    }
    
    /**
     * Customize character name after selecting what class to play
     * @param scene scene of the character select for styling
     * @param characterNum number of the class the player chose to play
     * @param className the label class name
     */
    private void customizeCharacter(Scene scene, int characterNum, Label className) {
        VBox vBox = new VBox(20);
        vBox.setId("main_menu");
        vBox.setAlignment(Pos.CENTER);
    
        TextArea nameBox = new TextArea();
        nameBox.setPrefWidth(WIDTH / 7.0);
        nameBox.setMaxWidth(WIDTH / 7.0);
        nameBox.setMaxHeight(20);
        nameBox.setPromptText("Enter Name");
        
        
        Button beginButton = new Button("Begin");
        beginButton.setId("start_button");
        beginButton.setPrefSize(WIDTH / 8.0, HEIGHT / 8.0);
        
        Button returnButton = new Button("Back");
        returnButton.setId("start_button");
        returnButton.setPrefSize(WIDTH / 8.0, HEIGHT / 8.0);
        
        Label noNameEnteredLabel = new Label("You must enter a name for the character");
        noNameEnteredLabel.setId("no_name_toast_invis");
        
        returnButton.setOnAction(actionEvent -> {
            characterSelect(scene);
        });
        
        beginButton.setOnAction(actionEvent -> {                                    //begin button checks to make sure player actually entered a name if they did then start a new game
            String characterName = nameBox.getText();
            if ((characterName == null || characterName.equals(""))) {
                if (noNameEnteredLabel.getId().equals("no_name_toast_invis"))
                    noNameEnteredLabel.setId("no_name_toast");
            }
            else {
                new Game(primaryStage, characterNum, characterName).run();
            }
            
            
        });
        
        vBox.getChildren().addAll(className, nameBox, beginButton, returnButton, noNameEnteredLabel);
        scene.setRoot(vBox);
        
    }


}
