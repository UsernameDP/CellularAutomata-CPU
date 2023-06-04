package renderer;

import org.joml.Vector2f;
import modules.CA.Cells.Cell;
import modules.Window;
import util.AssetPool;

import static org.lwjgl.opengl.GL30.*;

public class RenderBatch { //batch renders multiple squares
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int INDICE_SIZE = 6;
    private final int VERTEX_SIZE_BYTES = (POS_SIZE + COLOR_SIZE) * Float.BYTES;

    private float[] vertexArray;
    private Cell[] cellArray;
    private int VBO, VAO;
    private Shader shader;
    private final String shaderPath = "assets/shaders/Square.glsl";

    private int maxBatchSize;
    private int currentCellNumber;
    private boolean hasRoom;

    public RenderBatch(int maxBatchSize){
        shader = AssetPool.getShader(shaderPath);
        shader.compile();

        this.maxBatchSize = maxBatchSize; //max number of cells
        vertexArray = new float[this.maxBatchSize * (POS_SIZE + COLOR_SIZE) * 4]; //4 vertex per square, 6 attributes for each vertex
        cellArray = new Cell[maxBatchSize];
        currentCellNumber = 0;
        hasRoom = true;
    }

    public void start(){
        //---------VAO-------
        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);

        //--------VBO---------
        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW); //configures memory in GPU, DYNAMMIC DRAW is needed so we can upload vertex data later using subData in render

        //-------EBO--------
        int EBO = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, generateIndices(), GL_STATIC_DRAW);

        //-------Setting Attributes------
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_SIZE * Float.BYTES);
        glEnableVertexAttribArray(1);
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
        elements[offSet] = addition + 0;
        elements[offSet + 1] = addition + 1;
        elements[offSet + 2] = addition + 2;

        //Triangle 2
        elements[offSet + 3] = addition + 3;
        elements[offSet + 4] = addition + 2;
        elements[offSet + 5] = addition + 1;
    }

    public void addCell(Cell cell){
        int index = currentCellNumber;
        cellArray[index] = cell;

        loadCellVertex(index);
        currentCellNumber += 1;
        if(currentCellNumber >= maxBatchSize){
            this.hasRoom = false;
        }
    }
    public void loadCellVertex(int index){ //adds a Cell's information in cellArray to vertexArray
        Cell cell = cellArray[index];
        int offSet = currentCellNumber * (POS_SIZE + COLOR_SIZE) * 4; //because of 4 vertices
        Vector2f coord;

        for(int i = 0; i < 4; i++){
            coord = cell.getVertex(i);
            vertexArray[offSet] = coord.x;
            vertexArray[offSet + 1] = coord.y;

            vertexArray[offSet + 2] = cell.getColor().x;
            vertexArray[offSet + 3] = cell.getColor().y;
            vertexArray[offSet + 4] = cell.getColor().z;
            vertexArray[offSet + 5] = cell.getColor().w;

            offSet += (POS_SIZE + COLOR_SIZE);
        }
    }

    public void render(){
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertexArray);

        shader.use();
        shader.uploadVec2("uResolution", new Vector2f(Window.getWidth(), Window.getHeight()));

        glBindVertexArray(VAO);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, currentCellNumber * INDICE_SIZE, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        shader.detach();
    }

    public boolean hasRoom(){
        return this.hasRoom;
    }
}
