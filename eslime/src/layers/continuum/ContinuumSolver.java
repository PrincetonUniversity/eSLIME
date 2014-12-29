/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

/**
 * Created by dbborens on 12/12/14.
 */
public class ContinuumSolver {

    private ContinuumLayerContent content;
    private ScheduledOperations so;

    protected SteadyStateHelper helper;

    public ContinuumSolver(ContinuumLayerContent content, ScheduledOperations so) {
        this.content = content;
        this.so = so;

        helper = new SteadyStateHelper();
    }

    /**
     * Apply all scheduled operations, then reset the schedule.
     */
    public void solve() {
        Vector source = so.getSource();
        Matrix operator = so.getOperator();
        Vector template = content.getState().copy();

        Vector solution = helper.solve(source, operator, template);

        content.setState(solution);
        so.reset();
    }
}
