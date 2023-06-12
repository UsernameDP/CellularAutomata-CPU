package modules.CA;

import modules.CA.Cells.Cell;
import org.joml.Vector2f;
import renderer.RenderBatch;

import java.util.ArrayList;
import java.util.List;

public abstract class CARenderer {
    private int MAX_BATCH_SIZE;

    public CARenderer(int MAX_BATCH_SIZE){
        this.MAX_BATCH_SIZE = MAX_BATCH_SIZE;
    }

    public int getMAX_BATCH_SIZE() {
        return MAX_BATCH_SIZE;
    }

    abstract public void render();
}
