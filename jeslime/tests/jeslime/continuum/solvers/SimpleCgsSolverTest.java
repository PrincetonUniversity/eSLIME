package jeslime.continuum.solvers;

import continuum.solvers.SimpleCgsSolver;
import jeslime.EslimeTestCase;

public class SimpleCgsSolverTest extends EquilibriumSolverTest {

    public void constructSolver() {
        solver = new SimpleCgsSolver(null, geometry);
    }
}
