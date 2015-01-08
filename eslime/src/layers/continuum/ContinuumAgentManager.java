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
    private ContinuumAgentScheduler scheduler;
    private String id;
    public ContinuumAgentManager(ContinuumAgentScheduler scheduler, String id) {
        this.id = id;

        injIndex = new ContinuumAgentIndex(cell -> cell.getLinker().getInj(id));
        expIndex = new ContinuumAgentIndex(cell -> cell.getLinker().getExp(id));
        this.scheduler = scheduler;
    }

    public void apply() {
        scheduler.inject(injIndex.getRelationShips());
        scheduler.exponentiate(expIndex.getRelationShips());
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
