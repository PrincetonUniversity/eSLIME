package agent.targets;

import cells.BehaviorCell;
import geometry.Geometry;
import layers.LayerManager;
import structural.identifiers.Coordinate;

import java.util.Random;

/**
 * Targets specify which cells should receive the consequences
 * of an Action.
 * Created by dbborens on 2/7/14.
 */
public class TargetAllNeighbors extends TargetRule {

    public TargetAllNeighbors(BehaviorCell callback, LayerManager layerManager, int maximum, Random random) {
        super(callback, layerManager, maximum, random);
    }

    @Override
    protected Coordinate[] getCandidates(BehaviorCell caller) {
        // Get geometry
        Geometry geom = layerManager.getCellLayer().getGeometry();

        // Get self coordinate
        Coordinate self = layerManager.getCellLayer().getLookupManager().getCellLocation(callback);

        // Get coordinates of neighbors from geometry
        Coordinate[] neighbors = geom.getNeighbors(self, Geometry.APPLY_BOUNDARIES);

        // Return the array
        return neighbors;
    }

    @Override
    public TargetRule clone(BehaviorCell child) {
        return new TargetAllNeighbors(child, layerManager, maximum, random);
    }

}