/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.continuum;

import java.util.function.Supplier;

/**
 * The diffusion constant assumes a continuous space. Since both space and
 * time are discretized in Nanoverse, it is necessary to adjust the
 * diffusion constant.
 *
 * In general,
 *
 *              n      dt
 *     a = D * --- * ------
 *              m    (dx)^2
 *
 * Where "m" is the connectivity of the graph and "n" is the dimensionality
 * of the graph. We assume that dt and dx are both equal to unity, so the
 * DiffusionConstantHelper need only concern iself with D, n and m.
 *
 * Created by dbborens on 1/24/15.
 */
public class DiffusionConstantHelper {

    private double neighborValue;
    private double diagonalValue;

    public DiffusionConstantHelper(double baseConstant, int connectivity, int dimensionality) {
        if (baseConstant < 0.0) {
            throw new IllegalArgumentException("Illegal diffusion constant: Negative diffusion constant not permitted.");
        }

        neighborValue = baseConstant * ((double) dimensionality / (double) connectivity);
        diagonalValue = -1.0 * baseConstant * Math.pow(dimensionality, 2.0);

        if (diagonalValue < -1.0) {
            throw new IllegalArgumentException("Illegal diffusion constant: Total flux exceeds 100%.");
        }
    }

    /**
     * Return the fraction of the solute to be transferred to
     * each neighbor per dt.
     *
     * @return
     */
    public double getNeighborValue() {
        return neighborValue;
    }

    /**
     * Return the fraction of the solute to be retained at
     * the self-coordinate (diagonal) per dt.
     */
    public double getDiagonalValue() {
        return diagonalValue;
    }
}
