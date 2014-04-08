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

import continuum.solvers.Solver;
import control.identifiers.Coordinate;
import geometry.Geometry;
import layers.Layer;
import layers.LayerManager;
import no.uib.cipr.matrix.DenseVector;
import structural.postprocess.SolutionViewer;

import java.util.HashMap;

public abstract class SoluteLayer extends Layer {

    // Current model state
    protected SolutionViewer state;

    protected String id;

    protected HashMap<Coordinate, Integer> coordToIndex;

    protected LayerManager manager;

    protected Solver solver;

    protected DenseVector source;

    @Override
    public String getId() {
        return id;
    }

    /**
     * Constructor for normal use.
     */
    public SoluteLayer(Geometry geom, LayerManager manager, Solver solver, String id) {
        geometry = geom;
        this.id = id;

        this.manager = manager;

        this.solver = solver;
    }

    public void push(SolutionViewer state) {
        this.state = state;
    }

    public SolutionViewer getState() {
        return state;
    }

    public Geometry getGeom() {
        return geometry;
    }

    public HashMap<Coordinate, Integer> getCoordToIndex() {
        return coordToIndex;
    }

    public Solver getSolver() {
        return solver;
    }

    public void setSource(DenseVector source) {
        this.source = source;
    }

    public abstract void setDt(double dt);

    /**
     * Should integrate forward using the specified operator, source, and (if
     * applicable) dt and store the result to the state vector.
     */
    public abstract void integrate();

}
