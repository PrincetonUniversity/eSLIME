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

package structural.postprocess;

import geometry.Geometry;
import no.uib.cipr.matrix.DenseVector;
import structural.Flags;
import structural.identifiers.Coordinate;

/**
 * Read-only wrapper for solutions to continuum processes.
 * If the requested coordinate is out of bounds, it returns 0D.
 * <p/>
 * Created by dbborens on 12/16/13.
 */
public class SolutionViewer {
    private final DenseVector solution;
    private final Geometry geometry;

    public SolutionViewer(DenseVector solution, Geometry geometry) {
        this.solution = solution;
        this.geometry = geometry;
    }

    public double getRelative(Coordinate offset) {
        if (!offset.hasFlag(Flags.VECTOR)) {
            throw new IllegalStateException("SolutionViewer::getRelative expects an offset, not an absolute coordinate.");
        }

        Coordinate converted = applyOffset(offset);

        //System.out.println("Looking up index of " + converted);
        Integer index = geometry.coordToIndex(converted);
        if (index == null) {
            return 0D;
        } else {
            return solution.get(index);
        }
    }

    public double getAbsolute(Coordinate coordinate) {
        if (coordinate.hasFlag(Flags.VECTOR)) {
            throw new IllegalStateException("getAbsolute expects an absolute coordinate, not an offset.");
        }

        Integer index = geometry.coordToIndex(coordinate);
        return solution.get(index);
    }

    private Coordinate applyOffset(Coordinate offset) {
        Coordinate center = geometry.getCenter();

        Coordinate result;

        int x = center.x() + offset.x();
        int y = center.y() + offset.y();

        if (center.hasFlag(Flags.PLANAR)) {
            result = new Coordinate(x, y, 0);
        } else {
            int z = center.z() + offset.z();
            result = new Coordinate(x, y, z, 0);
        }

        return result;
    }

    public DenseVector getSolution() {
        return solution;
    }
}
