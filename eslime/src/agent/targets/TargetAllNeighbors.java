/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.targets;

import cells.BehaviorCell;
import control.identifiers.Coordinate;
import geometry.Geometry;
import layers.LayerManager;
import processes.discrete.filter.Filter;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Targets specify which cells should receive the consequences
 * of an Action.
 * Created by dbborens on 2/7/14.
 */
public class TargetAllNeighbors extends TargetRule {

    public TargetAllNeighbors(BehaviorCell callback, LayerManager layerManager, Filter filter, int maximum, Random random) {
        super(callback, layerManager, filter, maximum, random);
    }

    @Override
    protected List<Coordinate> getCandidates(BehaviorCell caller) {
        // Get geometry
        Geometry geom = layerManager.getCellLayer().getGeometry();

        // Get self coordinate
        Coordinate self = layerManager.getCellLayer().getLookupManager().getCellLocation(callback);

        // Get coordinates of neighbors from geometry
        Coordinate[] neighbors = geom.getNeighbors(self, Geometry.APPLY_BOUNDARIES);

        List<Coordinate> ret = Arrays.asList(neighbors);

        // Return the array
        return ret;
    }

    @Override
    public TargetRule clone(BehaviorCell child) {
        return new TargetAllNeighbors(child, layerManager, filter, maximum, random);
    }

}
