package modules.FileHandlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import imgui.ImFloat;
import modules.CA.CADataBase;
import modules.CA.CADriver;
import modules.CA.RDCA.RDCAData;
import modules.GUI.SubGUI.CA_Console;

import java.util.Arrays;


public class CAFileHandler extends FileHandler {
    private static String directory = "assets/saves/CASaves/";

    //Saving
    public static void saveRunningCA(String fileName){
        String runningCA = CA_Console.getChosenCA();
        if(runningCA == null){
            return;
        }

        /*
        Save format :
        1. name
        2. Specific Data...

        note : we are putting everything in json format
        */
        String data = "";

        if(runningCA == "RDCA"){
            data = runningCA + "\n" + saveRDCAData();
        }

        writeToFile(data, directory + fileName);
    }
    private static String saveRDCAData(){
        String jsonString = "";

        try{
            ObjectMapper mapper = new ObjectMapper();
            jsonString = mapper.writeValueAsString(CADataBase.RDCADataInstance);

        }catch(Exception err){
            err.printStackTrace();
        }
        return jsonString;
    }


    //Loading
    public static void loadCA(String fileName){
        String text = readFile(directory + fileName);
        int endIndx = text.indexOf("\n");
        String CAName = text.substring(0, endIndx).trim(); //since start has been trimmed already
        String data = text.substring(endIndx + 1);


        if(CAName.equals("RDCA")){
            loadRDCAData(data);
        }
    }

    private static void loadRDCAData(String data){
        try{
            ObjectMapper mapper = new ObjectMapper();
            RDCAData parsedData = mapper.readValue(data, RDCAData.class);

            CADataBase.RDCADataInstance = parsedData;
        }catch(Exception err){
            err.printStackTrace();
        }
    }


    public static void main(String[] args) { //demo of using Jackson JSON serialier(stringify) and de-serializer(parser)
        RDCAData data = new RDCAData();
        data.setDiffusionRateA(0.5f);
        data.setDiffusionRateB(0.3f);
        data.setFeedRate(0.1f);
        data.setKillRate(0.2f);
        data.setDt(1.0f);

        int[][] dimensionRange = {
                {0, 10},
                {0, 10}
        };
        data.setDimensionRange(dimensionRange);

        float[][] neighborhoodA = {
                {0.1f, 0.2f, 0.3f},
                {0.4f, 0.5f, 0.6f},
                {0.7f, 0.8f, 0.9f}
        };
        data.setNeighborhoodA(neighborhoodA);

        float[][] neighborhoodB = {
                {0.2f, 0.4f, 0.6f},
                {0.8f, 1.0f, 1.2f},
                {1.4f, 1.6f, 1.8f}
        };
        data.setNeighborhoodB(neighborhoodB);

        float[] chemicalAColor = {1.0f, 0.0f, 0.0f};
        data.setChemicalAColor(chemicalAColor);

        float[] chemicalBColor = {0.0f, 1.0f, 0.0f};
        data.setChemicalBColor(chemicalBColor);

        try {
            ObjectMapper mapper = new ObjectMapper();

            // Serialization
            String jsonString = mapper.writeValueAsString(data);
            System.out.println("Serialized JSON: " + jsonString);

            // Deserialization
            RDCAData parsedData = mapper.readValue(jsonString, RDCAData.class);

            System.out.println("Deserialized Object: " + parsedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

