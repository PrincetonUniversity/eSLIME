/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package layers.solute;

import continuum.solvers.EquilibriumSolver;
import continuum.solvers.Solver;
import geometry.Geometry;
import layers.LayerManager;
import no.uib.cipr.matrix.Vector;
import sun.tools.jar.resources.jar_de;

/**
 * Created by dbborens on 1/6/14.
 */
public class EquilibriumSoluteLayer extends SoluteLayer {
    private EquilibriumSolver solver;

    public EquilibriumSoluteLayer(Geometry geom, LayerManager manager, EquilibriumSolver solver, String id) {
        super(geom, manager, solver, id);
    }

    @Override
    public void setDt(double dt) {
        throw new UnsupportedOperationException();
    }

    public void integrate() {
        // Check sources.
        state = solver.solve(source);
    }
}
