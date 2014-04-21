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

package processes.continuum;

import cells.MockCell;
import control.identifiers.Coordinate;
import geometry.MockGeometry;
import io.loader.ProcessLoader;
import layers.LayerManager;
import layers.MockLayerManager;
import layers.MockSoluteLayer;
import layers.cell.CellLayer;
import no.uib.cipr.matrix.DenseVector;
import processes.MockStepState;
import processes.gillespie.GillespieState;
import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 1/7/14.
 */
public class FieldUpdateProcessTest extends EslimeTestCase {

    private ExposedFieldUpdateProcess query;
    private MockLayerManager layerManager;
    private MockSoluteLayer soluteLayer;
    private MockGeometry geom;
    private CellLayer cellLayer;

    @Override
    protected void setUp() throws Exception {
        initIndependentObjects();
        initCanonicalSites();
        initDependentObjects();
        initCells();
    }

    private void initCells() {
        MockCell inert, produceLo, produceHi;

        // Place an inert cell on (1, 0)
        inert = new MockCell();
        inert.setProduction(0.0);
        cellLayer.getUpdateManager().place(inert, new Coordinate(1, 0, 0));

        // Place a producer cell on (2, 0)
        produceLo = new MockCell();
        produceLo.setProduction(1.0);
        cellLayer.getUpdateManager().place(produceLo, new Coordinate(2, 0, 0));

        // Place a producer cell with a different yield on (3, 0)
        produceHi = new MockCell();
        produceHi.setProduction(2.0);
        cellLayer.getUpdateManager().place(produceHi, new Coordinate(3, 0, 0));
    }

    private void initIndependentObjects() {
        // Note: cellLayer has to be initialized after the canonical
        //    sites because its construction involves traversing
        //    pointers that would otherwise be null.
        soluteLayer = new MockSoluteLayer();
        geom = new MockGeometry();
        layerManager = new MockLayerManager();
    }

    private void initCanonicalSites() {
        // Create four canonical sites
        Coordinate[] canonicalSites = new Coordinate[4];
        for (int i = 0; i < 4; i++) {
            canonicalSites[i] = new Coordinate(i, 0, 0);
        }

        geom.setCanonicalSites(canonicalSites);
    }

    private void initDependentObjects() {
        cellLayer = new CellLayer(geom, 0);
        soluteLayer.setGeometry(geom);
        layerManager.addSoluteLayer("query", soluteLayer);
        layerManager.setCellLayer(cellLayer);
        query = new ExposedFieldUpdateProcess(null, 0, layerManager, "query");
    }

    public void testTargetSimple() {
        try {
            query.target(null);
        } catch (Exception ex) {
            fail();
        }
    }

    public void testTargetGillespie() throws Exception {
        Integer id = query.getID();
        Integer[] ids = new Integer[]{id};
        GillespieState gs = new GillespieState(ids);
        query.target(gs);
        gs.close();
        assertEquals(0.0, gs.getTotalWeight(), epsilon);
        assertEquals(1, gs.getTotalCount());
        assertEquals(1, gs.getEventCount(id));
    }

    public void testFire() throws Exception {
        MockStepState state = new MockStepState();
        query.fire(state);


        // The coordinate indices for the mock geometry are the same
        // as the array indices for the supplied canonical sites
        DenseVector source = soluteLayer.getSource();
        checkVector(source);


        // verify that SoluteLayer::integrate() was called
        assertTrue(soluteLayer.integrateWasFired());
    }

    private void checkVector(DenseVector source) {
        // verify source vector supplied to solute layer
        double[] expectedArr = new double[]{0.0, 0.0, 1.0, 2.0};
        for (int i = 0; i < 4; i++) {
            double actual = source.get(i);
            double expected = expectedArr[i];
            assertEquals(expected, actual);
        }
    }

    public void testCheckForProduction() throws Exception {
        // call check for production
        DenseVector actual = query.checkForProduction();

        // check resulting vector
        checkVector(actual);
    }

    private class ExposedFieldUpdateProcess extends FieldUpdateProcess {
        public ExposedFieldUpdateProcess(ProcessLoader loader, int processId, LayerManager layerManager, String layerId) {
            super(loader, processId, layerManager, null, layerId);
        }

        @Override
        public DenseVector checkForProduction() {
            return super.checkForProduction();
        }
    }
}
