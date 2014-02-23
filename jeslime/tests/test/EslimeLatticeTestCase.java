package test;

import geometry.MockGeometry;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import structural.identifiers.Coordinate;

/**
 * Convenience class providing automatic mock infrastructure
 * for tests that require a cell layer. This includes all
 * cells and cell processes, for example.
 *
 * Created by dbborens on 2/21/14.
 */
public abstract class EslimeLatticeTestCase extends EslimeTestCase {

    protected MockGeometry geom;
    protected MockLayerManager layerManager;
    protected CellLayer layer;
    protected Coordinate origin, x, y, z, yz;
    protected Coordinate[] cc;

    @Override
    protected void setUp() throws Exception {
        geom = buildMockGeometry();
        layerManager = new MockLayerManager();
        layer = new CellLayer(geom, 0);
        layerManager.setCellLayer(layer);

        cc = geom.getCanonicalSites();

        // I should really sort these out
        origin = cc[0];
        x  = cc[4];
        y  = cc[2];
        z  = cc[1];
        yz = cc[3];

    }
}
