package util;

import entities.Enemy;
import entities.Entity;
import javafx.scene.Group;
import javafx.scene.image.Image;
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

/**
 * helper class to handle file input
 */
public final class FileUtil {

    /**
     * gets a javafx.scene.image.Image from the classpath
     * @param c the class to make resource stream
     * @param path the path of the image
     * @return an Image
     */
    public static Image getResourceStreamFromClass(final Class<?> c, final String path) {
        try {
            return new Image(c.getResourceAsStream(path));
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(path, e);
        }
    }
    public static Image getScaledImageFromStream(final Class<?> c, final String path, final double width, final double height) {
        try {
            return new Image(c.getResourceAsStream(path), width, height, true, true);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(path, e);
        }
    }

    /**
     * get a GameScene instance from the xml file
     * @param group the group to which the scene will be assigned
     * @param sceneNumber the scene number to get from the file
     * @param path the path to the xml file
     * @return a Gamescene instance pulled from the xml file
     */
    public static GameScene getSceneFromXML(final Group group, final int sceneNumber, final String path) {
    
        SAXBuilder builder = new SAXBuilder();                      //setup xml reader
        File xmlFile = new File(path);
        
        try {
            Document doc = builder.build(xmlFile);
            Element rootNode = doc.getRootElement();
            
            Element e = rootNode.getChildren().get(sceneNumber);           //get the root element

            return GameScene.builder()                                                  //build a game scene with the info from the xml file
                    .entityDrawY(Integer.parseInt(e.getChildText("ground_height")))
                    .name(e.getChildText("name"))
                    .group(group)
                    .filePath(e.getChildText("file_path"))
                    .build();
            
            
        } catch (IOException | JDOMException e) {
            System.out.println("File not Found");
        }
        return null;
    }

    /**
     * read the xml battle scene file and count the total number of options
     * @param path path to the xml file
     * @return integer number of different scene ins the xml file
     */
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

    /**
     * create a list of enemies of the given type
     * @param type the type/biome of the enemy
     * @param path the path to the enemy xml file
     * @param baseHealth enemy base hp
     * @return a list of enemies
     */
    public static List<Enemy> getEnemiesOfType(final String type, final String path, final int baseHealth) {
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(path);

        List<String> enemyTypes = List.of("", "desert", "field", "mine", "ruin", "snow", "swap");       //allowed enemy types

        try {
            if (!enemyTypes.contains(type)) {
                System.out.println("Enemy of type " + type + " doesn't exist");
                return null;
            }
            Element root = builder.build(xmlFile).getRootElement();         //build xml file reader
            List<Element> enemyElements = root.getChildren();
            List<Enemy> enemies = new ArrayList<>();
            for (Element e : enemyElements) {                               //iterate all xml nodes and create an enemy from the xml data
                for (int x = 1; x < 6; x++) {
                    String name = e.getChildText("name");
                    Image model = new Image(e.getChildText("model_path"));
                    String biome = e.getChildText("biome");
                    String attackPath = e.getChildText("attack_animation");
                    String idlePath = e.getChildText("idle_animation");
                    String deathPath = e.getChildText("death_animation");
                    int attackFrames = Integer.parseInt(e.getChildText("attack_frames"));
                    int deathFrames = Integer.parseInt(e.getChildText("death_frames"));
                    int idleFrames = Integer.parseInt(e.getChildText("idle_frames"));
                    
                    enemies.add(Enemy.builder()
                            .entityClass(Entity.Class.NONE)
                            .name(name)
                            .maxHealth(baseHealth)
                            .level(x)
                            .model(model)
                            .biome(biome)
                            .attackPath(attackPath)
                            .idlePath(idlePath)
                            .deathPath(deathPath)
                            .attackFrames(attackFrames)
                            .deathFrames(deathFrames)
                            .idleFrames(idleFrames)
                            .build());

                }
            }
            if (type.equals(""))                //if empty string was supplied return the entire list
                return enemies;

            return enemies.stream()                                         //otherwise filter only the elements with supplied type/biome string
                    .filter(x -> x.getBiome().equals(type.toLowerCase()))
                    .collect(Collectors.toList());
        }
        catch (IOException | JDOMException e) {
            System.out.println("Enemy file not found");
            System.out.println("File path: " + xmlFile.getAbsolutePath());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
