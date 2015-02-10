/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.boundaries;

import control.identifiers.Coordinate;
import control.identifiers.Flags;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.shape.Hexagon;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import org.junit.Before;
import org.junit.Test;
import test.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Integration test of the TetrisBoundary. (Setting this
 * up as a true mock would be needlessly difficult.)
 */
public class TetrisBoundaryTest extends TestBase {

    private Shape shape;
    private Lattice lattice;
    private TetrisBoundary query;

    @Before
    public void init() {
        lattice = new RectangularLattice();
        shape = new Rectangle(lattice, 2, 2);
        query = new TetrisBoundary(shape, lattice);
    }

    @Test(expected = IllegalArgumentException.class)
    public void d1Throws() {
        Shape shape = mock(Shape.class);
        Lattice lattice = mock(Lattice.class);
        when(lattice.getDimensionality()).thenReturn(1);
        new TetrisBoundary(shape, lattice);
    }

    @Test(expected = IllegalArgumentException.class)
    public void d3Throws() {
        Shape shape = mock(Shape.class);
        Lattice lattice = mock(Lattice.class);
        when(lattice.getDimensionality()).thenReturn(3);
        new TetrisBoundary(shape, lattice);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nonRectangleThrows() {
        Shape shape = mock(Hexagon.class);
        Lattice lattice = mock(Lattice.class);
        when(lattice.getDimensionality()).thenReturn(2);
        new TetrisBoundary(shape, lattice);
    }

    @Test
    public void rightOverboundWraps() {
        Coordinate input = new Coordinate(2, 0, 0);
        Coordinate expected = new Coordinate(0, 0, Flags.BOUNDARY_APPLIED);
        doTest(input, expected);
    }

    @Test
    public void leftOverboundWraps() {
        Coordinate input = new Coordinate(-1, 0, 0);
        Coordinate expected = new Coordinate(1, 0, Flags.BOUNDARY_APPLIED);
        doTest(input, expected);
    }

    @Test
    public void bottomOverboundIsNull() {
        Coordinate input = new Coordinate(0, -1, 0);
        doNullTest(input);
    }

    @Test
    public void topOverboundEndOfWorld() {
        Coordinate input = new Coordinate(0, 2, 0);
        Coordinate expected = new Coordinate(0, 2, Flags.BOUNDARY_APPLIED | Flags.END_OF_WORLD);
        doTest(input, expected);
    }

    @Test
    public void lowerLeftIsNull() {
        Coordinate input = new Coordinate(-1, -1, 0);
        doNullTest(input);
    }

    @Test
    public void lowerRightIsNull() {
        Coordinate input = new Coordinate(2, -1, 0);
        doNullTest(input);
    }

    @Test
    public void upperLeftWrappedAndEndOfWorld() {
        Coordinate input = new Coordinate(-1, 2, 0);
        Coordinate expected = new Coordinate(1, 2, Flags.BOUNDARY_APPLIED | Flags.END_OF_WORLD);
        doTest(input, expected);
    }

    @Test
    public void upperRightWrappedAndEndOfWorld() {
        Coordinate input = new Coordinate(2, 2, 0);
        Coordinate expected = new Coordinate(0, 2, Flags.BOUNDARY_APPLIED | Flags.END_OF_WORLD);
        doTest(input, expected);
    }

    @Test
    public void inBoundsDoesNothing() {
        Coordinate input = new Coordinate(0, 0, 0);
        Coordinate expected = new Coordinate(0, 0, 0);
        doTest(input, expected);
    }

    private void doTest(Coordinate input, Coordinate expected) {
        Coordinate actual = query.apply(input);
        assertEquals(expected, actual);
    }

    private void doNullTest(Coordinate input) {
        Coordinate actual = query.apply(input);
        assertNull(actual);
    }
}