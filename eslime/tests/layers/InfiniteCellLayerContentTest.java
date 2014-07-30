/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers;

import control.identifiers.Coordinate;
import layers.cell.CellLayerContent;
import layers.cell.InfiniteCellLayerContent;

import java.util.Set;

/**
 * Created by David B Borenstein on 4/10/14.
 */
public class InfiniteCellLayerContentTest extends CellLayerContentTest {
    public void testSanityCheck() {
        boolean thrown = false;
        try {
            query.sanityCheck(new Coordinate(-1, 0, 0));
        } catch (Exception ex) {
            thrown = true;
        }

        assertFalse(thrown);
    }

    public void testGetImaginarySites() {
        Coordinate imaginary = new Coordinate(-1, 0, 0);
        Coordinate real = new Coordinate(0, 0, 0);

        indices.setOccupied(imaginary, true);
        indices.setOccupied(real, true);

        Set<Coordinate> result = query.getImaginarySites();

        assertEquals(1, result.size());
        assertTrue(result.contains(imaginary));
        assertFalse(result.contains(real));
    }

    @Override
    public CellLayerContent makeQuery() {
        return new InfiniteCellLayerContent(geom, indices);
    }
}
