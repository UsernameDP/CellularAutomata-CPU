package modules.CA;

import modules.CA.RDCA.RDCA;

public class CADriver {
    private static CA_Class runningCA;

    public static void setRunningCA(String runningCAs){
        if(runningCAs == "RDCA"){
            CADriver.runningCA = new RDCA();
            CADriver.runningCA.init();
        }
    }
    public static void setRunningCA(){
        CADriver.runningCA = null;
    }

    public static void render(){
        if(runningCA != null){
            runningCA.render();
        }
    }

    public static CA_Class getRunningCA(){
        return CADriver.runningCA;
    }
}
