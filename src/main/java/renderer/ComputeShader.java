package renderer;

import util.AssetPool;


import static org.lwjgl.opengl.GL43.*;

public abstract class ComputeShader {

    public int shaderProgram;

    private String computeSrc;
    public String filepath;


    public int TEXTURE_WIDTH;
    public int TEXTURE_HEIGHT;
    public int VECTOR_SIZE;

    public ComputeShader(String filepath){
        computeSrc = AssetPool.getComputeShaderSrc(filepath);
        this.filepath = filepath;
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
        bindAllTextures();
    }
    public void waitTillDone(){
        glMemoryBarrier(GL_ALL_BARRIER_BITS);
    }

    public void dispatch(){
        //dimensions of work group :
        glDispatchCompute(TEXTURE_WIDTH / VECTOR_SIZE, TEXTURE_HEIGHT, 1);
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

    public void setVECTOR_SIZE(int VECTOR_SIZE){
        this.VECTOR_SIZE = VECTOR_SIZE;
    }
}
