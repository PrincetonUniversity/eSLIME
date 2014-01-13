package continuum.solvers;

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
        System.err.println("WARNING! Range scale not yet implemented.");
        System.err.println("WARNING! Coefficient scaling not yet implemented.");
        SimpleCgsSolver solver = new SimpleCgsSolver(geometry);
        return solver;
    }
}
