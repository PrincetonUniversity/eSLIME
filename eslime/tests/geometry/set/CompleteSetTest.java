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

package geometry.set;//import junit.framework.TestCase;

import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.Arena;
import geometry.boundaries.Boundary;
import geometry.lattice.*;
import geometry.shape.*;
import test.EslimeTestCase;

public class CompleteSetTest extends EslimeTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void test1D() {
        Lattice lattice = new LinearLattice();
        Shape shape = new Line(lattice, 4);
        Boundary boundary = new Arena(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        CustomSet expected = new CustomSet();
        expected.add(new Coordinate(0, 0, 0));
        expected.add(new Coordinate(0, 1, 0));
        expected.add(new Coordinate(0, 2, 0));
        expected.add(new Coordinate(0, 3, 0));
        CompleteSet actual = new CompleteSet(geom);
        assertEquals(expected, actual);
    }

    public void test2DTri() {
        Lattice lattice = new TriangularLattice();
        Shape shape = new Hexagon(lattice, 1);
        Boundary boundary = new Arena(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        CompleteSet actual = new CompleteSet(geom);
        CustomSet expected = new CustomSet();
        expected.add(new Coordinate(0, 0, 0));
        expected.add(new Coordinate(1, 0 ,0));
        expected.add(new Coordinate(2, 1, 0));
        expected.add(new Coordinate(2, 2, 0));
        expected.add(new Coordinate(1, 2, 0));
        expected.add(new Coordinate(0, 1, 0));
        expected.add(new Coordinate(1, 1, 0));
        assertEquals(expected, actual);
    }

    public void test2DRec() {
        Lattice lattice = new RectangularLattice();
        Shape shape = new Rectangle(lattice, 2, 2);
        Boundary boundary = new Arena(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        CustomSet expected = new CustomSet();
        expected.add(new Coordinate(0, 0, 0));
        expected.add(new Coordinate(0, 1, 0));
        expected.add(new Coordinate(1, 0, 0));
        expected.add(new Coordinate(1, 1, 0));
        CompleteSet actual = new CompleteSet(geom);
        assertEquals(expected, actual);
    }

    public void test3D() {
        Lattice lattice = new CubicLattice();
        Shape shape = new Cuboid(lattice, 2, 2, 2);
        Boundary boundary = new Arena(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        CustomSet expected = new CustomSet();
        expected.add(new Coordinate(0, 0, 0, 0));
        expected.add(new Coordinate(0, 0, 1, 0));
        expected.add(new Coordinate(0, 1, 0, 0));
        expected.add(new Coordinate(0, 1, 1, 0));
        expected.add(new Coordinate(1, 0, 0, 0));
        expected.add(new Coordinate(1, 0, 1, 0));
        expected.add(new Coordinate(1, 1, 0, 0));
        expected.add(new Coordinate(1, 1, 1, 0));
        CompleteSet actual = new CompleteSet(geom);
        assertEquals(expected, actual);
    }
}