package modules.GUI.SubGUI;

import imgui.ImBool;
import imgui.ImGui;
import imgui.ImString;
import imgui.enums.ImGuiCond;
import imgui.enums.ImGuiTabBarFlags;
import imgui.enums.ImGuiWindowFlags;
import modules.FileHandlers.SettingsFileHandler;
import modules.GUI.MainGUI;
import modules.Window;

public class Settings implements  SubGUI_Interface{
    private static int[] windowDimensions = new int[2];
    private static int[] mainGUIDimensions = new int[2];
    private static int[] subGUIDimensions = new int[2];

    public static void render(ImBool p_open){

        //fps, gui startwidth & height and sub gui width & height, hotkeys
        ImGui.setNextWindowSize(MainGUI.subGUIWidth, MainGUI.subGUIHeight,  ImGuiCond.Always);

        if(ImGui.begin("General CA Settings", p_open, ImGuiWindowFlags.MenuBar)) {
            if(ImGui.beginTabBar("##tab2",  ImGuiTabBarFlags.None)) {
                if(ImGui.beginTabItem("Window")) {
                    ImGui.inputInt2("##input3", windowDimensions);
                    ImGui.sameLine();
                    if(ImGui.button("Window Dimensions")){
                        SettingsFileHandler.updateSettingsMap("windowwidth" + " " + windowDimensions[0]);
                        SettingsFileHandler.updateSettingsMap("windowheight" + " " + windowDimensions[1]);

                        SettingsFileHandler.updateAssociatedClasses();
                    }
                    ImGui.textColored(1.0f, 0.0f, 0.0f, 1.0f, "IMPORTANT!! Restart program once you've submitted the window dimensions");
                    ImGui.endTabItem();
                }
                if(ImGui.beginTabItem("GUI")){
                    ImGui.inputInt2("##input12312", mainGUIDimensions);
                    ImGui.sameLine();
                    if(ImGui.button("mainGUIDimensions")){
                        //save the windowWidth and height to a file
                        SettingsFileHandler.updateSettingsMap("mainguiwidth" + " " + mainGUIDimensions[0]);
                        SettingsFileHandler.updateSettingsMap("mainguiheight" + " " + mainGUIDimensions[1]);

                        SettingsFileHandler.updateAssociatedClasses();
                    }

                    ImGui.inputInt2("##input12321", subGUIDimensions);
                    ImGui.sameLine();
                    if(ImGui.button("subGUIDimensions")){
                        //save the windowWidth and height to a file
                        SettingsFileHandler.updateSettingsMap("subguiwidth" + " " + subGUIDimensions[0]);
                        SettingsFileHandler.updateSettingsMap("subguiheight" + " " + subGUIDimensions[1]);

                        SettingsFileHandler.updateAssociatedClasses();
                    }

                    ImGui.endTabItem();
                }
                if(ImGui.beginTabItem("Hot Keys")){
                    ImGui.bulletText("space - toggle mainGUI");
                    ImGui.bulletText("c - open CA Console");
                    ImGui.bulletText("m - open settings");

                    ImGui.endTabItem();
                }
                if(ImGui.beginTabItem("View")){
                    ImGui.text("FPS : " + Window.FPS);

                    ImGui.endTabItem();
                }


                ImGui.endTabBar();
            }
        }

        ImGui.end();
    }
}
