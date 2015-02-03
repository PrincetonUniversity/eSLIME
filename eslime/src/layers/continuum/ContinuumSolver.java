/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import layers.continuum.solve.SteadyState;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;
import structural.utilities.MatrixUtils;

/**
 * Created by dbborens on 12/12/14.
 */
public class ContinuumSolver {

    private ContinuumLayerContent content;
    private ScheduledOperations so;
    private SteadyState steadyState;

    public ContinuumSolver(ContinuumLayerContent content, ScheduledOperations so, SteadyState steadyState) {
        this.content = content;
        this.so = so;
        this.steadyState = steadyState;
    }

    /**
     * Apply all scheduled operations, then reset the schedule.
     */
    public void solve() {
        Vector source = so.getSource();
        Matrix operator = so.getOperator();

        Vector template = content.getState().copy();

        Vector solution = steadyState.solve(source, operator, template);

        content.setState(solution);
        System.out.println(MatrixUtils.asMatrix(solution, 32));
        so.reset();
    }

}
