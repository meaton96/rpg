package util;

import items.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class ArmorFileReader {

    private final static String FILE_PATH = "src/main/resources/armor.xml";
    private static LinkedHashMap<String, Item> items;

    public static void init(final Class<?> c) {
    
        items = new LinkedHashMap<>();
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(FILE_PATH);
        
        try {
            Document doc = builder.build(xmlFile);
            Element rootNode = doc.getRootElement();
            
            for (Element e : rootNode.getChildren()) {
                if (e.getName().equals("Armor"))
                    parseArmor(c, e);
                else if (e.getName().equals("Weapon")) {
                    parseWeapons(c, e);
                }
                //add added item types here for parsing
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public static LinkedHashMap<String, Item> getItemMap() {
        return items;
    }
    public static void printItems() {
        for (Item i: items.values()) {
            System.out.println(i);
        }
    }
    public static Item getItemByName(String itemName) {
        return items.get(itemName);
    }
    
    public static Weapon getWeaponByName(String itemName) {
        Item i = items.get(itemName);
        if (i instanceof Weapon)
            return (Weapon) i;
        else
            System.out.println("Weapon name was not given");
        return null;
    }
    private static void parseWeapons(Class<?> c, Element node) {
        
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
    public static List<Item> getArmorForLevel(int level) {
        
        return items.values()
                .stream()
                .filter(x -> x.getLevel() != level)
                .collect(Collectors.toList());
        
    }
    
    private static void parseArmor(final Class<?> c, Element node) {
        String slotType = node.getChildText("Slot");
        Armor temp = null;
        
        if (slotType.equals("Chest")) {
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
        }
        else if (slotType.equals("Helm")) {
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
        }
        else if (slotType.equals("Legs")) {
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
        }
        else if (slotType.equals("Boots")) {
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
        }
        else {
            System.out.println("SOMETHING WENT WRONG PANIC");
        }
        assert temp != null;
        items.put(temp.getName(), temp);
            
    }
}


