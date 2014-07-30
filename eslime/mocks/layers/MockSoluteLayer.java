/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers;

import continuum.solvers.Solver;
import geometry.Geometry;
import layers.solute.SoluteLayer;
import no.uib.cipr.matrix.DenseVector;
import structural.postprocess.SolutionViewer;

/**
 * Created by David B Borenstein on 1/7/14.
 */
public class MockSoluteLayer extends SoluteLayer {
    public MockSoluteLayer() {
        super(null, null, null, null);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    @Override
    public Solver getSolver() {
        return null;
    }

    @Override
    public void setDt(double dt) {

    }

    public boolean integrateWasFired() {
        return integrateWasFired;
    }

    boolean integrateWasFired = false;

    @Override
    public void integrate() {
        integrateWasFired = true;
    }

    public DenseVector getSource() {
        return source;
    }

    DenseVector source;

    @Override
    public void setSource(DenseVector source) {
        this.source = source;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    private Geometry geometry;

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof MockSoluteLayer);
    }

    @Override
    public void push(SolutionViewer state) {
        super.push(state);
    }
}
