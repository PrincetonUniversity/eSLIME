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

package processes.discrete;

import cells.Cell;
import cells.MockCell;
import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.*;
import geometry.lattice.CubicLattice;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.shape.Cuboid;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import junit.framework.TestCase;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import structural.MockRandom;
import test.EslimeTestCase;

import java.util.HashSet;
import java.util.Random;

public class ShoveHelperTest extends EslimeTestCase {

    private CellLayer layer;
    private ShoveHelper query;
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Create a 10x1 rectangular, 2D geometry
        Lattice lattice = new RectangularLattice();
        Shape shape = new Rectangle(lattice, 10, 1);
        Boundary boundary = new Periodic(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        MockLayerManager lm = new MockLayerManager();
        layer = new CellLayer(geom);
        lm.setCellLayer(layer);
        placeCells();

        Random random = new Random(RANDOM_SEED);

        query = new ShoveHelper(lm, random);
    }

    /**
     * This test will create a linear geometry that has one vacancy. A line
     * of cells will be pushed toward this vacancy, moving the vacancy to the
     * origin of the shove. This does not test all of the behavior of this
     * process, but it's a start.
     *
     * So here is what is supposed to happen:
     *
     *   0123456_89  Initial condition
     *       ^       (Cell to be shoved)
     *
     *   0123_45689  Result
     */
    public void test1Dshove() throws Exception {
        Coordinate target = new Coordinate(7, 0, 0);
        Coordinate origin = new Coordinate(4, 0, 0);
        query.shove(origin, target);

        int[] leftSeq = new int[] {0, 1, 2, 3};
        int[] rightSeq = new int[] {4, 5, 6, 8, 9};

        for (int x = 0; x < 4; x++) {
            Coordinate c = new Coordinate(x, 0, 0);
            Cell observed = layer.getViewer().getCell(c);
            int expected = leftSeq[x];
            int actual = observed.getState();
            assertEquals(expected, actual);
        }

        for (int x = 0; x < 5; x++) {
            Coordinate c = new Coordinate(x + 5, 0, 0);
            Cell observed = layer.getViewer().getCell(c);
            int expected = rightSeq[x];
            int actual = observed.getState();
            assertEquals(expected, actual);
        }
    }

    public void test3Dshove() throws Exception {
        // Create a 10x1 rectangular, 2D geometry
        Lattice lattice = new CubicLattice();
        Shape shape = new Cuboid(lattice, 5, 5, 5);
        Boundary boundary = new Absorbing(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        MockLayerManager lm = new MockLayerManager();
        layer = new CellLayer(geom);
        lm.setCellLayer(layer);
        Random random = new MockRandom();
        query = new ShoveHelper(lm, random);

        for (int x = 0 ; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 4; z++) {
                    Cell cell = new MockCell(1);
                    Coordinate coord = new Coordinate(x, y, z, 0);
                    layer.getUpdateManager().place(cell, coord);
                }
            }
        }

        Coordinate origin = new Coordinate(1, 2, 3, 0);
        Coordinate target = new Coordinate(4, 4, 4, 0);

        HashSet<Coordinate> affected = query.shove(origin, target);

        // Algorithm will prefer z, then y, then x. So the shoving should
        // progress like this:
        assertTrue(affected.contains(new Coordinate(1, 2, 4, 0)));
        assertTrue(affected.contains(new Coordinate(1, 3, 4, 0)));
        assertTrue(affected.contains(new Coordinate(1, 4, 4, 0)));
        assertTrue(affected.contains(new Coordinate(2, 4, 4, 0)));
        assertTrue(affected.contains(new Coordinate(3, 4, 4, 0)));
        assertTrue(affected.contains(new Coordinate(4, 4, 4, 0)));

        // Having shoved, the origin should now be vacant.
        assertFalse(layer.getViewer().isOccupied(origin));
    }

    public void testGetTarget() throws Exception {
        Coordinate origin = new Coordinate(4, 0, 0);

        Coordinate expected = new Coordinate(7, 0, 0);
        Coordinate actual = query.getTarget(origin);
        assertEquals(expected, actual);
    }

    public void testRemoveImaginary() throws Exception {
        Lattice lattice = new RectangularLattice();
        Shape shape = new Rectangle(lattice, 10, 1);
        Boundary boundary = new Arena(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        MockLayerManager lm = new MockLayerManager();
        layer = new CellLayer(geom);
        lm.setCellLayer(layer);
        placeCells();
        Random random = new Random(RANDOM_SEED);
        query = new ShoveHelper(lm, random);
        MockCell cell = new MockCell(1);
        layer.getUpdateManager().place(cell, new Coordinate(-1, 0, 0));
        assertEquals(1, layer.getViewer().getImaginarySites().size());
        query.removeImaginary();
        assertEquals(0, layer.getViewer().getImaginarySites().size());
    }

    private void placeCells() {
        for (int x = 0; x < 7; x++) {
            placeNumberedCell(x);
        }

        for (int x = 8; x <= 9; x++) {
            placeNumberedCell(x);
        }
    }

    private void placeNumberedCell(int x) {
        MockCell cell = new MockCell(x);
        Coordinate coord = new Coordinate(x, 0, 0);
        layer.getUpdateManager().place(cell, coord);
    }

}