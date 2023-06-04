package modules.CA.Cells;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Cell { //essentially a square
    private Vector2f coord;
    private Vector4f color;
    private float width;

    public Cell(float width, Vector2f coord, Vector4f color){
        this.width = width;
        this.coord = coord;
        this.color = color;
    }

    public Vector2f getVertex(int n){
        //Vertices
        /*
        1       3

        0       2
         */

        //Color
        /*
        bottom-l = color.x
        top-l = color.y
        bottom-r = color.z
        top-r = color.w
         */
        Vector2f vertex = new Vector2f();

        if(n == 0){
            vertex.x = coord.x;
            vertex.y = coord.y;
        }else if(n == 1){
            vertex.x = coord.x;
            vertex.y = coord.y + width;
        }else if(n == 2){
            vertex.x = coord.x + width;
            vertex.y = coord.y;
        }else if(n == 3){
            vertex.x = coord.x + width;
            vertex.y = coord.y + width;
        }else{
            throw new Error("Vertex out of range, choose from 0 - 3");
        }

        return vertex;
    }

    public Vector4f getColor(){
        return this.color;
    }

    public void update(){

    }

    @Override
    public String toString(){
        String vertices = "";
        for(int i = 0; i < 4; i++){
            Vector2f vertex = getVertex(i);
            vertices += vertex.x + " , " + vertex.y + "\n";
        }
        return vertices;
    }
}
