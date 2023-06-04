package renderer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.File;
import java.nio.FloatBuffer;
import java.util.Scanner;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int shaderProgram;

    private String vertexShaderSrc;
    private String fragmentShaderSrc;
    private String filepath;

    public Shader(String filepath){
        this.filepath = filepath;
        setShaderSrc(readFile(filepath));
    }

    private String readFile(String filepath){
        String stringBuffer = "";

        try{
            Scanner scanner = null;
            scanner = new Scanner(new File(filepath));

            while(scanner.hasNextLine()){
                stringBuffer += scanner.nextLine().strip() + "\n";
            }

        }catch(IOException err){
            err.printStackTrace();
        }

        if(stringBuffer.length() == 0){
            throw new Error("Failed to read");
        }

        return stringBuffer;
    }
    private void setShaderSrc(String buffer){
        int startIndx;
        int endIndx;

        String[] bufferSplit = buffer.split("(#type)( )+([a-zA-Z]+)");

        String firstShader;
        startIndx = buffer.indexOf("#type") + 5;
        endIndx = buffer.indexOf("\n");
        firstShader = buffer.substring(startIndx, endIndx).trim();

        String secondShader;
        startIndx = buffer.indexOf("#type", endIndx) + 5; //search after endindx
        endIndx = buffer.indexOf("\n", startIndx);
        secondShader = buffer.substring(startIndx, endIndx).trim();

        if(firstShader.toLowerCase().equals("vertex")){ //then bufferSplit[1]
            vertexShaderSrc = bufferSplit[1];
        }else if(firstShader.toLowerCase().equals("fragment")){
            fragmentShaderSrc = bufferSplit[1];
        }else{
            throw new Error("Unexpected shader : " + firstShader);
        }
//
        if(secondShader.toLowerCase().equals("vertex")){
            vertexShaderSrc = bufferSplit[2];
        }else if(secondShader.toLowerCase().equals("fragment")){
            fragmentShaderSrc = bufferSplit[2];
        }else {
            throw new Error("Unexpected shader : " + secondShader);
        }
    }

    public void compile(){
        int vertexShader;
        int fragmentShader;

        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSrc);
        glCompileShader(vertexShader);
        int success = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(vertexShader, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: ' " + filepath + "'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderi(vertexShader, len));
            assert  false : "";
        }

        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSrc);
        glCompileShader(fragmentShader);

        success = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentShader, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderi(fragmentShader, len));
            assert  false : "";
        }

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tLinking of shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }
    }

    public void use(){
        glUseProgram(shaderProgram);
    }
    public void detach(){
        glUseProgram(0);
    }


    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadFloat(String varName, float val){ //upload to a uniform value
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use(); //to upload the value, you need to use the program
        glUniform1f(varLocation, val); //inserting the value to the varLocation
    }
    public void uploadInt(String varName, int val){
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use(); //to upload the value, you need to use the program
        glUniform1f(varLocation, val); //inserting the value to the varLocation
    }

    public void uploadVec2(String varName, Vector2f vec2){
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        FloatBuffer vecBuffer = BufferUtils.createFloatBuffer(2);
        vec2.get(vecBuffer);
        glUniform2fv(varLocation, vecBuffer);
    }
}
