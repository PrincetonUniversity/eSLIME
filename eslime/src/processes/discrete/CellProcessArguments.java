/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import control.arguments.Argument;
import geometry.set.CoordinateSet;

/**
 * Created by dbborens on 11/23/14.
 */
public class CellProcessArguments {
    private CoordinateSet activeSites;
    private Argument<Integer> maxTargets;

    public CellProcessArguments(CoordinateSet activeSites, Argument<Integer> maxTargets) {
        this.activeSites = activeSites;
        this.maxTargets = maxTargets;
    }

    public CoordinateSet getActiveSites() {
        return activeSites;
    }

    public Argument<Integer> getMaxTargets() {
        return maxTargets;
    }


}
