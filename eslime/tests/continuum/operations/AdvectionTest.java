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

import geometry.*;
import geometry.boundaries.Boundary;
import geometry.boundaries.PlaneRingReflecting;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import structural.identifiers.Coordinate;
import structural.identifiers.Flags;
import test.EslimeTestCase;

public class AdvectionTest extends EslimeTestCase {

    public void testLinear() {
        int w = 10;
        LinearMockGeometry geom = new LinearMockGeometry();
        geom.setWidth(w);

        Coordinate displacement = new Coordinate(1, 0, Flags.VECTOR);

        int size = w;

        Coordinate[] sites = new Coordinate[size];

        for (int i = 0; i < size; i++) {
            sites[i] = new Coordinate(i, 0, 0);
        }

        doTest(geom, displacement, sites, w);
    }

    public void testRectangular() {
        int width = 4;
        SquareMockGeometry geom = new SquareMockGeometry();
        geom.setWidth(width);
        Coordinate displacement = new Coordinate(1, 0, Flags.VECTOR);

        Coordinate[] sites = new Coordinate[16];
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                int i = (y * width) + x;
                sites[i] = new Coordinate(x, y, 0);
            }
        }

        doTest(geom, displacement, sites, width);
    }

    public void testTriangular() {
        int width = 4;
        TriangularMockGeometry geom = new TriangularMockGeometry();
        geom.setRadius(width);
        Coordinate displacement = new Coordinate(1, 0, 0, Flags.VECTOR);
        Coordinate[] sites = makeTriangularLattice(geom, width);
        doTest(geom, displacement, sites, width);

    }

    public void testCubic() {
        int w = 4;
        CubicMockGeometry geom = new CubicMockGeometry();
        geom.setWidth(w);
        Coordinate displacement = new Coordinate(1, 0, 0, Flags.VECTOR);
        Coordinate[] sites = makeCubicLattice(geom, w);
        doTest(geom, displacement, sites, w);
    }

    /**
     * Integration test--make sure that the operator does
     * the right thing when it encounters nontrivial edges.
     */
    public void testEdgeCase() {
        // Initialize a "real" geometry with a reflecting dimension.
        Lattice lattice = new RectangularLattice();
        Shape shape = new Rectangle(lattice, 4, 4);
        Boundary boundary = new PlaneRingReflecting(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);

        // Northward displacement
        Coordinate displacement = new Coordinate(0, 1, Flags.VECTOR);

        // With and without boundary conditions
        Operator with = new Advection(geom, true, displacement, 0.25);
        with.init();

        //Operator without = new Advection(geom, false, displacement, 0.25);
        //without.init();

        Coordinate o, p, q;
        int oi, pi, qi;

        // Look north from an interior coordinate
        o = new Coordinate(1, 1, 0);
        p = new Coordinate(1, 2, 0);

        oi = geom.coordToIndex(o);
        pi = geom.coordToIndex(p);

        assertEquals(0.75, with.get(oi, oi), epsilon);
        assertEquals(0.25, with.get(pi, oi), epsilon);

        //assertEquals(0.75, without.get(oi, oi), epsilon);
        //assertEquals(0.25, without.get(pi, oi), epsilon);

        // Look north from an exterior coordinate
        q = new Coordinate(1, 3, 0);
        qi = geom.coordToIndex(q);

        // With reflecting boundary conditions, advection back
        // to a boundary position will be negated. Without them,
        // the advected quantity will be absorbed at the boundary.
        assertEquals(1.0, with.get(qi, qi), epsilon);
        //assertEquals(0.75, without.get(qi, qi), epsilon);
    }

    private void doTest(MockGeometry geom, Coordinate displacement, Coordinate[] sites, int width) {
        int size = sites.length;
        geom.setCanonicalSites(sites);
        Operator mat = new Advection(geom, false, displacement, 0.25);
        mat.init();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i % width != 0 && i == j + 1) {
                    assertEquals(0.25, mat.get(i, j), epsilon);
                } else if (i == j) {
                    assertEquals(0.75, mat.get(i, j), epsilon);
                } else {
                    assertEquals(0.0, mat.get(i, j), epsilon);
                }
            }
        }
    }

    private Coordinate[] makeCubicLattice(MockGeometry geom, int width) {
        Coordinate[] sites = new Coordinate[64];

        int i = 0;
        for (int z = 0; z < width; z++) {
            for (int y = 0; y < width; y++) {
                for (int x = 0; x < width; x++) {
                    Coordinate idx = new Coordinate(x, y, z, 0);
                    sites[i] = idx;
                    i++;
                }
            }
        }
        geom.setCanonicalSites(sites);

        return sites;
    }

    private Coordinate[] makeTriangularLattice(MockGeometry geom, int width) {
        Coordinate[] sites = new Coordinate[16];

        int i = 0;
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {

                Coordinate idx = new Coordinate(x, y, 0);
                sites[i] = idx;

                i++;
            }
        }

        geom.setCanonicalSites(sites);

        return sites;
    }
}
