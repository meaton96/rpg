package control;


import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Battle {
    
    //private final Scene scene;
    private final BorderPane mainPane;
    
    public Battle(String sceneName) {
        mainPane = new BorderPane();
        mainPane.setId(sceneName);
        //todo init scene
    }
    
    //todo
    // add static methods for getting a scene, reading scene from xml to set up variables for x/y writing of character
    // background image path ect..
}
