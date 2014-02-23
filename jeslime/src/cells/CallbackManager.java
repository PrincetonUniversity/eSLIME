package cells;

import layers.LayerManager;
import layers.cell.CellLayer;
import structural.identifiers.Coordinate;

/**
 * A helper class for cells that triggers
 * update events related to the life cycle
 * of the cell, such as death or divisibility.
 *
 * Created by dbborens on 2/21/14.
 */
public class CallbackManager {

    private Cell cell;
    private LayerManager layerManager;

    public CallbackManager(Cell cell, LayerManager layerManager) {
        this.cell = cell;
        this.layerManager = layerManager;
    }

    /**
     * Signals to the LayerManager that the callback cell is dead
     * and should be removed from the simulation.
     */
    public void die() {
        CellLayer layer = layerManager.getCellLayer();
        Coordinate coord = layer.getLookupManager().getCellLocation(cell);
        layer.getUpdateManager().banish(coord);
    }

    /**
     * Signals to the LayerManager that the callback cell may have
     * changed its divisibility status and should be checked.
     */
    public void refreshDivisibility() {
        CellLayer layer = layerManager.getCellLayer();

        Coordinate coord = layer.getLookupManager().getCellLocation(cell);
        layer.getUpdateManager().refreshDivisibility(coord);
    }

    public LayerManager getLayerManager() {
        return layerManager;
    }
}
