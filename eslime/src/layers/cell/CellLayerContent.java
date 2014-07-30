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
import control.identifiers.Coordinate;
import geometry.Geometry;
import structural.CanonicalCellMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author David Bruce Borenstein
 */
public abstract class CellLayerContent {

    // Mapping of sites to cell pointers -- the lattice itself.
    protected CanonicalCellMap map;

    // All sites (basically the keyset for the lattice map).
    protected Set<Coordinate> canonicalSites;

    protected Geometry geom;
    protected CellLayerIndices indices;

    public CellLayerContent(Geometry geom, CellLayerIndices indices) {
        // Assign geometry.
        this.geom = geom;

        this.indices = indices;

        // Get canonical site list.
        canonicalSites = new HashSet<>(geom.getCanonicalSites().length);

        Coordinate[] cc = geom.getCanonicalSites();
        // Initialize map.
        map = new CanonicalCellMap();
        for (int i = 0; i < cc.length; i++) {
            Coordinate coord = cc[i];

            // Initialize each site to null (empty)
            map.put(coord, null);
            canonicalSites.add(coord);
        }
    }

    public Set<Coordinate> getOccupiedSites() {
        return indices.getOccupiedSites();
    }

    public Set<Coordinate> getDivisibleSites() {
        return indices.getDivisibleSites();
    }

    public Cell get(Coordinate coord) {

        // Get pointer to cell and return it
        Cell res = map.get(coord);

        return res;
    }

    public boolean has(Coordinate coord) {
        return (get(coord) != null);
    }

    /**
     * Returns a vector containing the canonical coordinate of each
     * site on the lattice.
     *
     * @return
     */
    public Coordinate[] getCanonicalSites() {
        // Construct a copy of internal state
        Coordinate[] res = geom.getCanonicalSites();

        // Return it
        return res;
    }

    public void put(Coordinate coord, Cell current) {
        Cell previous = map.get(coord);
        indices.refresh(coord, previous, current);
        map.put(coord, current);
    }

    public void remove(Coordinate coord) {
        Cell previous = map.get(coord);
        indices.refresh(coord, previous, null);
        map.put(coord, null);
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
     * Returns true iff the specified site has a canonical form
     * in this geometry.
     */
    public boolean hasCanonicalForm(Coordinate coord) {
        Coordinate canonical = coord.canonicalize();
        boolean has = canonicalSites.contains(canonical);
        return has;
    }

    /**
     * Returns the health vector, in canonical site order.
     */
    public double[] getHealthVector() {
        Coordinate[] cArr = getCanonicalSites();

        double[] fArr = new double[cArr.length];

        for (int i = 0; i < cArr.length; i++) {
            Cell c = map.get(cArr[i]);
            if (c == null) {
                fArr[i] = 0D;
            } else {
                fArr[i] = map.get(cArr[i]).getHealth();
            }
        }

        return fArr;
    }

    public abstract void sanityCheck(Coordinate coord);

    public abstract Set<Coordinate> getImaginarySites();

    public Coordinate locate(Cell cell) {
        return indices.locate(cell);
    }

    public Map<Integer, Integer> getStateMap() {
        return indices.getStateMap();
    }

    public boolean isIndexed(Cell cell) {
        return indices.isIndexed(cell);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellLayerContent that = (CellLayerContent) o;

        if (canonicalSites != null ? !canonicalSites.equals(that.canonicalSites) : that.canonicalSites != null)
            return false;
        if (geom != null ? !geom.equals(that.geom) : that.geom != null)
            return false;
        if (indices != null ? !indices.equals(that.indices) : that.indices != null)
            return false;
        if (map != null ? !map.equals(that.map) : that.map != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = map != null ? map.hashCode() : 0;
        result = 31 * result + (canonicalSites != null ? canonicalSites.hashCode() : 0);
        result = 31 * result + (geom != null ? geom.hashCode() : 0);
        result = 31 * result + (indices != null ? indices.hashCode() : 0);
        return result;
    }

    @Override
    public abstract CellLayerContent clone();
}
