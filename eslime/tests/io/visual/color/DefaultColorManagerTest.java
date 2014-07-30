/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
