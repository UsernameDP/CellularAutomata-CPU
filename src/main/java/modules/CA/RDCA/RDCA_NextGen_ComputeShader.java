package modules.CA.RDCA;

import org.joml.Matrix3f;
import org.lwjgl.BufferUtils;
import renderer.ComputeShader;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class RDCA_NextGen_ComputeShader extends ComputeShader {
    private int texture;

    public RDCA_NextGen_ComputeShader(){
        super("assets/shaders/RDCA_NextGen.glsl");
    }

    public void createTexture(){
        texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);

        // lets shader modify values without them being set(const)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        //create empty texture
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RG32F, TEXTURE_WIDTH, TEXTURE_HEIGHT, 0, GL_RG, GL_FLOAT, NULL);

        glBindImageTexture(0, texture, 0, false, 0, GL_READ_WRITE, GL_RG32F);
    }

    public void bindAllTextures(){
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);
    }


    private void set_texture_values(float[] values){
        glTexImage2D( GL_TEXTURE_2D, 0, GL_RG32F, TEXTURE_WIDTH, TEXTURE_HEIGHT, 0, GL_RG, GL_FLOAT, values );
    }
    private float[] get_texture_values(){
        int collection_size = TEXTURE_WIDTH * TEXTURE_HEIGHT;
        float[] output = new float[collection_size];

        glGetTexImage(GL_TEXTURE_2D, 0, GL_RG, GL_FLOAT, output);

        return output;
    }

    private void uploadFloat(String varName, float val){ //upload to a uniform value
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use(); //to upload the value, you need to use the program
        glUniform1f(varLocation, val); //inserting the value to the varLocation
    }
    private void uploadMat3f(String varName, Matrix3f mat3){
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void computeNextGen(float[][] currentGeneration, float[][] nextGeneration){
        float[] buffer = new float[TEXTURE_WIDTH * TEXTURE_HEIGHT];
        System.arraycopy(currentGeneration[0], 0, buffer, 0, TEXTURE_WIDTH);
        System.arraycopy(currentGeneration[1], 0, buffer, TEXTURE_WIDTH, TEXTURE_WIDTH);
        System.arraycopy(currentGeneration[2], 0, buffer, TEXTURE_WIDTH * 2, TEXTURE_WIDTH);

        set_texture_values(buffer);
        use();
        dispatch();
        waitTillDone();

        float[] dataArr = get_texture_values();

        String outputString = "";
        for(float data : dataArr){
            outputString += data + ",";
        }
        System.out.println(outputString);
    }
}
