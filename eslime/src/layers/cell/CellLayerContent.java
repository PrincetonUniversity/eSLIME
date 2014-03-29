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

package layers.cell;

import cells.Cell;
import geometry.Geometry;
import structural.identifiers.Coordinate;
import structural.identifiers.Flags;

import java.util.HashMap;

/**
 * @author David Bruce Borenstein
 * @test CellLayerContentTest
 */
public class CellLayerContent {

    // Mapping of sites to cell pointers -- the lattice itself.
    protected HashMap<Coordinate, Cell> map;

    // All sites (basically the keyset for the lattice map).
    protected Coordinate[] canonicalSites;

    protected Geometry geom;
    protected CellLayerIndices indices;

    public CellLayerContent(Geometry geom, CellLayerIndices indices) {
        // Assign geometry.
        this.geom = geom;
        this.indices = indices;
        // Get canonical site list.
        canonicalSites = geom.getCanonicalSites();

        // Initialize map.
        map = new HashMap<>();
        for (int i = 0; i < canonicalSites.length; i++) {
            Coordinate coord = canonicalSites[i];

            // Initialize each site to null (empty)
            map.put(coord, null);
        }
    }

    public Coordinate checkExists(Coordinate coord) {
        // First check to see if this cell is supposed to be retained even though
        // it is not a "real" coordinate.
        if (geom.isInfinite() && coord.hasFlag(Flags.END_OF_WORLD)) {
            return coord;
        }

        Coordinate canonical = coord.canonicalize();

        // Otherwise, it had better be in the coordinate system.
        if (!map.containsKey(canonical)) {
            StringBuilder ss = new StringBuilder();
            ss.append("Consistency failure: coordinate ");
            ss.append(coord.stringForm());
            ss.append(" does not exist in this geometry.\n");
            String str = ss.toString();
            throw new IllegalStateException(str);
        }
        return canonical;
    }

    public Cell get(Coordinate coord) {
        checkExists(coord);

        // Validate input
        if (!indices.isOccupied(coord))
            throw new IllegalStateException("Attempted to access an empty cell at " + coord);

        // Get pointer to cell and return it
        Cell res = map.get(coord.canonicalize());

        return res;
    }

    /**
     * Returns a vector containing the canonical coordinate of each
     * site on the lattice.
     *
     * @return
     */
    public Coordinate[] getCanonicalSites() {
        // Construct a copy of internal state
        Coordinate[] res = canonicalSites.clone();

        // Return it
        return res;
    }

    public void put(Coordinate coord, Cell cell) {
        checkExists(coord);
        map.put(coord, cell);
    }

    public int[] getStateVector() {
        Coordinate[] cArr = getCanonicalSites();

        int[] sArr = new int[cArr.length];

        for (int i = 0; i < cArr.length; i++) {
            Cell c = map.get(cArr[i]);
            if (c == null) {
                sArr[i] = 0;
            } else {
                sArr[i] = map.get(cArr[i]).getState();
            }
        }

        return sArr;
    }

    /**
     * Returns the fitness vector, in canonical site order.
     */
    public double[] getFitnessVector() {
        Coordinate[] cArr = getCanonicalSites();

        double[] fArr = new double[cArr.length];

        for (int i = 0; i < cArr.length; i++) {
            Cell c = map.get(cArr[i]);
            if (c == null) {
                fArr[i] = 0D;
            } else {
                fArr[i] = map.get(cArr[i]).getFitness();
            }
        }

        return fArr;
    }
}
