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

    private ContinuumAgentIndex injIndex;    // Injection (source)
    private ContinuumAgentIndex expIndex;    // Exponentiation (feedback, decay)
    private ReactionLoader loader;
    private String id;

    public ContinuumAgentManager(ReactionLoader loader, ContinuumAgentIndex injIndex, ContinuumAgentIndex expIndex, String id) {
        this.id = id;

        this.injIndex = injIndex;
        this.expIndex = expIndex;
        this.loader = loader;
    }

    public void apply() {
        loader.inject(injIndex.getRelationships());
        loader.exponentiate(expIndex.getRelationships());
    }

    public void reset() {
        injIndex.reset();
        expIndex.reset();
    }

    public ContinuumAgentLinker getLinker(Function<Coordinate, Double> stateLookup) {
        ContinuumAgentNotifier injNotifier = injIndex.getNotifier();
        ContinuumAgentNotifier expNotifier = expIndex.getNotifier();
        return new ContinuumAgentLinker(injNotifier, expNotifier, stateLookup);
    }

    public String getId() {
        return id;
    }
}
