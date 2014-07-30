/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers;

import junit.framework.TestCase;
import layers.cell.StateMapViewer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StateMapViewerTest extends TestCase {

    public void testGetStates() {
        // Set up
        StateMapViewer viewer = makeViewer();

        // Get state array
        Integer[] states = viewer.getStates();

        // Sort it
        Arrays.sort(states);

        // Verify elements
        assertEquals(states.length, 2);
        assertEquals((int) states[0], 1);
        assertEquals((int) states[1], 2);

    }

    public void testGetCount() {
        // Set up
        StateMapViewer viewer = makeViewer();

        int count = viewer.getCount(2);

        assertEquals(count, 5);
    }

    private StateMapViewer makeViewer() {
        Map<Integer, Integer> stateMap = new HashMap<Integer, Integer>();
        stateMap.put(1, 3);
        stateMap.put(2, 5);

        StateMapViewer viewer = new StateMapViewer(stateMap);

        return viewer;
    }
}
