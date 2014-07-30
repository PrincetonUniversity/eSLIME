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
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package test;

import control.identifiers.Coordinate;
import geometry.MockGeometry;
import layers.MockLayerManager;
import layers.cell.CellLayer;

/**
 * Convenience class providing automatic mock infrastructure
 * for tests that require a cell layer. This includes all
 * cells and cell processes, for example.
 * <p/>
 * Created by dbborens on 2/21/14.
 */
public abstract class EslimeLatticeTestCase extends EslimeTestCase {

    protected MockGeometry geom;
    protected MockLayerManager layerManager;
    protected CellLayer cellLayer;
    protected Coordinate origin, x, y, z, yz;
    protected Coordinate[] cc;

    @Override
    protected void setUp() throws Exception {
        geom = buildMockGeometry();
        layerManager = new MockLayerManager();
        cellLayer = new CellLayer(geom);
        layerManager.setCellLayer(cellLayer);

        cc = geom.getCanonicalSites();

        // I should really sort these out
        origin = cc[0];
        x = cc[4];
        y = cc[2];
        z = cc[1];
        yz = cc[3];

    }
}
