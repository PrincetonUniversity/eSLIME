/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.cell;

import cells.MockCell;
import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.MockGeometry;
import geometry.boundaries.HaltBoundary;
import test.EslimeTestCase;

/**
 * Created by dbborens on 4/25/14.
 */
public class CellLayerTest extends EslimeTestCase {
    private MockGeometry geom;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        geom = buildMockGeometry();
    }

    public void testClone() {
        CellLayer query = new CellLayer(geom);
        CellLayer clone = query.clone();
        assertEquals(query, clone);
        assertFalse(query == clone);
    }

    public void testHaltBoundaryCase() {
        Class[] componentClasses = new Class[] {
                Object.class,
                Object.class,
                HaltBoundary.class
        };

        geom.setComponentClasses(componentClasses);
        ExposedCellLayer query = new ExposedCellLayer(geom);

        assertEquals(HaltCellLayerContent.class, query.getContent().getClass());
    }

    public void testFiniteBoundaryCase() {
        geom.setInfinite(false);
        ExposedCellLayer query = new ExposedCellLayer(geom);
        assertEquals(FiniteCellLayerContent.class, query.getContent().getClass());
    }

    public void testInfiniteBoundaryCase() {
        geom.setInfinite(true);
        ExposedCellLayer query = new ExposedCellLayer(geom);
        assertEquals(InfiniteCellLayerContent.class, query.getContent().getClass());
    }

    public void testReset() throws Exception {
        // Put some stuff on the lattice.
        ExposedCellLayer query = new ExposedCellLayer(geom);
        for (int i = 0; i < geom.getCanonicalSites().length; i++) {
            Coordinate c = geom.getCanonicalSites()[i];
            query.getUpdateManager().place(new MockCell(i), c);
        }

        // Verify that the lattice is filled up.
        assertEquals(geom.getCanonicalSites().length, query.getViewer().getOccupiedSites().size());

        // Reset the lattice.
        query.reset();

        // Make sure that the lattice is reset to square one.
        assertEquals(0, query.getViewer().getOccupiedSites().size());
        for (Coordinate c : geom.getCanonicalSites()) {
            assertFalse(query.getViewer().isOccupied(c));
        }
    }

    private class ExposedCellLayer extends CellLayer {

        public ExposedCellLayer(Geometry geom) {
            super(geom);
        }

        public CellLayerContent getContent() {
            return content;
        }
    }
}
