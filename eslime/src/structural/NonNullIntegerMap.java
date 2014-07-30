/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package structural;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by David B Borenstein on 4/11/14.
 */
public class NonNullIntegerMap extends HashMap<Integer, Integer> {
    public NonNullIntegerMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public NonNullIntegerMap(int initialCapacity) {
        super(initialCapacity);
    }

    public NonNullIntegerMap() {
        super();
    }

    public NonNullIntegerMap(Map<? extends Integer, ? extends Integer> m) {
        super(m);
    }

    @Override
    public Integer get(Object key) {
        if (!(key instanceof Integer)) {
            throw new IllegalStateException("Received unexpected key class.");
        }

        if (!containsKey(key)) {
            return 0;
        }

        return super.get(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NonNullIntegerMap other = (NonNullIntegerMap) this;

        if (other.size() != this.size()) {
            return false;
        }

        for (Integer key : keySet()) {
            if (!other.containsKey(key)) {
                return false;
            }

            if (get(key) != other.get(key)) {
                return false;
            }
        }

        return true;
    }
}
