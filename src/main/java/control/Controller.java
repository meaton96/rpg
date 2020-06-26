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
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class Controller {

    public static final int WIDTH = 1440;
    public static final int HEIGHT = 810;
    
    private Font gameFont;

    //private final Application app;
    private final Stage primaryStage;
    private Player player;
    private String styleSheet;
    

    public void start() {
        gameFont = Font.loadFont(getClass().getClassLoader().getResourceAsStream("immortal.ttf"), 40);
        
        styleSheet = getClass().getClassLoader().getResource("style.css").toExternalForm();
        mainMenu();
        primaryStage.show();
        //loadBattleStage();
    }

    private void mainMenu() {
        VBox mainMenuPane = new VBox(50);
        mainMenuPane.setId("main_menu");
        mainMenuPane.setAlignment(Pos.CENTER);


        Button start = new Button("Start");
        start.setId("start_button");
        start.setPrefSize(WIDTH / 8.0, HEIGHT / 8.0);
        start.setFont(gameFont);
        start.setTextFill(Color.rgb(255, 255, 255));
        
        
        Label rules = new Label("Insert rules/instructions here!!");
        mainMenuPane.getChildren().addAll(start, rules);


        Scene menuScene = new Scene(mainMenuPane, WIDTH, HEIGHT);
        menuScene.getStylesheets().addAll(styleSheet);
        primaryStage.setScene(menuScene);

        start.setOnAction(actionEvent -> {
            characterSelect(menuScene);
        });
    }

    private void characterSelect(Scene scene) {
        scene.getStylesheets().add(getClass().getClassLoader().getResource("characterSelectStyle.css").toExternalForm());

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

        Label warriorDesc = new Label("Insert info about the class");
        Label rogueDesc = new Label("Insert info about the class");
        Label mageDesc = new Label("Insert info about the class");

        warriorVBox.getChildren().addAll(warriorName, warriorButton, warriorDesc);
        rogueVBox.getChildren().addAll(rogueName, rogueButton, rogueDesc);
        mageVBox.getChildren().addAll(mageName, mageButton, mageDesc);

        hBox.getChildren().addAll(warriorVBox, rogueVBox, mageVBox);

        scene.setRoot(hBox);

    }

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
        beginButton.setFont(gameFont);
        beginButton.setTextFill(Color.rgb(255, 255, 255));
        
        Button returnButton = new Button("Back");
        returnButton.setId("start_button");
        returnButton.setPrefSize(WIDTH / 8.0, HEIGHT / 8.0);
        returnButton.setFont(gameFont);
        returnButton.setTextFill(Color.rgb(255, 255, 255));
        
        Label noNameEnteredLabel = new Label("You must enter a name for the character");
        noNameEnteredLabel.setId("no_name_toast_invis");
        
        returnButton.setOnAction(actionEvent -> {
            characterSelect(scene);
        });
        
        beginButton.setOnAction(actionEvent -> {
            String characterName = nameBox.getText();
            if ((characterName == null || characterName.equals(""))) {
                if (noNameEnteredLabel.getId().equals("no_name_toast_invis"))
                    noNameEnteredLabel.setId("no_name_toast");
            }
            else {
                new Game(primaryStage, characterNum, characterName);
            }
            
            
        });
        
        vBox.getChildren().addAll(className, nameBox, beginButton, returnButton, noNameEnteredLabel);
        scene.setRoot(vBox);
        
    }

}
