/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package cells;

import java.util.HashMap;

/**
 * Created by dbborens on 12/31/14.
 */
public class AgentContinuumIndex {
    private HashMap<String, ContinuumLinkage> linkages;

    public AgentContinuumIndex() {
        linkages = new HashMap<>();
    }

    public void put(String id, ContinuumLinkage linkage) {
        linkages.put(id, linkage);
    }

    /**
     * Get the value of the continuum field at this cell's location.
     *
     * @param id
     * @return
     */
    public double getValue(String id) {
        ContinuumLinkage linkage = linkages.get(id);
        return linkage.getValue();
    }

    public void removeFromAll() {
        linkages.values().forEach(cells.ContinuumLinkage::remove);
    }

    public double getMagnitude(String id) {
        if (!linkages.containsKey(id)) {
            return 0.0;
        } else {
            ContinuumLinkage linkage = linkages.get(id);
            return linkage.getMagnitude();
        }
    }

}
