/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.targets;

import cells.BehaviorCell;
import control.identifiers.Coordinate;
import layers.LayerManager;
import processes.discrete.filter.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Targets specify which cells should receive the consequences
 * of an Action.
 * Created by dbborens on 2/7/14.
 */
public class TargetCaller extends TargetRule {
    @Override
    public TargetRule clone(BehaviorCell child) {
        return new TargetCaller(child, layerManager, filter, maximum, random);
    }

    public TargetCaller(BehaviorCell callback, LayerManager layerManager, Filter filter, int maximum, Random random) {
        super(callback, layerManager, filter, maximum, random);
    }

    @Override
    protected List<Coordinate> getCandidates(BehaviorCell caller) {
        Coordinate coord = layerManager.getCellLayer().getLookupManager().getCellLocation(caller);

        ArrayList<Coordinate> ret = new ArrayList<>(1);
        ret.add(coord);
        return ret;
    }
}
