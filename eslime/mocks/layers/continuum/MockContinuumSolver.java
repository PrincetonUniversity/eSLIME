/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

/**
 * Created by dbborens on 12/28/14.
 */
public class MockContinuumSolver extends ContinuumSolver {

    private boolean solved;

    public MockContinuumSolver() {
        super(null, null);
        solved = false;
    }

    @Override
    public void solve() {
        solved = true;
    }

    public boolean isSolved() {
        return solved;
    }
}
