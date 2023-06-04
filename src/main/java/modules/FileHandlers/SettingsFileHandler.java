package modules.FileHandlers;

import modules.GUI.MainGUI;

import java.util.HashMap;
import java.util.Map;

public class SettingsFileHandler extends FileHandler{
    public static Map<String, String> settingsMap = new HashMap<>();
    public static String filePath = "assets/saves/Setting.txt";

    public static void initSettings(){
        String[] keysAndValues = readFile(filePath).strip().split("\n");
        String[] splitted;
        for(String keyAndValue : keysAndValues){
            if(keyAndValue.length() > 0){
                splitted = keyAndValue.split(" ");
                settingsMap.put(splitted[0].toLowerCase(), splitted[1].toLowerCase());
            }
        }
    }

    public static void updateSettingsMap(String keyAndValue){
        String[] splitted = keyAndValue.strip().split(" ");
        if(splitted.length != 2){
            throw new Error("Wrong format for keyAndValue");
        }

        settingsMap.put(splitted[0].strip(), splitted[1].strip());
    }

    private static String settingsMap_toString(){
        String buffer = "";
        for(Map.Entry<String, String> entry : settingsMap.entrySet() ){
            buffer += entry.getKey() + " " + entry.getValue() + "\n";
        }

        return buffer.strip();
    }

    public static void updateSettingsFile(){
        writeToFile(settingsMap_toString(), filePath);
    }

    public static void updateAssociatedClasses(){ //files whose values changed based on this
        MainGUI.updateSettings();
    }
}
