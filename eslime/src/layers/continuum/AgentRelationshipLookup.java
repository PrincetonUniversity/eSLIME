/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import cells.BehaviorCell;

/**
 * Created by dbborens on 12/30/14.
 */
public interface AgentRelationshipLookup {
    public RelationshipTuple go(BehaviorCell cell);
}
