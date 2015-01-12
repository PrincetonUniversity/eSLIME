/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by dbborens on 12/31/14.
 */
public class ContinuumAgentLinker {

    private ContinuumAgentNotifier notifier;
    private Function<Coordinate, Double> stateLookup;

    public ContinuumAgentLinker(ContinuumAgentNotifier notifier,
                                Function<Coordinate, Double> stateLookup) {

        this.notifier = notifier;
        this.stateLookup = stateLookup;
    }

    public double get(Supplier<Coordinate> c) {
        return stateLookup.apply(c.get());
    }

    public ContinuumAgentNotifier getNotifier() {
        return notifier;
    }
}
