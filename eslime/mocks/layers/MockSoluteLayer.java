/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

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
