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
    }

    @Override
    public CellLayerContent makeQuery() {
        return new InfiniteCellLayerContent(geom, indices);
    }
}
