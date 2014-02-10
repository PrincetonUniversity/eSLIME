package agent.targets;

import cells.BehaviorCell;
import geometry.Geometry;
import layers.LayerManager;
import layers.cell.CellLayer;
import layers.cell.CellLayerViewer;
import structural.identifiers.Coordinate;

import java.util.ArrayList;

/**
 * Targets specify which cells should receive the consequences
 * of an Action.
 * Created by dbborens on 2/7/14.
 */
public class TargetAllNeighbors extends Targeter {

    public TargetAllNeighbors(BehaviorCell callback, LayerManager layerManager) {
        super(callback, layerManager);
    }

    @Override
    public Coordinate[] report(BehaviorCell caller) {
        // Get geometry
        Geometry geom = layerManager.getCellLayer().getGeometry();

        // Get self coordinate
        Coordinate self = layerManager.getCellLayer().getLookupManager().getCellLocation(callback);

        // Get coordinates of neighbors from geometry
        Coordinate[] neighbors = geom.getNeighbors(self, Geometry.APPLY_BOUNDARIES);

        // Return the array
        return neighbors;
    }
}
