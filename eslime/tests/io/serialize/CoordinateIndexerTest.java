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

package io.serialize;

import geometry.MockGeometry;
import io.serialize.text.CoordinateIndexer;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import structural.MockGeneralParameters;
import structural.identifiers.Coordinate;
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
        CellLayer layer = new CellLayer(geom, 0);
        lm.setCellLayer(layer);
        indexer = new CoordinateIndexer(params);
        indexer.init(lm);
    }

    public void testCoordinateIndexer() throws Exception {
        // dispatchHalt instructs the indexer to build the index
        indexer.dispatchHalt(null);

        // Compare output to fixture
        String fixture = fixturePath + FileConventions.COORDINATE_FILENAME;
        String output = outputPath + FileConventions.COORDINATE_FILENAME;

        assertFilesEqual(FileConventions.COORDINATE_FILENAME);
    }

}
