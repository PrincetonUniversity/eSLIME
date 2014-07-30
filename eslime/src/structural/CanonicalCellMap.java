/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package structural;

import cells.Cell;
import control.identifiers.Coordinate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by David B Borenstein on 4/11/14.
 */
public class CanonicalCellMap extends HashMap<Coordinate, Cell> {
    public CanonicalCellMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public CanonicalCellMap(int initialCapacity) {
        super(initialCapacity);
    }

    public CanonicalCellMap() {
        super();
    }

    public CanonicalCellMap(Map<? extends Coordinate, ? extends Cell> m) {
        super(m);
    }

    @Override
    public boolean containsKey(Object key) {
        Coordinate canonical = objToCanonicalCoord(key);
        return super.containsKey(canonical);
    }

    @Override
    public Cell get(Object key) {
        Coordinate canonical = objToCanonicalCoord(key);

        return super.get(canonical);
    }

    @Override
    public Cell put(Coordinate key, Cell value) {
        Coordinate canonical = objToCanonicalCoord(key);
        return super.put(canonical, value);
    }

    private Coordinate objToCanonicalCoord(Object key) {
        if (!(key instanceof Coordinate)) {
            throw new ClassCastException();
        }

        Coordinate cKey = (Coordinate) key;
        Coordinate canonical = cKey.canonicalize();
        return canonical;
    }
}
