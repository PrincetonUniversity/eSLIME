/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.continuum.solvers;

import continuum.solvers.SimpleCgsSolver;
import continuum.solvers.Solver;
import geometry.Geometry;
import org.dom4j.Element;

/**
 * Created by dbborens on 1/6/14.
 */
public abstract class SolverFactory {
    public static Solver instantiate(Element solverRoot, Geometry geometry) {
        String solverClass = solverRoot.element("class").getTextTrim();

        if (solverClass.equalsIgnoreCase("SimpleCGS")) {
            return simpleCGS(solverRoot, geometry);
            // Null case for testing
        } else if (solverClass.equalsIgnoreCase("null")) {
            return null;
        } else {
            throw new IllegalArgumentException("Unrecognized solver class '" + solverClass + "'");
        }
    }

    private static Solver simpleCGS(Element solverRoot, Geometry geometry) {
        // Range scale allows the solver to work on a number of sites
        // that goes beyond the boundaries of the lattice by some multiple.
        // This is useful for emulating infinite boundary conditions--the
        // longer the range, the weaker the boundary effects.
        Geometry finalGeometry = getFinalGeometry(solverRoot, geometry);
        System.err.println("WARNING! Coefficient scaling not yet implemented.");
        SimpleCgsSolver solver = new SimpleCgsSolver(finalGeometry);
        return solver;
    }

    protected static Geometry getFinalGeometry(Element solverRoot, Geometry geometry) {
        Geometry finalGeometry;
        if (solverRoot.element("range-scale") == null) {
            finalGeometry = geometry;
        } else {
            double rangeScale = Double.valueOf(solverRoot.element("range-scale").getTextTrim());
            Geometry scaledGeometry = geometry.cloneAtScale(rangeScale);
            finalGeometry = scaledGeometry;
        }
        return finalGeometry;
    }
}
