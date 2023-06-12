package modules.CA;

import org.joml.Vector2i;
import renderer.Shader;
import util.AssetPool;

public abstract class CARenderBatch { //batch renders multiple squares
    public int POS_SIZE;
    public int COLOR_SIZE;
    public static final int INDICE_SIZE = 6;
    public int VERTEX_SIZE;
    public int VERTEX_SIZE_BYTES;

    public int VBO, VAO, EBO;
    public float[] vertexArray;
    private Shader shader;

    private int maxBatchSize;
    private int currentCellNumber;
    private boolean hasRoom;

    //Static Variables all inherited classes would have
    private static Vector2i uResolution;

    public CARenderBatch(int maxBatchSize, String shaderPath) {
        shader = AssetPool.getShader(shaderPath);

        this.maxBatchSize = maxBatchSize; //max number of cells
        currentCellNumber = 0;
        hasRoom = true;
    }

    public int[] generateIndices(){
        //POS                                  //Vertices
        //bottom-l, top-l, bottom-r, top-r     0 1 2 , 3 2 1

        int[] elements = new int[maxBatchSize * INDICE_SIZE];
        for(int i  = 0; i < maxBatchSize; i ++){ // i stands for 1 cell
            generateIndice(elements, i);
        }

        return elements;
    }
    public void generateIndice(int[] elements, int index){
        //Vertices
        //0 1 2, 3 2 1 | 4 5 7 , 7 6 5

        int addition = index * 4; //How many times we need to add 4
        int offSet = index * INDICE_SIZE; //which cell we are on

        //Triangle 1
        elements[offSet + 0] = addition + 0;
        elements[offSet + 1] = addition + 1;
        elements[offSet + 2] = addition + 2;

        //Triangle 2
        elements[offSet + 3] = addition + 3;
        elements[offSet + 4] = addition + 2;
        elements[offSet + 5] = addition + 1;
    }

    public boolean hasRoom(){
        return this.hasRoom;
    }

    //Accessors & Modifiers
    public void setHasRoom(boolean hasRoom){
        this.hasRoom = hasRoom;
    }
    public int getMaxBatchSize() {
        return maxBatchSize;
    }
    public int getCurrentCellNumber() {
        return currentCellNumber;
    }
    public void setCurrentCellNumber(int currentCellNumber) {
        this.currentCellNumber = currentCellNumber;
    }
    public Shader getShader(){
        return shader;
    }
    public static void setuResolution(Vector2i uResolution){
        CARenderBatch.uResolution = uResolution;
    }
    public static Vector2i getuResolution(){
        return CARenderBatch.uResolution;
    }

    abstract public void start();
    abstract public void loadCellVertex(int index);
    abstract public void render();

}
