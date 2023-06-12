package modules.CA;

import modules.CA.RDCA.RDCA;

public class CADriver {
    private static CA_Class runningCA;

    private static String runningCAString;
    private static boolean update = false;

    public static void setRunningCA(String runningCAs){
        update = true;
        runningCAString = runningCAs;
        if(runningCAs == "RDCA"){
            CADriver.runningCA = new RDCA();
            CADriver.runningCA.init();
        }
    }

    public static void render(){
        if(runningCA != null){
            runningCA.render();

            if(update == true){
                runningCA.nextGen();
                runningCA.updateRenderer();
            }
        }
    }

    public static void setRunningCA(){
        CADriver.runningCA = null;
    }
    public static CA_Class getRunningCA(){
        return CADriver.runningCA;
    }
    public static String getRunningCAString() {
        return runningCAString;
    }
    public static void setUpdate(boolean update){CADriver.update = update;}
    public static boolean getUpdate(){return update;}
}
