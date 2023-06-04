package modules.CA.RDCA;

import modules.CA.CA_Class;
import modules.CA.Cells.Cell;
import modules.GUI.SubGUI.CA_Console;
import modules.Window;
import org.joml.Matrix3f;

import java.util.Arrays;
import java.util.Map;

public class RDCA extends CA_Class {
    private final int CHEMICALS = 2;

    public int cellSize = CA_Console.getCellSize();
    public int rowsToRender = CA_Console.getRowsToRender();

    //Fields for Compute Shader
    public float diffusionRateA;
    public float diffusionRateB;
    public float feedRate;
    public float killRate;
    public Matrix3f neighborhood;

    //Cells
    private final int pixelsX = Window.getWidth();
    private final int pixelsY = 3;
    private final int cellsX = pixelsX / cellSize;
    private final int cellsY = pixelsY / cellSize;

    private final int WIDTH = cellsX * CHEMICALS;
    private final int HEIGHT = cellsY;
    public float[][] currentGeneration; // [chemicalA, chemicalB, chemicalA, chemicalB...]
    public float[][] nextGeneration;

    //Compute Shaders
    private RDCA_NextGen_ComputeShader nextGenShader;

    public void init(){
        diffusionRateA = RDCAData.diffusionRateA.get();
        diffusionRateB = RDCAData.diffusionRateB.get();
        feedRate = RDCAData.feedRate.get();
        killRate = RDCAData.killRate.get();

        neighborhood = new Matrix3f();
        neighborhood.m00 = RDCAData.neighborhood[0][0];
        neighborhood.m01 = RDCAData.neighborhood[0][1];
        neighborhood.m02 = RDCAData.neighborhood[0][2];

        neighborhood.m10 = RDCAData.neighborhood[1][0];
        neighborhood.m11 = RDCAData.neighborhood[1][1];
        neighborhood.m12 = RDCAData.neighborhood[1][2];

        neighborhood.m20 = RDCAData.neighborhood[2][0];
        neighborhood.m21 = RDCAData.neighborhood[2][1];
        neighborhood.m22 = RDCAData.neighborhood[2][2];

        initGenerations();

        //init nextGenShader
        nextGenShader = new RDCA_NextGen_ComputeShader();
        nextGenShader.setTEXTURE_WIDTH(WIDTH);
        nextGenShader.setTEXTURE_HEIGHT(rowsToRender);
        nextGenShader.setVECTOR_SIZE(CHEMICALS);
        nextGenShader.compile();
        nextGenShader.createTexture();
        nextGenShader.use(); //initalize

        computeNextGen();
    }

    public void initGenerations(){
        currentGeneration = new float[HEIGHT][WIDTH];
        nextGeneration = new float[HEIGHT][WIDTH];

        for(int i = 0; i < HEIGHT ; i++){
            for(int j = 1; j < WIDTH; j += 2){
                currentGeneration[i][j] = 1.00f;
                nextGeneration[i][j] = 1.00f;
            }
        }
    }

    public void computeNextGen(){
        nextGenShader.computeNextGen(currentGeneration, nextGeneration);
    }

    public void render(){

    }
}
