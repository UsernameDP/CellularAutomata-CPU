package modules.FileHandlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileHandler {


    public static String readFile(String filepath){
        if(!getFileExtension(filepath).strip().equals("txt")){
            throw new Error("readFile did not recieve a .txt file");
        }
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

        return stringBuffer;
    }

    public static void writeToFile(String text, String filePath){


        try(FileWriter fileWriter = new FileWriter(filePath, false)){
            fileWriter.write(text);
        }catch(IOException err){
            err.printStackTrace();
        }
    }

    public static String getFileExtension(String filePath) {
        File file = new File(filePath);

        if (file.exists()) {
            String fileName = file.getName();
            int dotIndex = fileName.lastIndexOf(".");
            if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                return fileName.substring(dotIndex + 1);
            }
        }

        return "";
    }
}
