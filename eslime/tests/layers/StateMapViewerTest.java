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
