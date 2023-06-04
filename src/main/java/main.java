import modules.FileHandlers.SettingsFileHandler;
import modules.Window;

public class main {
    public static void main(String[] args){
        SettingsFileHandler.initSettings();
        Window window = Window.get();
        window.run(); //while loop continuously running the
    }
}
