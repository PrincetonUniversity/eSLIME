/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.cell;

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

    private class ExposedCellLayer extends CellLayer {

        public ExposedCellLayer(Geometry geom) {
            super(geom);
        }

        public CellLayerContent getContent() {
            return content;
        }
    }
}
