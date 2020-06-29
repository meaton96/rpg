package control;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import ui.GameScene;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    
    
    public static Image getResourceStreamFromClass(final Class<?> c, final String path) {
        try {
            return new Image(c.getResourceAsStream(path));
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(path, e);
        }
    }
    
    public static GameScene getSceneFromXML(final Pane pane, final int sceneNumber, final String path) {
    
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(path);
        
        try {
            Document doc = builder.build(xmlFile);
            Element rootNode = doc.getRootElement();
            
            Element e = rootNode.getChildren().get(sceneNumber);
            
            return GameScene.builder()
                    .entityDrawX(Integer.parseInt(e.getChildText("ground_height")))
                    .name(e.getChildText("name"))
                    .pane(pane)
                    .filePath(e.getChildText("file_path"))
                    .build();
            
            
        } catch (IOException | JDOMException e) {
            System.out.println("File not Found");
        }
        return null;
    }
    
    public static int getNumBattleScenes(final String path) {
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(path);
    
        try {
            return builder.build(xmlFile)
                    .getRootElement()
                    .getChildren()
                    .size();
            
            
        } catch (IOException | JDOMException e) {
            System.out.println("File not Found");
        }
        return 0;
    }

}
