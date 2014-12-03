/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.solute;

import continuum.solvers.Solver;
import geometry.Geometry;
import layers.Layer;
import layers.LayerManager;
import no.uib.cipr.matrix.DenseVector;
import structural.postprocess.SolutionViewer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * This entire subsystem probably deserves a bit of a rethink.
 *
 */
public abstract class SoluteLayer extends Layer {

    // Current model state
    protected SolutionViewer state;

    protected String id;

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

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public void reset() {
        throw new NotImplementedException();
    }
}
