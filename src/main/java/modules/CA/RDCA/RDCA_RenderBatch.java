package modules.CA.RDCA;


import modules.CA.CARenderBatch;
import modules.CA.Cells.RDCA_Cell;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL45.*;

public class RDCA_RenderBatch extends CARenderBatch {
    private final int CHEMICAL_SIZE = 2;
    private static String shaderPath;

    private RDCA_Cell[] cellArray;

    //Uniforms
    private static Vector3f chemicalAColor;
    private static Vector3f chemicalBColor;

    public RDCA_RenderBatch(int maxBatchSize){
        super(maxBatchSize, shaderPath);

        POS_SIZE = 2;
        COLOR_SIZE = 0;
        VERTEX_SIZE = (POS_SIZE + COLOR_SIZE + CHEMICAL_SIZE);
        VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

        cellArray = new RDCA_Cell[maxBatchSize];
        vertexArray = new float[maxBatchSize * VERTEX_SIZE * 4]; //4 vertex per square, 6 attributes for each vertex
    }

    public void start(){
        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);

        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        EBO = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, generateIndices(), GL_STATIC_DRAW);

        //Attributes
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, CHEMICAL_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_SIZE * Float.BYTES);
        glEnableVertexAttribArray(1);
    }
    public void addCell(RDCA_Cell cell){ //how can i have abstract classs to input rdca_celll
        int currentCellNumber = getCurrentCellNumber();

        int index = currentCellNumber;

        cellArray[index] = cell;

        loadCellVertex(index);
        setCurrentCellNumber(currentCellNumber + 1);
        if(getCurrentCellNumber() >= getMaxBatchSize()){
            setHasRoom(false);
        }
    }

    public void loadCellVertex(int index){
        RDCA_Cell cell = cellArray[index];
        int offSet = getCurrentCellNumber() * VERTEX_SIZE * 4;
        Vector2f coord;
        Vector2f chemicalCoord = cell.getChemicalValues();

        for(int i = 0; i < 4; i++){
            coord = cell.getVertex(i);
            vertexArray[offSet + 0] = coord.x;
            vertexArray[offSet + 1] = coord.y;

            vertexArray[offSet + 2] = chemicalCoord.x; //chemical A
            vertexArray[offSet + 3] = chemicalCoord.y; //chemical B

//            System.out.print(vertexArray[offSet + 0] + ", ");
//            System.out.print(vertexArray[offSet + 1] + ", ");
//            System.out.print(vertexArray[offSet + 2] + ", ");
//            System.out.print(vertexArray[offSet + 3] + "\n");

            offSet += VERTEX_SIZE;
        }
//        System.out.println("\n\n\n");
    }

    public void render(){
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertexArray);

        //do uniforms here
        getShader().use();
        getShader().uploadiVec2("uResolution", getuResolution());
        getShader().uploadVec3("chemicalAColor", chemicalAColor);
        getShader().uploadVec3("chemicalBColor", chemicalBColor);

        glBindVertexArray(VAO);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, getCurrentCellNumber() * INDICE_SIZE, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        getShader().detach();
    }

    public static void setShaderPath(String shaderPath) {
        RDCA_RenderBatch.shaderPath = shaderPath;
    }

    public static void setChemicalAColor(Vector3f chemicalAColor) {
        RDCA_RenderBatch.chemicalAColor = chemicalAColor;
    }
    public static void setChemicalBColor(Vector3f chemicalBColor) {
        RDCA_RenderBatch.chemicalBColor = chemicalBColor;
    }
}
