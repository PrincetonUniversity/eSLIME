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

package io.visual.color;

import cells.MockCell;
import control.identifiers.Coordinate;
import geometry.MockGeometry;
import junit.framework.TestCase;
import layers.MockLayerManager;
import layers.MockSystemState;
import layers.cell.CellLayer;

import java.awt.*;

/**
 * Created by dbborens on 4/2/14.
 */
public class DefaultColorManagerTest extends TestCase {
    private MockSystemState systemState;
    private Coordinate coord;
    private ColorManager query;
    private CellLayer layer;
    @Override
    protected void setUp() throws Exception {
        systemState = new MockSystemState();
        coord = new Coordinate(0, 0, 0);
        query = new DefaultColorManager();

        MockGeometry geom = new MockGeometry();
        geom.setCanonicalSites(new Coordinate[]{coord});
        MockLayerManager layerManager = new MockLayerManager();
        layer = new CellLayer(geom);
        layerManager.setCellLayer(layer);
        systemState.setLayerManager(layerManager);
    }

    // There are only three supported modes in the default color model,
    // so we can test all of them.
    public void testGetColor() throws Exception {
        // Test dead
//        systemState.setState(coord, 0);
        assertEquals(Color.BLACK, query.getColor(coord, systemState));

        // Test 1
        layer.getUpdateManager().place(new MockCell(1), coord);
        assertEquals(Color.BLUE, query.getColor(coord, systemState));

        // Test 2
        layer.getUpdateManager().banish(coord);
        layer.getUpdateManager().place(new MockCell(2), coord);
        assertEquals(Color.RED, query.getColor(coord, systemState));
    }

    public void testGetBorderColor() throws Exception {
        assertEquals(Color.DARK_GRAY, query.getBorderColor());
    }
}
