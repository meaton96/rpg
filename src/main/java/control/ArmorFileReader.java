package control;

import items.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

public class ArmorFileReader {

    private final static String FILE_PATH = "src/main/resources/armor.xml";
    private static LinkedHashMap<String, Item> items;

    public static void init() {
    
        items = new LinkedHashMap<>();
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(FILE_PATH);
        
        try {
            Document doc = builder.build(xmlFile);
            Element rootNode = doc.getRootElement();
            
            for (Element e : rootNode.getChildren()) {
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
    
    public static LinkedHashMap<String, Item> getItemMap() {
        return items;
    }
    public static void printItems() {
        for (Item i: items.values()) {
            System.out.println(i);
        }
    }
    private static void parseWeapons(Element node) {
        
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
                .build();
        
        items.put(weapon.getName(), weapon);
    }
    public static List<Item> getArmorForLevel(int level) {
        
        List<Item> temp = new ArrayList<>();
        for (Item i : items.values()) {
            if (i.getLevel() == level)
                temp.add(i);
        }
        return temp;
        
        //this is close to working maybe?
        //items.entrySet().stream().filter(x -> x.getValue().getLevel() != level).forEach(temp.add());
        
    }
    
    private static void parseArmor(Element node) {
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
                    .build();
        }
        else {
            System.out.println("SOMETHING WENT WRONG PANIC");
        }
        assert temp != null;
        items.put(temp.getName(), temp);
            
    }
}


