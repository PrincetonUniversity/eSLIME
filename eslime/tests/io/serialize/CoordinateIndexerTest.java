/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize;

import control.identifiers.Coordinate;
import geometry.MockGeometry;
import io.serialize.text.CoordinateIndexer;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import structural.MockGeneralParameters;
import structural.utilities.FileConventions;
import test.EslimeTestCase;

/**
 * Created by dbborens on 12/10/13.
 */
public class CoordinateIndexerTest extends EslimeTestCase {

    private MockGeometry geom;
    private MockGeneralParameters params;
    private CoordinateIndexer indexer;
    private MockLayerManager lm;

    public void setUp() {
        geom = new MockGeometry();

        Coordinate[] canonicals = new Coordinate[]{
                new Coordinate(0, 0, 0),
                new Coordinate(0, 1, 0),
                new Coordinate(1, 0, 0),
                new Coordinate(1, 1, 0)
        };

        geom.setCanonicalSites(canonicals);
        params = new MockGeneralParameters();
        params.setInstancePath(outputPath);
        lm = new MockLayerManager();
        CellLayer layer = new CellLayer(geom);
        lm.setCellLayer(layer);
        indexer = new CoordinateIndexer(params);
        indexer.init(lm);
    }

    public void testCoordinateIndexer() throws Exception {
        // dispatchHalt instructs the indexer to build the index
        indexer.dispatchHalt(null);

//        // Compare output to fixture
//        String fixture = fixturePath + FileConventions.COORDINATE_FILENAME;
//        String output = outputPath + FileConventions.COORDINATE_FILENAME;

        assertFilesEqual(FileConventions.COORDINATE_FILENAME);
    }

}
