/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.boundaries;//import junit.framework.TestCase;

import geometry.boundary.ArenaTest;
import geometry.lattice.Lattice;
import geometry.shape.Shape;

public class HaltArenaTest extends ArenaTest {
    @Override
    protected Boundary makeBoundary(Shape shape, Lattice lattice) {
        return new HaltArena(shape, lattice);
    }
}