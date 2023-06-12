package modules;

import com.sun.tools.javac.Main;
import modules.CA.CADriver;
import modules.FileHandlers.SettingsFileHandler;
import modules.GUI.ImGuiClass;
import modules.GUI.MainGUI;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    //Properties of Window
    private static int width;
    private static int height;

    private static Window window = null;

    private String title;
    private long glfwWindow;
    public static float FPS;
    public static float dt;
    private float r,g,b,a;

    private MainGUI ImGuiRenderer;

    //Module Imports


    public Window(){

        Window.width = SettingsFileHandler.settingsMap.get("windowwidth") == null ? 1920 : Integer.parseInt(SettingsFileHandler.settingsMap.get("windowwidth"));
        Window.height = SettingsFileHandler.settingsMap.get("windowheight") == null ? 1080 : Integer.parseInt(SettingsFileHandler.settingsMap.get("windowheight"));
        this.title = "Simulation_CA";
        r = 0; g = 0; b = 0; a = 0;
    }

    public void run(){
        System.out.println("Hello LWJGL" + Version.getVersion());

        initWindow();
        //Other inits here
        ImGuiRenderer = new MainGUI(glfwWindow);
        ImGuiRenderer.initImGui();
        ImGuiRenderer.init(); //init all subgui stuff

        loop();

        ImGuiRenderer.destroyImGui();
        //--manually freeing memory--
        //Free the memory once loop has exited
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

        System.out.println("Saving Settings....");
        SettingsFileHandler.updateSettingsFile();
    }

    public void initWindow(){
        //Set up an error callback
        GLFWErrorCallback.createPrint(System.err).set(); //Where the errors will be printed to

        //Initialize GLFW
        if (!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW."); //If we fail to init glfw
        }

        //Configure GLFW (Setting up Props)
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); //don't make it visible until we set all the props
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        //Create the window
        glfwWindow = glfwCreateWindow(Window.getWidth(), Window.getHeight(), this.title, NULL, NULL); //glfwWindow is a memory address where the create window returns a memory space
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        //Setting mouse listener
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            ImGuiClass.guiWidth = newWidth;
            ImGuiClass.guiHeight = newHeight;
        });
        minimize_maximize_window(); //this configures new size for imgui dimensions

        //Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync animation/refresh rate
        glfwSwapInterval(1); //same as window refresh rate

        //Make window visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities(); //Makes sure that we can use the bind (binding openGL objects s.t it can be displayed// )
    }

    public void loop(){
        float beginTime = (float)glfwGetTime();
        float endTime;
        Window.dt = -1.0f;


        while(!glfwWindowShouldClose(glfwWindow)){
            //Poll Events (any type of event)
            glfwPollEvents(); //lets us use event listeners

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT); //how to clear the buffer

            if(Window.dt >= 0 ){
                customRender(dt);
            }

            glfwSwapBuffers(glfwWindow);
            /*
             * Front buffer - currently being displayed
             * Back Buffer - on queue to be displayed
             * Swapping Buffers - swapping the back to front buffers
             * */

            endTime = (float)glfwGetTime();
            Window.dt = endTime - beginTime;
            beginTime = (float)glfwGetTime();
            Window.FPS = (1 / dt);
        }
    }

    public void customRender(float dt){ //all the gui, scene rendering goes here
        //We want Scene to render first, then GUI
        CADriver.render();
        ImGuiRenderer.update();
    }

    //Accessors & Modifiers
    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }

        return Window.window;
    }
    public static int getWidth(){
        return Window.width;
    }
    public static int getHeight(){
        return Window.height;
    }

    //Helper Functions
    private void minimize_maximize_window(){
        glfwIconifyWindow(glfwWindow);
        glfwMaximizeWindow(glfwWindow);
    }
}
