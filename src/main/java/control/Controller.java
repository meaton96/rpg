package control;

import entities.Player;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class Controller {

    private static final int WIDTH = 1440;
    private static final int HEIGHT = 810;
    
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

        HBox hBox = new HBox(60);

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
            new Game(3, "test");
        });

        Button rogueButton = new Button("");
        rogueButton.setId("rogue_button");
        rogueButton.setPrefSize(WIDTH / 8.0, HEIGHT / 8.0);

        Button mageButton = new Button("");
        mageButton.setId("mage_button");
        mageButton.setPrefSize(WIDTH / 8.0, HEIGHT / 8.0);

        Label warriorDesc = new Label("Insert info about the class");
        Label rogueDesc = new Label("Insert info about the class");
        Label mageDesc = new Label("Insert info about the class");

        warriorVBox.getChildren().addAll(warriorName, warriorButton, warriorDesc);
        rogueVBox.getChildren().addAll(rogueName, rogueButton, rogueDesc);
        mageVBox.getChildren().addAll(mageName, mageButton, mageDesc);

        hBox.getChildren().addAll(warriorVBox, rogueVBox, mageVBox);

        scene.setRoot(hBox);

    }

    private void customizeCharacter(int characterNum) {

    }

    public void loadBattleStage() {
        StackPane battlePane = new StackPane();
        battlePane.setId("hills_battle");
        Scene battleScene = new Scene(battlePane, WIDTH, HEIGHT);
        primaryStage.setScene(battleScene);
        battleScene.getStylesheets().addAll(styleSheet);
        //primaryStage.show();
    }

}
