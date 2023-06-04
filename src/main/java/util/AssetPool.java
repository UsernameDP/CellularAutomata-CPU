package util;

import renderer.Shader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>(); //absolute path to get shader
    private static Map<String, String> computeShaderSrcs = new HashMap<>(); //abs path and src

    public static Shader getShader(String resourceName) {
        File file = new File(resourceName);
        if (AssetPool.shaders.containsKey(file.getAbsolutePath())) {
            return AssetPool.shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(resourceName);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static String getComputeShaderSrc(String resourceName){
        File file = new File(resourceName);
        if(AssetPool.computeShaderSrcs.containsKey(file.getAbsolutePath())){
            return AssetPool.computeShaderSrcs.get(file.getAbsolutePath());
        }else{
            String computeShaderSrc = readComputeShaderSrc(file.getAbsolutePath());
            AssetPool.computeShaderSrcs.put(file.getAbsolutePath(), computeShaderSrc);
            return computeShaderSrc;

        }
    }

    private static String readFile(String filepath){
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


    private static String readComputeShaderSrc(String filePath){
        String buffer = readFile(filePath);

        int startIndx;
        int endIndx;

        String[] bufferSplit = buffer.split("(#type)( )+([a-zA-Z]+)");

        String shaderName;
        startIndx = buffer.indexOf("#type") + 5;
        endIndx = buffer.indexOf("\n");
        shaderName = buffer.substring(startIndx, endIndx).trim();

        if(!shaderName.toLowerCase().equals("compute")){
            throw new Error(shaderName + " is not a compute shader");
        }

        return bufferSplit[1];
    }
}
