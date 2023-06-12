package modules.GUI.SubGUI;

import imgui.ImBool;
import imgui.ImGui;
import imgui.ImInt;
import imgui.ImString;
import imgui.enums.ImGuiCond;
import imgui.enums.ImGuiTabBarFlags;
import imgui.enums.ImGuiWindowFlags;
import modules.CA.CADataBase;
import modules.CA.CADriver;
import modules.CA.RDCA.RDCA;
import modules.FileHandlers.CAFileHandler;
import modules.GUI.MainGUI;

import java.util.ArrayList;

public class CA_Console implements  SubGUI_Interface{
    private static ArrayList<String> CAOptions = new ArrayList<>();
    private static String chosenCA;

    public static void init(){
        CA_Console.CAOptions.add("RDCA");
        CA_Console.CAOptions.add("NCA");
    }

    public static void render(ImBool p_open){
        ImGui.setNextWindowSize(MainGUI.subGUIWidth, MainGUI.subGUIHeight,  ImGuiCond.Always);

        if(ImGui.begin("CA Console", p_open, ImGuiWindowFlags.MenuBar)){ //by having p_open, you can edit p_open variable to false by pressing x

            //left
            ImGui.beginChild("left pane", 150, 100, true); //child div
            for(String CAOption : CAOptions){
                if(ImGui.selectable(CAOption, chosenCA == CAOption)){ //checks if the chosenCA is the CAOption
                    //if you click on the selectable, then chosenCA is set to the CAOptions
                    chosenCA = CAOption;
                }
            }
            ImGui.endChild();
            ImGui.sameLine();

            //right
            ImGui.beginChild("right pane", ImGui.getContentRegionAvailWidth(), -ImGui.getFrameHeightWithSpacing());
            ImGui.text("CA Type : ");
            ImGui.sameLine();
            if(chosenCA == null){
                ImGui.textColored(1.0f, 0.0f, 0.0f, 1.0f, "null");
            }else{
                ImGui.text(chosenCA);
            }
            ImGui.separator();

            if(ImGui.beginTabBar("##tab1", ImGuiTabBarFlags.None)){
                if(ImGui.beginTabItem("Rules")){
                    if(chosenCA == "RDCA"){
                        RDCA_Rules_Tab();
                    }
                    ImGui.endTabItem();
                }
                /*--------------------------------------------------------------*/
                if(ImGui.beginTabItem("Color")){
                    if(chosenCA == "RDCA"){
                        RDCA_Colors_Tab();
                    }
                    ImGui.endTabItem();
                }
                /*--------------------------------------------------------------*/

                if(ImGui.beginTabItem("Settings")){
                    if(CAOptions.contains(chosenCA)){
                        CASettingTabItem();
                    }
                    ImGui.endTabItem();
                }
                /*--------------------------------------------------------------*/
                if(ImGui.beginTabItem("Save & Load")){
                    if(CAOptions.contains(chosenCA)){
                        SaveAndLoadTabItem();
                    }
                    ImGui.endTabItem();
                }
                ImGui.endTabBar();
            }

            ImGui.endChild();
        }

        if(ImGui.button("Run")){
            //apply the settings and run the simulation, increasing time
            CADriver.setRunningCA(chosenCA);
        }
        ImGui.sameLine();
        if(ImGui.button("Stop / Continue")){
            //CA continues to render same scene until start
            if(CADriver.getUpdate() == false){
                CADriver.setUpdate(true);
            }else{
                CADriver.setUpdate(false);
            }
        }
        ImGui.sameLine();
        if(ImGui.button("Reset")){ //reset the CA that is currently running (delete all Cells)
            CADriver.setRunningCA();
        }

        ImGui.end();
    }

    //CA Rules:
    private static void RDCA_Rules_Tab(){
        if(ImGui.treeNode("Chemical A")){
            ImGui.inputFloat("Diffusion Rate A", CADataBase.RDCADataInstance.diffusionRateA);

            ImGui.newLine();
            ImGui.inputFloat3("##input5", CADataBase.RDCADataInstance.neighborhoodA[0]);
            ImGui.inputFloat3("Neighborhood##input6", CADataBase.RDCADataInstance.neighborhoodA[1]);
            ImGui.inputFloat3("##input7", CADataBase.RDCADataInstance.neighborhoodA[2]);

            ImGui.treePop();
        }
        if(ImGui.treeNode("Chemical B")){
            ImGui.inputFloat("Diffusion Rate B", CADataBase.RDCADataInstance.diffusionRateB);

            ImGui.newLine();
            ImGui.inputFloat3("##input4123123", CADataBase.RDCADataInstance.neighborhoodB[0]);
            ImGui.inputFloat3("Neighborhood##input4354243", CADataBase.RDCADataInstance.neighborhoodB[1]);
            ImGui.inputFloat3("##input982039", CADataBase.RDCADataInstance.neighborhoodB[2]);

            ImGui.treePop();
        }
        if(ImGui.treeNode("General")){
            ImGui.inputFloat("Feed Rate", CADataBase.RDCADataInstance.feedRate);
            ImGui.inputFloat("Kill Rate", CADataBase.RDCADataInstance.killRate);
            ImGui.inputFloat("dt", CADataBase.RDCADataInstance.dt);

            ImGui.newLine();
            ImGui.inputInt2("Width Spawn Range", CADataBase.RDCADataInstance.dimensionRange[0]);
            ImGui.inputInt2("Height Spawn Range", CADataBase.RDCADataInstance.dimensionRange[1]);

            ImGui.treePop();
        }

    }

    //CA Colors:
    private static void RDCA_Colors_Tab(){
        ImGui.colorEdit3("Chemical A Color", CADataBase.RDCADataInstance.chemicalAColor);
        ImGui.colorEdit3("Chemical B Color", CADataBase.RDCADataInstance.chemicalBColor);
    }

    //CA Settings :
    private static ImInt cellSize = new ImInt(1);
    private static void CASettingTabItem(){
        ImGui.inputInt("Cell Size ", cellSize);
    }


    //Save & Load
    private static ImString saveFileName = new ImString(100);
    private static ImString loadFileName = new ImString(100);
    private static void SaveAndLoadTabItem(){
        //Save
        ImGui.inputText("##input1", saveFileName);
        ImGui.sameLine();
        if(ImGui.button("Save")){
            CAFileHandler.saveRunningCA(saveFileName.get());
        }

        //Load
        ImGui.inputText("##input2", loadFileName);
        ImGui.sameLine();
        if(ImGui.button("Load")){
            CAFileHandler.loadCA(loadFileName.get());
        }

    }

    //Acessors
    public static int getCellSize() {
        return cellSize.get();
    }
    public static String getChosenCA(){return chosenCA;}
}
