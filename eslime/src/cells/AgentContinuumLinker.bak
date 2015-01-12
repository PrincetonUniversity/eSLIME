/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package cells;

import layers.continuum.RelationshipTuple;

import java.util.function.Function;

/**
 * Created by dbborens on 12/31/14.
 */
public class AgentContinuumLinker {

    private Function<String, RelationshipTuple> injLookup;
    private Function<String, RelationshipTuple> expLookup;

    public AgentContinuumLinker(Function<String, RelationshipTuple> injLookup,
                                Function<String, RelationshipTuple> expLookup) {

        this.injLookup = injLookup;
        this.expLookup = expLookup;
    }

    public RelationshipTuple getInj(String id) {
        return injLookup.apply(id);
    }

    public RelationshipTuple getExp(String id) {
        return expLookup.apply(id);
    }
}
