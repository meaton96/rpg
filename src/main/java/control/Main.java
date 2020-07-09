package control;


import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setResizable(false);
        primaryStage.setTitle("RPG Game");
        Controller controller = new Controller(primaryStage);
        controller.start();
        //todo
        //
        //4.add a few spells - wednesday ( might need to hand in this day)
        //5.add gear drops - thursday
        //6.addbackpack system to allow player to equip new gear - friday/saturday
        //7.beta 1.0 finished ready to hand in :)
        
    }


    public static void main(String[] args) {
        launch(args);
    }
}
