package util;

import entities.Enemy;
import entities.Entity;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import ui.GameScene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                    .entityDrawY(Integer.parseInt(e.getChildText("ground_height")))
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

    public static List<Enemy> getEnemiesOfType(final String type, final String path, final int baseHealth) {
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(path);

        List<String> enemyTypes = List.of("", "desert", "field", "mine", "ruin", "snow", "swap");

        try {
            if (!enemyTypes.contains(type)) {
                System.out.println("Enemy of type " + type + " doesn't exist");
                return null;
            }
            Element root = builder.build(xmlFile).getRootElement();
            List<Element> enemyElements = root.getChildren();
            List<Enemy> enemies = new ArrayList<>();
            for (Element e : enemyElements) {
                for (int x = 1; x < 6; x++) {
                    String name = e.getChildText("name");
                    Image model = new Image(e.getChildText("model_path"));
                    String biome = e.getChildText("biome");
                    enemies.add(Enemy.builder()
                            .entityClass(Entity.Class.NONE)
                            .name(name)
                            .maxHealth(baseHealth)
                            .level(x)
                            .model(model)
                            .biome(biome)
                            .build());

                }
            }
            if (type.equals(""))
                return enemies;

            return enemies.stream()
                    .filter(x -> x.getBiome().equals(type.toLowerCase()))
                    .collect(Collectors.toList());
        }
        catch (IOException | JDOMException e) {
            System.out.println("Enemy file not found");
            System.out.println("File path: " + xmlFile.getAbsolutePath());
        }
        catch (Exception e) {
            System.out.println("Something else happened");
        }
        return null;
    }


}
