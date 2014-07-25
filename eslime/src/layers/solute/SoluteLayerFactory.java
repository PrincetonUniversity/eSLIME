/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package layers.solute;

import continuum.solvers.EquilibriumSolver;
import factory.continuum.solvers.SolverFactory;
import geometry.Geometry;
import factory.geometry.GeometryFactory;
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
    private static final String[] EQUILIBRIUM_SOLVERS = new String[]{
            "simplecgs",
            "null"
    };

    private static final String[] FINITE_TIME_SOLVERS = new String[]{
            // No finite time solvers yet
    };

    public static SoluteLayer instantiate(Element layerRoot, GeometryFactory geometryFactory, LayerManager layerManager) {
        String layerClass = layerRoot.element("class").getTextTrim();
        Geometry geometry = geometryFactory.make(layerRoot);
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
