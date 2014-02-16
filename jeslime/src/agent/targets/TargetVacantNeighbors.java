package agent.targets;

import cells.BehaviorCell;
import geometry.Geometry;
import layers.LayerManager;
import layers.cell.CellLayerViewer;
import structural.identifiers.Coordinate;

import java.util.ArrayList;
import java.util.Random;

/**
 * Targets specify which cells should receive the consequences
 * of an Action.
 * Created by dbborens on 2/7/14.
 */
public class TargetVacantNeighbors extends TargetRule {

    @Override
    public TargetRule clone(BehaviorCell child) {
        return new TargetVacantNeighbors(child, layerManager, maximum, random);
    }

    public TargetVacantNeighbors(BehaviorCell callback, LayerManager layerManager, int maximum, Random random) {
        super(callback, layerManager, maximum, random);
    }

    @Override
    protected Coordinate[] getCandidates(BehaviorCell caller) {
        // Get geometry
        Geometry geom = layerManager.getCellLayer().getGeometry();

        // Get cell layer viewer
        CellLayerViewer viewer = layerManager.getCellLayer().getViewer();

        // Get self coordinate
        Coordinate self = layerManager.getCellLayer().getLookupManager().getCellLocation(callback);

        // Get coordinates of neighbors from geometry
        Coordinate[] neighbors = geom.getNeighbors(self, Geometry.APPLY_BOUNDARIES);

        // Create an array list of neighbors that are vacant
        ArrayList<Coordinate> vacNeighbors = new ArrayList<>(neighbors.length);

        for (Coordinate neighbor : neighbors) {
            if (!viewer.isOccupied(neighbor)) {
                vacNeighbors.add(neighbor);
            }
        }

        // Convert to array
        Coordinate[] ret = vacNeighbors.toArray(new Coordinate[0]);

        // Return the array
        return ret;
    }
}
