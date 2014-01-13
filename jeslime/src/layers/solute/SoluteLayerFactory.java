package layers.solute;

import continuum.solvers.EquilibriumSolver;
import continuum.solvers.Solver;
import continuum.solvers.SolverFactory;
import geometry.Geometry;
import io.project.GeometryManager;
import layers.LayerManager;
import org.dom4j.Element;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by dbborens on 1/6/14.
 */
public abstract class SoluteLayerFactory {

   // As more solvers are written, enumerate the ones that will be
   // allowed for equilibrium solute layers here. Note that these
   // must be in lower case.
   private static final String[] EQUILIBRIUM_SOLVERS = new String[] {
        "simplecgs",
        "null"
   };

   private static final String[] FINITE_TIME_SOLVERS = new String[] {
        // No finite time solvers yet
   };

   public static SoluteLayer instantiate(Element layerRoot, GeometryManager geometryManager, LayerManager layerManager) {
       String layerClass = layerRoot.element("class").getTextTrim();
       Geometry geometry = geometryManager.make(layerRoot);
       if (layerClass.equalsIgnoreCase("equilibrium")) {
           return equilibriumLayer(layerRoot, geometry, layerManager);
       } else if (layerClass.equalsIgnoreCase("integration")) {
           throw new UnsupportedOperationException("Finite time integration not yet implemented.");
       } else {
           throw new IllegalArgumentException("Unrecognized solute layer class '" + layerClass + "'");
       }
   }

    private static EquilibriumSoluteLayer equilibriumLayer(Element layerRoot, Geometry geometry, LayerManager layerManager) {
        Element solverRoot = layerRoot.element("solver");
        String id = layerRoot.element("id").getTextTrim();
        validateSolver(solverRoot, EQUILIBRIUM_SOLVERS);
        
        
        EquilibriumSolver solver = (EquilibriumSolver) SolverFactory.instantiate(solverRoot, geometry);

        EquilibriumSoluteLayer layer = new EquilibriumSoluteLayer(geometry, layerManager, solver, id);

        return layer;
    }

    private static void validateSolver(Element solverRoot, String[] allowedSolversArr) {
        HashSet<String> allowedSolvers = new HashSet<String>(Arrays.asList(allowedSolversArr));
        Element classElement = solverRoot.element("class");
        String solverClass = classElement.getTextTrim().toLowerCase();
        if (!allowedSolvers.contains(solverClass)) {
            throw new IllegalArgumentException("Solver class '" + solverClass + "' unrecognized or not permitted in equilibrium solute layers.");
        }
    }
}
