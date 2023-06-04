package modules.FileHandlers;

import modules.CA.CADriver;
import modules.GUI.SubGUI.CA_Console;

public class CAFileHandler extends FileHandler {
    private static String directory = "assets/saves/CASaves/";

    public static void saveRunningCA(String fileName){

    }
    private static void RDCASaveMethod(){

    }

    public static void loadCA(String fileName){
        String text = readFile(fileName).strip();
        int endIndx = text.indexOf("\n");
        String CAName = text.substring(0, endIndx); //since start has been trimmed already

        if(CAName == "RDCA"){

        }
    }

    private static void loadRDCAMethod(String text){
        //Do stuff to RDCA class
    }

}
