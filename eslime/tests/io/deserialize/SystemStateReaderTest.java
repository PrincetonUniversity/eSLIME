/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.deserialize;

import cells.MockCell;
import control.identifiers.Coordinate;
import io.serialize.Serializer;
import io.serialize.binary.ContinuumStateWriter;
import io.serialize.binary.HighlightWriter;
import io.serialize.binary.TimeWriter;
import io.serialize.text.CoordinateIndexer;
import io.serialize.text.LegacyCellStateWriter;
import layers.LightweightSystemState;
import processes.MockStepState;
import structural.MockGeneralParameters;
import test.EslimeLatticeTestCase;

import java.util.Iterator;

//import layers.MockSoluteLayer;

/**
 * Test for the SystemStateReader. As an I/O orchestrator whose main function
 * is to open a bunch of files, this is an annoying class to mock. Ultimately,
 * I decided (for the time being) to use existing mocks, some of which are
 * incompatible. As a result, this is a rather cursory test. If I start running
 * into problems surrounding deserialization, I will rewrite the tests for this
 * class.
 * <p/>
 * Created by dbborens on 3/28/14.
 */
public class SystemStateReaderTest extends EslimeLatticeTestCase {
    // The only thing SystemStateReader does is to return an anonymous
    // iterator. Therefore, each test is actually about this iterator,
    // and the iterator is the query.
    private Iterator<LightweightSystemState> query;
    private String[] soluteIds;
    private int[] channelIds;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        String path = fixturePath + "SystemStateReader/";

        soluteIds = new String[]{"0"};
        channelIds = new int[]{0};
        SystemStateReader parent = new SystemStateReader(soluteIds, channelIds, path, geom);
        query = parent.iterator();
    }

    /**
     * The hasNext() function determines its value from the time file. If
     * the the other files disagree with this file, an error will occur.
     *
     * @throws Exception
     */
    public void testHasNext() throws Exception {
        // There are two frames specified in the time fixture, so we expect
        // that hasNext() will return true twice, and then return false the
        // third time.
        assertTrue(query.hasNext());

        query.next();
        assertTrue(query.hasNext());

        query.next();
        assertFalse(query.hasNext());
    }

    public void testNext() throws Exception {
        LightweightSystemState state = query.next();

        // Check solute state
//        assertEquals(1.0, state.getLayerManager().getSoluteLayer("0").getState().getAbsolute(origin), epsilon);

        // Check cell state
        assertEquals(5, state.getLayerManager().getCellLayer().getViewer().getState(x));
        assertEquals(2.0, state.getLayerManager().getCellLayer().getViewer().getCell(x).getHealth(), epsilon);

        // Origin is vacant
        assertEquals(0, state.getLayerManager().getCellLayer().getViewer().getState(origin));

        // Check time and frame
        assertEquals(2, state.getFrame());
        assertEquals(1.7, state.getTime(), epsilon);

        // Check highlighting
        assertTrue(state.isHighlighted(0, x));
        assertFalse(state.isHighlighted(0, y));
    }

    /**
     * Create the fixture files used in this test.
     */
    @SuppressWarnings("unused")
    private void generateFixtures() throws Exception {

        Serializer[] serializers = makeSerializerArray();

        /* Populate the system */
//        MockSoluteLayer layer0 = initializeSoluteLayer("0");
//        layerManager.addSoluteLayer("0", layer0);

//        pushState(layer0, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});

        placeCell(x, 2.0, 5);
        placeCell(y, 1.0, 3);

        Coordinate[] highlights = new Coordinate[]{x};

        MockStepState stepState = new MockStepState(1.7, 2);
        stepState.setHighlights(0, highlights);
        /* Initialize output and push first state */
        for (Serializer serializer : serializers) {
            serializer.init();
            serializer.flush(stepState);
            serializer.flush(stepState);
        }

        /* Set up second state */
        cellLayer.getUpdateManager().banish(x);
        highlights = new Coordinate[]{y};
        stepState = new MockStepState(4.8, 6);
        stepState.setHighlights(0, highlights);

//        pushState(layer0, new double[]{0.1, 0.2, 0.3, 0.4, 0.5});

        /* Push second state and close fixture */
        for (Serializer serializer : serializers) {
            serializer.flush(stepState);
            serializer.dispatchHalt(null);
        }
    }

    private Serializer[] makeSerializerArray() {
        MockGeneralParameters p = makeMockGeneralParameters();
        p.setIsFrameValue(true);
        Serializer[] ret = new Serializer[]{
                new CoordinateIndexer(p, layerManager),
                new TimeWriter(p, layerManager),
                new ContinuumStateWriter(p, layerManager),
                new LegacyCellStateWriter(p, layerManager),
                new HighlightWriter(p, new int[]{0}, layerManager)
        };

        return ret;
    }

//    private MockSoluteLayer initializeSoluteLayer(String id) {
//        MockSoluteLayer ret = new MockSoluteLayer();
//        ret.setGeometry(geom);
//        ret.setId(id);
//        return ret;
//    }

    private MockCell placeCell(Coordinate coord, double health, int state) throws Exception {
        MockCell cell = new MockCell();
        cell.setHealth(health);
        cell.setState(state);
        cellLayer.getUpdateManager().place(cell, coord);

        return cell;
    }

//    private void pushState(MockSoluteLayer layer, double[] state) {
//        DenseVector vector = new DenseVector(state);
//        SolutionViewer viewer = new SolutionViewer(vector, geom);
//        layer.push(viewer);
//    }
}
