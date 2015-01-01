/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package cells;

import control.identifiers.Coordinate;
import layers.continuum.ContinuumAgentLinker;
import layers.continuum.RelationshipTuple;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Cells can manipulate the local concentrations of continuum
 * layers by either exponentiating the local concentration
 * (decay, feedback) or by directly adding/subtracting from the
 * local concentration at a constrant rate(source/sink).
 *
 * The RelationshipManager reports the value of these
 * relationships, as well as notifying the related fields when
 * the cell dies.
 *
 * Created by dbborens on 12/30/14.
 */
public class AgentContinuumManager {

    private AgentContinuumIndex exp;
    private AgentContinuumIndex inj;

    private Supplier<Coordinate> locator;           // Locates self
    private AgentContinuumScheduler scheduler;      // Adds new relationships

    public AgentContinuumManager(Supplier<Coordinate> locator, Function<String, ContinuumAgentLinker> incomingLinker, BehaviorCell callback) {
        this.locator = locator;

        inj = new AgentContinuumIndex();
        exp = new AgentContinuumIndex();

        scheduler = new AgentContinuumScheduler(incomingLinker, callback, locator, inj, exp);
    }


    public AgentContinuumLinker getOutgoingLinker() {
        Coordinate c = locator.get();
        Function<String, RelationshipTuple> injLookup = id -> new RelationshipTuple(c, inj.getMagnitude(id));
        Function<String, RelationshipTuple> expLookup = id -> new RelationshipTuple(c, exp.getMagnitude(id));
        AgentContinuumLinker linker = new AgentContinuumLinker(injLookup, expLookup);

        return linker;
    }

    public AgentContinuumScheduler getScheduler() {
        return scheduler;
    }
}
