/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

/**
 * Created by dbborens on 12/28/14.
 */
public class MockSteadyStateHelper extends SteadyStateHelper {

    private Vector source;
    private Matrix operator;
    private Vector initial;
    private Vector solution;

    private boolean solved = false;

    @Override
    public Vector solve(Vector source, Matrix operator, Vector initial) {
        solved = true;

        this.source = source;
        this.operator = operator;
        this.initial = initial;

        return solution;
    }

    public void setSolution(Vector solution) {
        this.solution = solution;
    }

    public Vector getSource() {
        return source;
    }

    public Matrix getOperator() {
        return operator;
    }

    public Vector getInitial() {
        return initial;
    }

    public boolean isSolved() {
        return solved;
    }
}
