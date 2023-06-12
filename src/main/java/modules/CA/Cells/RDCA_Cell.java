package modules.CA.Cells;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class RDCA_Cell extends Cell {
    //Within VBO
    private Vector2f chemicalValues;

    public RDCA_Cell(float width, Vector2f coord, Vector2f chemicalValues){
        super(width, coord);
        this.chemicalValues = chemicalValues;
    }

    public Vector2f getChemicalValues(){
        return this.chemicalValues;
    }
    public void setChemicalValues(Vector2f chemicalValues){this.chemicalValues = chemicalValues;}
}
