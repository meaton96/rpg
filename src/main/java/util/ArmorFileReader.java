package util;

import items.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * static class to read the xml armor file
 */
public final class ArmorFileReader {

    private final static String FILE_PATH = "src/main/resources/armor.xml";
    private static LinkedHashMap<String, Item> items;

    /**
     * must be called before attempting to pull any data from xml file (called during initializing of the game)
     */
    public static void init() {
    
        items = new LinkedHashMap<>();          //init the item map and xml file reader
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(FILE_PATH);
        
        try {
            Document doc = builder.build(xmlFile);
            Element rootNode = doc.getRootElement();
            
            for (Element e : rootNode.getChildren()) {          //get a node and pass it to other methods to build that type of item
                if (e.getName().equals("Armor"))
                    parseArmor(e);
                else if (e.getName().equals("Weapon")) {
                    parseWeapons(e);
                }
                //add added item types here for parsing
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * getter for item map
     * @return linked hash map of all items with keys as item name
     */
    public static LinkedHashMap<String, Item> getItemMap() {
        return items;
    }

    /**
     * prints the entire list of items
     */
    public static void printItems() {
        for (Item i: items.values()) {
            System.out.println(i);
        }
    }

    /**
     * getter for a specific item of the provided name
     * @param itemName the name of the item to retrieve
     * @return an item instance of the given name
     */
    public static Item getItemByName(String itemName) {
        return items.get(itemName);
    }

    /**
     * get a weapon of the given name
     * @param itemName the item name you want
     * @return instace of Weapon class with the given name
     */
    public static Weapon getWeaponByName(String itemName) {
        Item i = items.get(itemName);
        if (i instanceof Weapon)
            return (Weapon) i;
        else
            System.out.println("Weapon name was not given");
        return null;
    }

    /**
     * parse the node and create a weapon from it and add it to the item map
     * @param node the root xml node of the weapon
     */
    private static void parseWeapons(Element node) {
        //so ugly
        Weapon weapon = Weapon.builder()
                .name(node.getChildText("Name"))
                .weight(Double.parseDouble(node.getChildText("Weight")))
                .durability(Double.parseDouble(node.getChildText("Durability")))
                .level(Integer.parseInt(node.getChildText("Level")))
                .damageLow(Double.parseDouble(node.getChildText("DamageLow")))
                .damageHigh(Double.parseDouble(node.getChildText("DamageHigh")))
                .type(Weapon.getTypeFromString(node.getChildText("Type")))
                .agility(Integer.parseInt(node.getChildText("Agility")))
                .stamina(Integer.parseInt(node.getChildText("Stamina")))
                .strength(Integer.parseInt(node.getChildText("Strength")))
                .agility(Integer.parseInt(node.getChildText("Agility")))
                .intellect(Integer.parseInt(node.getChildText("Intellect")))
                .iconId(node.getChildText("icon_id"))
                .build();
        
        items.put(weapon.getName(), weapon);
    }

    /**
     * get a list of items that are of the level supplied
     * @param level the level of item you want
     * @return a list of items of the given level
     */
    public static List<Item> getArmorForLevel(int level) {
        
        return items.values()
                .stream()
                .filter(x -> x.getLevel() != level)
                .collect(Collectors.toList());      //filter out all the weapons that aren't the level provided
        
    }

    /**
     * parse an xml node and turn it into an armor instance and add it to the item map
     * @param node an xml node
     */
    private static void parseArmor(Element node) {
        String slotType = node.getChildText("Slot");
        Armor temp = null;
        //:puke:
        switch (slotType) {
            case "Chest":
                temp = Chest.builder()
                        .name(node.getChildText("Name"))
                        .weight(Double.parseDouble(node.getChildText("Weight")))
                        .durability(Double.parseDouble(node.getChildText("Durability")))
                        .level(Integer.parseInt(node.getChildText("Level")))
                        .armor(Integer.parseInt(node.getChildText("ArmorAmt")))
                        .type(Armor.getTypeFromString((node.getChildText("Type"))))
                        .stamina(Integer.parseInt(node.getChildText("Stamina")))
                        .strength(Integer.parseInt(node.getChildText("Strength")))
                        .agility(Integer.parseInt(node.getChildText("Agility")))
                        .intellect(Integer.parseInt(node.getChildText("Intellect")))
                        .iconId(node.getChildText("icon_id"))
                        .build();
                break;
            case "Helm":
                temp = Helm.builder()
                        .name(node.getChildText("Name"))
                        .weight(Double.parseDouble(node.getChildText("Weight")))
                        .durability(Double.parseDouble(node.getChildText("Durability")))
                        .level(Integer.parseInt(node.getChildText("Level")))
                        .armor(Integer.parseInt(node.getChildText("ArmorAmt")))
                        .type(Armor.getTypeFromString(node.getChildText("Type")))
                        .stamina(Integer.parseInt(node.getChildText("Stamina")))
                        .strength(Integer.parseInt(node.getChildText("Strength")))
                        .agility(Integer.parseInt(node.getChildText("Agility")))
                        .intellect(Integer.parseInt(node.getChildText("Intellect")))
                        .iconId(node.getChildText("icon_id"))
                        .build();
                break;
            case "Legs":
                temp = Legs.builder()
                        .name(node.getChildText("Name"))
                        .weight(Double.parseDouble(node.getChildText("Weight")))
                        .durability(Double.parseDouble(node.getChildText("Durability")))
                        .level(Integer.parseInt(node.getChildText("Level")))
                        .armor(Integer.parseInt(node.getChildText("ArmorAmt")))
                        .type(Armor.getTypeFromString(node.getChildText("Type")))
                        .stamina(Integer.parseInt(node.getChildText("Stamina")))
                        .strength(Integer.parseInt(node.getChildText("Strength")))
                        .agility(Integer.parseInt(node.getChildText("Agility")))
                        .intellect(Integer.parseInt(node.getChildText("Intellect")))
                        .iconId(node.getChildText("icon_id"))
                        .build();
                break;
            case "Boots":
                temp = Boots.builder()
                        .name(node.getChildText("Name"))
                        .weight(Double.parseDouble(node.getChildText("Weight")))
                        .durability(Double.parseDouble(node.getChildText("Durability")))
                        .level(Integer.parseInt(node.getChildText("Level")))
                        .armor(Integer.parseInt(node.getChildText("ArmorAmt")))
                        .type(Armor.getTypeFromString(node.getChildText("Type")))
                        .stamina(Integer.parseInt(node.getChildText("Stamina")))
                        .strength(Integer.parseInt(node.getChildText("Strength")))
                        .agility(Integer.parseInt(node.getChildText("Agility")))
                        .intellect(Integer.parseInt(node.getChildText("Intellect")))
                        .iconId(node.getChildText("icon_id"))
                        .build();
                break;
            default:
                System.out.println("SOMETHING WENT WRONG PANIC");
                break;
        }
        assert temp != null;
        items.put(temp.getName(), temp);
            
    }
}


