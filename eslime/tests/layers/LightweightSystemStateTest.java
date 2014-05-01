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
import geometry.Geometry;
import io.deserialize.MockCoordinateDeindexer;
import no.uib.cipr.matrix.DenseVector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dbborens on 3/26/14.
 */
public class LightweightSystemStateTest extends SystemStateTest {

    private LightweightSystemState query;
    private Coordinate[] canonicals;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockCoordinateDeindexer deindexer = new MockCoordinateDeindexer();
        Geometry g = buildMockGeometry();
        canonicals = g.getCanonicalSites();
        deindexer.setUnderlying(canonicals);
        query = new LightweightSystemState(deindexer);
    }

    @Override
    public void testGetHealth() throws Exception {
        double[] healthVector = new double[]{1.0, 0.0, -0.1, 2.0};
        query.setHealthVector(healthVector);
        for (int i = 0; i < 4; i++) {
            Coordinate coord = canonicals[i];
            double expected = healthVector[i];
            double actual = query.getHealth(coord);
            assertEquals(expected, actual, epsilon);
        }
    }

    @Override
    public void testGetState() throws Exception {
        int[] stateVector = new int[]{1, 0, 2, 3};
        query.setStateVector(stateVector);
        for (int i = 0; i < 4; i++) {
            Coordinate coord = canonicals[i];
            int expected = stateVector[i];
            int actual = query.getState(coord);
            assertEquals(expected, actual);
        }
    }

    @Override
    public void testGetValue() throws Exception {
        double[] values = new double[]{1.0, 0.0, 0.1, 2.0};
        DenseVector stateVector = new DenseVector(values);
        ContinuumState state = new ContinuumState(stateVector, 0.5, 2);
        String id = "id";
        HashMap<String, ContinuumState> stateMap = new HashMap<>(1);
        stateMap.put(id, state);
        query.setContinuumStates(stateMap);
        for (int i = 0; i < 4; i++) {
            Coordinate coord = canonicals[i];
            double expected = values[i];
            double actual = query.getValue(id, coord);
            assertEquals(expected, actual, epsilon);
        }
    }

    @Override
    public void testGetTime() throws Exception {
        double expected = 0.7;
        query.setTime(expected);
        double actual = query.getTime();
        assertEquals(expected, actual, epsilon);
    }

    @Override
    public void testGetFrame() throws Exception {
        int expected = 7;
        query.setFrame(expected);
        int actual = query.getFrame();
        assertEquals(expected, actual);
    }

    @Override
    public void testIsHighlighted() throws Exception {
        int channelId = 0;
        Set<Coordinate> expected = new HashSet<>(1);
        expected.add(canonicals[2]);
        query.setHighlights(channelId, expected);

        assertTrue(query.isHighlighted(channelId, canonicals[2]));
        assertFalse(query.isHighlighted(channelId, canonicals[0]));
    }
}
