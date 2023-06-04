package modules.GUI;

import imgui.ImBool;
import imgui.ImGui;
import imgui.enums.ImGuiCond;
import imgui.enums.ImGuiWindowFlags;
import modules.FileHandlers.SettingsFileHandler;
import modules.GUI.SubGUI.*;

import static org.lwjgl.glfw.GLFW.*;

public class MainGUI extends ImGuiClass{
    private KeyToggle mainToggle;
    private KeyToggle CAConsoleToggle;
    private KeyToggle SettingsToggle;

    public static int mainGUIWidth;
    public static int mainGUIHeight;
    public static int subGUIWidth;
    public static int subGUIHeight;

    public void init(){
        CA_Console.init();
    }


    public MainGUI(long glfwWindow){
        super(glfwWindow);

        mainToggle = new KeyToggle(GLFW_KEY_SPACE, new ImBool(true), true, true);
        CAConsoleToggle = new KeyToggle(GLFW_KEY_C);
        SettingsToggle = new KeyToggle(GLFW_KEY_M);

        updateSettings();
    }
    public static void updateSettings(){
        mainGUIWidth = SettingsFileHandler.settingsMap.get("mainguiwidth") == null ? 550 : Integer.parseInt(SettingsFileHandler.settingsMap.get("mainguiwidth"));
        mainGUIHeight = SettingsFileHandler.settingsMap.get("mainguiheight") == null ? 680 : Integer.parseInt(SettingsFileHandler.settingsMap.get("mainguiheight"));
        subGUIWidth = SettingsFileHandler.settingsMap.get("subguiwidth") == null ? 500 : Integer.parseInt(SettingsFileHandler.settingsMap.get("subguiwidth"));
        subGUIHeight = SettingsFileHandler.settingsMap.get("subguiheight") == null ? 440 : Integer.parseInt(SettingsFileHandler.settingsMap.get("subguiheight"));
    }

    public void renderGUI() {
        ImGui.begin("CA GUI", ImGuiWindowFlags.MenuBar);
        ImGui.setWindowSize(mainGUIWidth, mainGUIHeight, ImGuiCond.Always);

        if(ImGui.collapsingHeader("CA Console")){
            ImGui.text("Description : ");

            if(ImGui.button("Open##button1")){
                CAConsoleToggle.getP_open().set(true);
            }
        }
        if(ImGui.collapsingHeader("Settings")){
            ImGui.text("Description : ");
            ImGui.textWrapped("Contains information about Window dimensions, main & sub gui dimensions, fps, and hotkeys");

            if(ImGui.button("Open##button2")){
                SettingsToggle.getP_open().set(true);
            }
        }

        updateToggles();
        handleToggles();

        ImGui.end();
    }

    public void updateToggles(){
        mainToggle.updateP_Open();
        CAConsoleToggle.updateP_Open();
        SettingsToggle.updateP_Open();
    }

    public void handleToggles(){
        if(mainToggle.getP_open().get()){ //main window
            ImGui.setWindowCollapsed(false); //if we should open, don't collpase
        }else{
            ImGui.setWindowCollapsed(true);
        }

        if(CAConsoleToggle.getP_open().get()){
            //if true, then we send into the subclass to render
            CA_Console.render(CAConsoleToggle.getP_open());
        }
        if(SettingsToggle.getP_open().get()){
            Settings.render(SettingsToggle.getP_open());
        }
    }
}
