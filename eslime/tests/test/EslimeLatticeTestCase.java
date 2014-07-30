/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
