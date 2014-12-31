/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package cells;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Cells can manipulate the local concentrations of continuum
 * layers by either exponentiating the local concentration
 * (decay, feedback) or by directly adding/subtracting from the
 * local concentration at a constrant rate(source/sink).
 *
 * The RelationshipManager reports the value of these
 * relationships, as well as notifying the related fields when
 * the cell dies.
 *
 * Created by dbborens on 12/30/14.
 */
public class RelationshipManager {

    private HashMap<String, Double> expMappings;    // Exponentiation
    private HashMap<String, Double> injMappings;    // Injection

    public RelationshipManager() {
        expMappings = new HashMap<>();
        injMappings = new HashMap<>();
    }

    /**
     * Get the exponent sumand for the specified field.
     * @param id
     * @return
     */ public double getExp(String id) {
        if (!expMappings.containsKey(id)) {
            return 0.0; } else {
            return expMappings.get(id);
        }
    }

    /**
     * Get the direct production (source/injection) sumand for the specified
     * field.
     *
     * @param id
     * @return
     */
    public double getInj(String id) {
        if (!injMappings.containsKey(id)) {
            return 0.0;
        } else {
            return injMappings.get(id);
        }
    }

    /**
     * Returns the set of all fields upon which this cell is capable of acting.
     *
     * @return
     */
    public Collection<String> getRelationships() {
        Set<String> ret = new HashSet<>(expMappings.keySet());
        ret.addAll(injMappings.keySet());

        return ret;
    }

    public void setInj(String id, double value) {
        injMappings.put(id, value);
    }

    public void setExp(String id, double value) {
        expMappings.put(id, value);
    }
}
