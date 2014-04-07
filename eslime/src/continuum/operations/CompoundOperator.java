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

package continuum.operations;

import geometry.Geometry;
import io.loader.OperatorLoader;
import org.dom4j.Element;
import structural.utilities.EpsilonUtil;

public class CompoundOperator extends Operator {

    private Operator[] children;

    public CompoundOperator(Geometry geom, boolean useBoundaries, Element root) {
        super(geom, useBoundaries);

        // Load child operator list.
        OperatorLoader childLoader = new OperatorLoader(geom, useBoundaries);
        children = childLoader.getOperators(root);
    }

    public CompoundOperator(Geometry geom, boolean useBoundaries, Operator[] children) {
        super(geom, useBoundaries);
        this.children = children;
    }

    @Override
    public void init() {
        // Iterate over child operators.
        for (Operator child : children) {
            loadChild(child);
        }
    }

    private void loadChild(Operator child) {

        // Verify child dimension
        int n = dim(child);

        // Look at every site in child operator matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Get the value of the child matrix at every entry.
                double du = child.get(i, j);

                // If we're not actually adding anything, don't risk allocating
                // storage that we don't need by calling set(...) on the matrix
                // entry.

                if (Math.abs(du) <= EpsilonUtil.epsilon()) {
                    continue;
                }

                // Otherwise, augment the compound operator by the value of the
                // child operator at every entry.
                double u = get(i, j);
                set(i, j, u + du);
            }
        }


    }

    private int dim(Operator child) {
        if (child.numRows() != numRows) {
            throw new IllegalStateException("Error loading compound operator.");
        }

        if (child.numColumns() != numColumns) {
            throw new IllegalStateException("Error loading compound operator.");
        }

        if (!child.isSquare()) {
            throw new IllegalStateException("Non-square operator matrix.");
        }

        int l = child.numRows();
        return l;
    }

}
