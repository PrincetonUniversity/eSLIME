/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

/**
 * Created by dbborens on 12/31/14.
 */
public class ContinuumAgentManager {
    private ContinuumAgentIndex injIndex;    // Injection (source)
    private ContinuumAgentIndex expIndex;    // Exponentiation (feedback, decay)
    private ContinuumAgentScheduler scheduler;

    public ContinuumAgentManager(ContinuumAgentScheduler scheduler, String id) {
        injIndex = new ContinuumAgentIndex(cell -> cell.getAgentContinuumManager().getInj(id));
        expIndex = new ContinuumAgentIndex(cell -> cell.getAgentContinuumManager().getExp(id));
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
}
