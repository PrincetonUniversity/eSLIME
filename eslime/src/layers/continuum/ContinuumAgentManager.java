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
public class ContinuumAgentManager {

    private ContinuumAgentIndex index;
    private ReactionLoader loader;
    private String id;

    public ContinuumAgentManager(ReactionLoader loader, ContinuumAgentIndex index, String id) {
        this.id = id;

        this.index = index;
        this.loader = loader;
    }

    public void apply() {
        loader.apply(index.getRelationships());
    }

    public void reset() {
        index.reset();
    }

    public ContinuumAgentLinker getLinker(Function<Coordinate, Double> stateLookup) {
        ContinuumAgentNotifier notifier = index.getNotifier();
        return new ContinuumAgentLinker(notifier, stateLookup);
    }

    public String getId() {
        return id;
    }
}
