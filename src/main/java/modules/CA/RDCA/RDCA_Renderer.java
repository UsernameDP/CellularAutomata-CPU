package modules.CA.RDCA;

import modules.CA.CARenderBatch;
import modules.CA.CARenderer;
import modules.CA.Cells.RDCA_Cell;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class RDCA_Renderer extends CARenderer {
    public List<RDCA_RenderBatch> batches;

    private int TEXTURE_UNIT;
    private int TEXTURE_ID;
    private int TEXTURE_VALUE;


    public RDCA_Renderer(int MAX_BATCH_SIZE){
        super(MAX_BATCH_SIZE);
        this.batches = new ArrayList<>();
    }

    public void add(RDCA_Cell cell){ //adding a cell(square) to a batch
        boolean added = false;
        for(RDCA_RenderBatch batch : batches){
            if(batch.hasRoom()){
                batch.addCell(cell);
                added = true;
                break;
            }
        }
        if(!added){
            RDCA_RenderBatch batch = new RDCA_RenderBatch(getMAX_BATCH_SIZE());
            batch.start();
            batch.addCell(cell);
            batches.add(batch);
        }
    }

    public void render(){
        for(RDCA_RenderBatch batch : batches){
            batch.render();
        }
    }

    public void setTEXTURE_UNIT(int TEXTURE_UNIT) {
        this.TEXTURE_UNIT = TEXTURE_UNIT;
    }
    public void setTEXTURE_ID(int TEXTURE_ID) {
        this.TEXTURE_ID = TEXTURE_ID;
    }
    public void setTEXTURE_VALUE(int TEXTURE_VALUE) {
        this.TEXTURE_VALUE = TEXTURE_VALUE;
    }
}
