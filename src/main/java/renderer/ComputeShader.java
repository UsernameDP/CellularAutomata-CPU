package renderer;

import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import util.AssetPool;


import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL45.*;

public abstract class ComputeShader {

    public int shaderProgram;

    private String computeSrc;
    public String filepath;


    public int TEXTURE_WIDTH;
    public int TEXTURE_HEIGHT;
    public int VECTOR_SIZE;

    public ComputeShader(){}

    public ComputeShader(String filepath){
        computeSrc = AssetPool.getComputeShaderSrc(filepath);
        this.filepath = filepath;
    }

    public void setComputeSrc(String filePath){ //you would have to compile again
        computeSrc = AssetPool.getComputeShaderSrc(filePath);
        this.filepath = filePath;
    }

    public void compile() {
        int computeShader = glCreateShader(GL_COMPUTE_SHADER);
        glShaderSource(computeShader, computeSrc);
        glCompileShader(computeShader);
        int success = glGetShaderi(computeShader, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(computeShader, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + this.filepath + "'\n\tCompute shader compilation failed.");
            System.out.println(glGetShaderInfoLog(computeShader, len)); // Use glGetShaderInfoLog instead of glGetShaderi
            assert false : "";
        }

        /* Program */
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, computeShader);
        glLinkProgram(shaderProgram);

        success = glGetProgrami(shaderProgram, GL_LINK_STATUS); // Use program instead of computeShader
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH); // Use program instead of computeShader
            System.out.println("ERROR: '" + this.filepath + "'\n\tLinking of shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgram, len)); // Use glGetProgramInfoLog instead of glGetProgrami
            assert false : "";
        }
    }

    public void use(){
        glUseProgram(shaderProgram);
    }
    public void waitTillDone(){
        glMemoryBarrier(GL_ALL_BARRIER_BITS);
    }
    public void dispatch(){
        //dimensions of work group :
        glDispatchCompute(TEXTURE_WIDTH, TEXTURE_HEIGHT, 1);
    }
    public void detach(){
        glUseProgram(0);
    }

    //Uniforms
    public void uploadFloat(String varName, float value){ //upload to a uniform value
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use(); //to upload the value, you need to use the program
        glUniform1f(varLocation, value); //inserting the value to the varLocation
    }
    public void uploadInt(String varName, int value){
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform1i(varLocation, value);
    }
    public void uploadMat3f(String varName, Matrix3f mat3){
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        use();
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }
    public void uploadVec2(String varName, Vector2f vec2){
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        FloatBuffer vecBuffer = BufferUtils.createFloatBuffer(2);
        vec2.get(vecBuffer);
        use();
        glUniform2fv(varLocation, vecBuffer);
    }
    public void uploadiVec2(String varName, Vector2i ivec2){
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        IntBuffer vecBuffer = BufferUtils.createIntBuffer(2);
        ivec2.get(vecBuffer);
        use();
        glUniform2iv(varLocation, vecBuffer);
    }

    abstract public void createTexture();
    abstract public void bindAllTextures(); //activate text and bind it


    //Accessors
    public int getTEXTURE_WIDTH() {
        return TEXTURE_WIDTH;
    }

    public void setTEXTURE_WIDTH(int TEXTURE_WIDTH) {
        this.TEXTURE_WIDTH = TEXTURE_WIDTH;
    }

    public int getTEXTURE_HEIGHT() {
        return TEXTURE_HEIGHT;
    }

    public void setTEXTURE_HEIGHT(int TEXTURE_HEIGHT) {
        this.TEXTURE_HEIGHT = TEXTURE_HEIGHT;
    }

    public int getVECTOR_SIZE(){return this.VECTOR_SIZE;}
    public void setVECTOR_SIZE(int VECTOR_SIZE){
        this.VECTOR_SIZE = VECTOR_SIZE;
    }
}
