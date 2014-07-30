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

package geometry.boundaries;

import control.identifiers.Coordinate;
import geometry.boundaries.helpers.WrapHelper;
import geometry.boundary.MockWrapHelper;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import junit.framework.TestCase;

public class PeriodicTest extends TestCase {

    private MockWrapHelper helper;
    private ExposedPeriodic query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        helper = new MockWrapHelper();
        Lattice lattice = new RectangularLattice();
        Shape shape = new Rectangle(lattice, 0, 0);
        query = new ExposedPeriodic(shape, lattice);
        query.setHelper(helper);
    }

    public void testApply() throws Exception {
        Coordinate c = new Coordinate(0, 0, 0);
        query.apply(c);
        assertTrue(helper.isAllWrapped());
    }

    public void testIsInfinite() throws Exception {
        assertFalse(query.isInfinite());
    }

    private class ExposedPeriodic extends Periodic {
        public ExposedPeriodic(Shape shape, Lattice lattice) {
            super(shape, lattice);
        }

        public void setHelper(WrapHelper helper) {
            this.helper = helper;
        }
    }
}