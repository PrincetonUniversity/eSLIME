/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package cells;

import control.identifiers.Coordinate;
import factory.cell.Reaction;
import layers.continuum.ContinuumAgentLinker;
import layers.continuum.RelationshipTuple;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by dbborens on 12/31/14.
 *
 * Provides continuum access to cell reaction index.
 *
 * This class is a candidate for redesign. It is too complicated.
 * Perhaps the continuum should just provide a single object that
 * defines this entire interface, and which can be passed right
 * through?
 */
public class AgentContinuumManager {

    private RemoverIndex index;
    private BehaviorCell cell;
    private Supplier<Coordinate> locator;
    private Function<String, ContinuumAgentLinker> linkerLookup;

    public AgentContinuumManager(BehaviorCell cell,
                                 RemoverIndex index,
                                 Supplier<Coordinate> locator,
                                 Function<String, ContinuumAgentLinker> linkerLookup) {

        this.index = index;
        this.cell = cell;
        this.locator = locator;
        this.linkerLookup = linkerLookup;
    }

    public void schedule(Reaction reaction) {
        String id = reaction.getId();
        ContinuumAgentLinker linker = linkerLookup.apply(id);
        Supplier<RelationshipTuple> supplier = () -> getRelationshipTuple(reaction);
        linker.getNotifier().add(cell, supplier);
        index.add(() -> linker.getNotifier().remove(cell));
    }

    private RelationshipTuple getRelationshipTuple(Reaction reaction) {
        Coordinate c = locator.get();
        return new RelationshipTuple(c, reaction);
    }

    public void removeFromAll() {
        index.removeFromAll();
    }
}
