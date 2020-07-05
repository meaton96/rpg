package control;

import animation.SpriteAnimation;
import javafx.animation.Animation;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private static final Image IMAGE = new Image("/images/Rogue/Idle/idle.png");
    private static final Image IMAGE_1 = new Image("/images/Rogue/Attack/attack.png");

    private static final int COLUMNS  =   18;
    private static final int COUNT    =  18;
    private static final int OFFSET_X =  0;
    private static final int OFFSET_Y =  0;
    private static final int WIDTH    = 128;
    private static final int HEIGHT   = 128;


    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setResizable(false);
        primaryStage.setTitle("RPG Game");
        Controller controller = new Controller(primaryStage);
        controller.start();

         /*
        primaryStage.setTitle("Animation Test");

        final ImageView imageView = new ImageView(IMAGE);
        imageView.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
    
        final ImageView imageView1 = new ImageView(IMAGE_1);
        imageView.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
        
        final Animation animation = new SpriteAnimation(
                imageView,
                Duration.millis(2400),
                COUNT, COLUMNS,
                OFFSET_X, OFFSET_Y,
                WIDTH, HEIGHT
        );
        Animation animation1 = new SpriteAnimation(imageView1, Duration.millis(1000), 7, 7, 0, 0, 128, 128);
        animation1.setCycleCount(Animation.INDEFINITE);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        animation1.play();
        
        
        HBox g = new HBox(50);
        g.setMinWidth(800);
        g.setMinHeight(600);
        g.setAlignment(Pos.CENTER);
        g.getChildren().addAll(imageView, imageView1);
    
        Task<Integer> task = new Task<Integer>() {
            @Override public Integer call() throws Exception {
                int iterations;
                for (iterations = 0; iterations < 100000; iterations++) {
                    if (isCancelled()) {
                        break;
                    }
                    System.out.println("Iteration " + iterations);
                }
                return iterations;
            }
        };
        
        Scene scene = new Scene(g);
        //todo working on how to implement multithreading for updating entity hp as to not pause animations
        //todo also need to work on animations
        //push to alpha branch what i have now is passable to hand in
        
        //2.fix animations for combat - monday
        //3.walking animation - tuesday
        //4.add a few spells - wednesday ( might need to hand in this day)
        //5.add gear drops - thursday
        //6.addbackpack system to allow player to equip new gear - friday/saturday
        //7.beta 1.0 finished ready to hand in :)  - hand in monday
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.SPACE) {
              //  task.
            }
        });
        
        primaryStage.setScene(scene);
        primaryStage.show();

        */

    }


    public static void main(String[] args) {
        launch(args);
    }
}
