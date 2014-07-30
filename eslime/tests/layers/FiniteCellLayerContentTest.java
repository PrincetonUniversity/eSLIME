/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers;

import control.identifiers.Coordinate;
import layers.cell.CellLayerContent;
import layers.cell.FiniteCellLayerContent;

/**
 * Created by David B Borenstein on 4/10/14.
 */
public class FiniteCellLayerContentTest extends CellLayerContentTest {

    public void testImaginaryBehavior() {

        boolean thrown = false;
        try {
            query.sanityCheck(new Coordinate(-1, 0, 0));
        } catch (Exception ex) {
            thrown = true;
        }

        assertTrue(thrown);

        assertNotNull(query.getImaginarySites());
        assertEquals(0, query.getImaginarySites().size());
    }

    @Override
    public CellLayerContent makeQuery() {
        return new FiniteCellLayerContent(geom, indices);
    }
}
