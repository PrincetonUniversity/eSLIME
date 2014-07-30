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

package structural.utilities;

import no.uib.cipr.matrix.BandMatrix;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

public abstract class MatrixUtils {
    /**
     * Returns a matrix in a tab-separated matrix form.
     *
     * @return
     */
    public static String matrixForm(Matrix m) {
        StringBuilder sb = new StringBuilder();

        int r = m.numRows();
        int c = m.numColumns();
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                sb.append(m.get(i, j));
                sb.append('\t');
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    public static Matrix vectorToMatrix(Vector v, int l) {
        Matrix matrix = new DenseMatrix(v.size() / l, l);
        for (int q = 0; q < v.size(); q++) {
            int i = q / l;
            int j = q % l;

            Double value = v.get(q);
            matrix.set(i, j, value);
        }

        return matrix;
    }

    /**
     * Returns an identity matrix of size n.
     * <p/>
     * TODO: There must be a built-in for this.
     */
    public static Matrix I(int n) {
        Matrix m = new BandMatrix(n, 0, 0);
        for (int i = 0; i < n; i++)
            m.set(i, i, 1d);

        return m;
    }

    /**
     * Compares two matrices for numerical equality.
     */
    public static boolean equal(Matrix p, Matrix q) {
        double epsilon = EpsilonUtil.epsilon();
        if (p.numColumns() != q.numColumns()) {
            return false;
        }

        if (p.numRows() != q.numRows()) {
            return false;
        }

        for (int i = 0; i < p.numRows(); i++) {
            for (int j = 0; j < p.numColumns(); j++) {
                double delta = p.get(i, j) - q.get(i, j);
                double magnitude = Math.abs(delta);

                if (magnitude > epsilon) {
                    return false;
                }
            }
        }

        return true;
    }
}
