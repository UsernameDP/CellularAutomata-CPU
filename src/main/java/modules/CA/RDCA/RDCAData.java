package modules.CA.RDCA;

import imgui.ImFloat;

public class RDCAData {
    public static ImFloat diffusionRateA = new ImFloat();
    public static ImFloat diffusionRateB = new ImFloat();
    public static ImFloat feedRate = new ImFloat();
    public static ImFloat killRate = new ImFloat();


    public static float[][] neighborhood = new float[3][3];
}
