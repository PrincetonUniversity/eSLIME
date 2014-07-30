/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.cell;

import java.util.Map;

/**
 * @author David Bruce Borenstein
 * @test StateMapViewerTest
 */
public class StateMapViewer {

    private Map<Integer, Integer> stateMap;

    public StateMapViewer(Map<Integer, Integer> stateMap) {
        this.stateMap = stateMap;
    }

    public Integer[] getStates() {
        Integer[] states = stateMap.keySet().toArray(new Integer[0]);
        return states;
    }

    public Integer getCount(Integer state) {

        // If it doesn't have the key, there are no cells of that state
        if (!stateMap.containsKey(state)) {
            return 0;
        }

        return stateMap.get(state);
    }
}
