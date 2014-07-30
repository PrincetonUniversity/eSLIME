/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.solute;

import control.identifiers.Coordinate;
import geometry.Geometry;
import layers.LayerManager;
import no.uib.cipr.matrix.DenseVector;
import structural.postprocess.SolutionViewer;
import structural.utilities.EpsilonUtil;

/**
 * Contains an explicitly specified solute state, which can be directly set
 * on a per-coordinate basis.
 *
 * Created by dbborens on 7/24/14.
 */
public class LightweightSoluteLayer extends SoluteLayer {

    private DenseVector valueVector;

    /**
     * Constructor for normal use.
     *
     * @param geom
     * @param manager
     * @param id
     */
    public LightweightSoluteLayer(Geometry geom, LayerManager manager, String id) {
        super(geom, manager, null, id);
        valueVector = new DenseVector(geom.getCanonicalSites().length);
    }

    @Override
    public void setDt(double dt) {
    }

    @Override
    public void integrate() {
    }

    public void set(Coordinate coord, double value) {
        if (!geometry.contains(coord)) {
            throw new IllegalStateException("Invalid coordinate " + coord);
        }

        int index = geometry.coordToIndex(coord);

        valueVector.set(index, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LightweightSoluteLayer)) {
            return false;
        }

        LightweightSoluteLayer other = (LightweightSoluteLayer) obj;

        if (valueVector.getData().length != other.valueVector.getData().length) {
            return false;
        }

        for (int i = 0; i < valueVector.getData().length; i++) {
            double p = valueVector.get(i);
            double q = other.valueVector.get(i);
            if (!EpsilonUtil.epsilonEquals(p, q)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public SolutionViewer getState() {
        SolutionViewer ret = new SolutionViewer(valueVector, geometry);
        return ret;
    }
}
