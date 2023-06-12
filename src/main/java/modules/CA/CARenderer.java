package renderer;

import modules.CA.Cells.Cell;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private static int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer(){
        this.batches = new ArrayList<>();
    }

    public void add(Cell cell){ //adding a cell(square) to a batch
        boolean added = false;
        for(RenderBatch batch : batches){
            if(batch.hasRoom()){
                batch.addCell(cell);
                added = true;
                break;
            }
        }
        if(!added){
            RenderBatch batch = new RenderBatch(MAX_BATCH_SIZE);
            batch.start();
            batch.addCell(cell);
            batches.add(batch);
        }
    }

    public void render(){
        for(RenderBatch batch : batches){
            batch.render();
        }
    }
}
