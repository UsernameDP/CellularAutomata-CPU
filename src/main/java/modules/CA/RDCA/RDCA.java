package modules.CA.RDCA;

import modules.CA.CADataBase;
import modules.CA.CARenderBatch;
import modules.CA.CA_Class;
import modules.CA.Cells.RDCA_Cell;
import modules.GUI.SubGUI.CA_Console;
import modules.Window;
import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import modules.CA.CARenderer;
import org.joml.Vector3f;
import renderer.RenderBatch;

public class RDCA extends CA_Class {
    private final int CHEMICALS = 2;

    public int cellSize = CA_Console.getCellSize();

    //Uniforms for Compute Shader
    public float diffusionRateA = CADataBase.RDCADataInstance.diffusionRateA.get();
    public float diffusionRateB = CADataBase.RDCADataInstance.diffusionRateB.get();
    public float feedRate = CADataBase.RDCADataInstance.feedRate.get();
    public float killRate = CADataBase.RDCADataInstance.killRate.get();
    public float dt = CADataBase.RDCADataInstance.dt.get();
    public Matrix3f neighborhoodA = new Matrix3f();
    public Matrix3f neighborhoodB = new Matrix3f();
    public Vector2i widthRange = new Vector2i(CADataBase.RDCADataInstance.dimensionRange[0]);
    public Vector2i heightRange = new Vector2i(CADataBase.RDCADataInstance.dimensionRange[1]);
    public Vector3f chemicalAColor = new Vector3f(CADataBase.RDCADataInstance.chemicalAColor);
    public Vector3f chemicalBColor = new Vector3f(CADataBase.RDCADataInstance.chemicalBColor);

    //Cells
    private final int pixelsX = Window.getWidth();
    private final int pixelsY = Window.getHeight();
    private final int cellsX = (int) Math.ceil( (double) pixelsX / cellSize ); //doing this could potentionally cause problem because of normalized grid sys
    private final int cellsY = (int) Math.ceil( (double) pixelsY / cellSize );

    private final int WIDTH = cellsX;
    private final int HEIGHT = cellsY;

    //Srcs
    private String initGenSrc = "assets/shaders/RDCA/RDCA_initGen.glsl";
    private String nextGenSrc = "assets/shaders/RDCA/RDCA_NextGen.glsl";
    private String rendererSrc = "assets/shaders/RDCA/RDCA_RenderShader.glsl";

    //Renderer
    private int MAX_BATCH_SIZE = 10000; //in future, get this from CADataBase.RDCADataInstance
    RDCA_Renderer renderer;

    //Fields for CA
    private int generation = 0;
    private RDCA_Cell[][] grid = new RDCA_Cell[cellsY][cellsX];
    private RDCA_Cell[][] next = new RDCA_Cell[cellsY][cellsX];


    public void init(){
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                neighborhoodA.set(row, col, CADataBase.RDCADataInstance.neighborhoodA[row][col]);
                neighborhoodB.set(row, col, CADataBase.RDCADataInstance.neighborhoodB[row][col]);
            }
        }
        CARenderBatch.setuResolution(new Vector2i(pixelsX, pixelsY));
        RDCA_RenderBatch.setShaderPath(rendererSrc);
        RDCA_RenderBatch.setChemicalAColor(chemicalAColor);
        RDCA_RenderBatch.setChemicalBColor(chemicalBColor);
        initRenderer();
        initGen();
        updateRenderer();
    }

    public void initGen(){
       int offSetX = 1;
       int offSetY = 1;

       Vector2f coord;
       Vector2f chemicalValues;

       for(int xi = 0; xi < cellsX; xi += offSetX){
            for(int yi = 0; yi < cellsY; yi += offSetY){
                coord = new Vector2f(xi * cellSize, yi * cellSize);
                if(widthRange.x <= coord.x && coord.x <= widthRange.y           &&          heightRange.x <= coord.y && coord.y <= heightRange.y){
                    chemicalValues = new Vector2f(1, 1);
                }else{
                    chemicalValues = new Vector2f(1, 0);
                }

                grid[yi][xi] = new RDCA_Cell(cellSize, coord, chemicalValues);
                next[yi][xi] = new RDCA_Cell(cellSize, coord, chemicalValues);
            }
        }
    }
    public void initRenderer(){
        renderer = new RDCA_Renderer(MAX_BATCH_SIZE);
    }

    public void nextGen() {
        float chemicalA;
        float chemicalB;
        float nextChemicalA;
        float nextChemicalB;

        for(int i = 1; i < cellsX - 1; i++){
            for(int j = 1; j < cellsY - 1; j++){
                chemicalA = grid[j][i].getChemicalValues().x;
                chemicalB = grid[j][i].getChemicalValues().y;

                nextChemicalA = chemicalA +
                        ( (diffusionRateA * laplaceA(i , j)) -
                        (chemicalA * chemicalB * chemicalB) +
                        (feedRate * (1 - chemicalA)) ) * dt;
                nextChemicalB = chemicalB +
                        ( (diffusionRateB * laplaceB(i, j)) +
                        (chemicalA * chemicalB * chemicalB) -
                        ((killRate + feedRate) * chemicalB) ) * dt;

                nextChemicalA = constrain(nextChemicalA, 0, 1);
                nextChemicalB = constrain(nextChemicalB, 0, 1);


                next[j][i].setChemicalValues(new Vector2f( nextChemicalA, nextChemicalB));
            }
        }
    }

    private float laplaceA(int x, int y){
        float sumA = 0;

        sumA += grid[y+1][x-1].getChemicalValues().x * neighborhoodA.m00;
        sumA += grid[y+1][x].getChemicalValues().x * neighborhoodA.m10;
        sumA += grid[y+1][x+1].getChemicalValues().x * neighborhoodA.m20;

        sumA += grid[y][x-1].getChemicalValues().x * neighborhoodA.m01;
        sumA += grid[y][x].getChemicalValues().x * neighborhoodA.m11;
        sumA += grid[y][x+1].getChemicalValues().x * neighborhoodA.m21;

        sumA += grid[y-1][x-1].getChemicalValues().x * neighborhoodA.m02;
        sumA += grid[y-1][x].getChemicalValues().x * neighborhoodA.m12;
        sumA += grid[y-1][x+1].getChemicalValues().x * neighborhoodA.m22;

        return sumA;
    }
    private float laplaceB(int x, int y){
        float sumB = 0;

        sumB += grid[y+1][x-1].getChemicalValues().y * neighborhoodB.m00;
        sumB += grid[y+1][x].getChemicalValues().y * neighborhoodB.m10;
        sumB += grid[y+1][x+1].getChemicalValues().y * neighborhoodB.m20;

        sumB += grid[y][x-1].getChemicalValues().y * neighborhoodB.m01;
        sumB += grid[y][x].getChemicalValues().y * neighborhoodB.m11;
        sumB += grid[y][x+1].getChemicalValues().y * neighborhoodB.m21;

        sumB += grid[y-1][x-1].getChemicalValues().y * neighborhoodB.m02;
        sumB += grid[y-1][x].getChemicalValues().y * neighborhoodB.m12;
        sumB += grid[y-1][x+1].getChemicalValues().y * neighborhoodB.m22;

        return sumB;
    }
    public static float constrain(float value, float min, float max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }

    public void updateRenderer(){
        renderer.batches.clear();
        for(int xi = 0; xi < cellsX; xi += 1){
            for(int yi = 0; yi < cellsY; yi += 1){
                renderer.add(next[yi][xi]);
            }
        }

        swapGens();
    }

    private void swapGens(){
        RDCA_Cell[][] temp = next;
        next = grid;
        grid = temp;
    }


    public void render(){
        renderer.render();
    }
}
