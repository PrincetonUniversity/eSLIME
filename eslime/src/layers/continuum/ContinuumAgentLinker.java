/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;

import java.util.function.Function;

/**
 * Created by dbborens on 12/31/14.
 */
public class ContinuumAgentLinker {

    private ContinuumAgentNotifier injNotifier;
    private ContinuumAgentNotifier expNotifier;
    private Function<Coordinate, Double> stateLookup;

    public ContinuumAgentLinker(ContinuumAgentNotifier injNotifier,
                                ContinuumAgentNotifier expNotifier,
                                Function<Coordinate, Double> stateLookup) {

        this.injNotifier = injNotifier;
        this.expNotifier = expNotifier;
        this.stateLookup = stateLookup;
    }

    public ContinuumAgentNotifier getInjNotifier() {
        return injNotifier;
    }

    public ContinuumAgentNotifier getExpNotifier() {
        return expNotifier;
    }

    public double get(Coordinate c) {
        return stateLookup.apply(c);
    }
}
