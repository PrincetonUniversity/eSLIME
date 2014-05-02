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

import control.identifiers.Coordinate;

import java.util.*;

/**
 * A coordinate set, backed by a hash set, that canonicalizes
 * coordinates before using them.
 *
 * @author dbborens
 */
public class CellIndex implements Set<Coordinate> {

    private Set<Coordinate> contents = new HashSet<>();

    public CellIndex() {
    }

    public CellIndex(Set<Coordinate> contents) {
        for (Coordinate c : contents) {
            add(c);
        }
    }

    @Override
    public boolean add(Coordinate e) {
        Coordinate c = e.canonicalize();
        return contents.add(c);
    }

    @Override
    public boolean addAll(Collection<? extends Coordinate> toAdd) {
        ArrayList<Coordinate> canonical = new ArrayList<>(toAdd.size());

        for (Coordinate c : toAdd) {
            canonical.add(c.canonicalize());
        }

        return contents.addAll(canonical);
    }

    @Override
    public void clear() {
        contents.clear();
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Coordinate)) {
            return false;
        }

        Coordinate c = (Coordinate) o;

        Coordinate cc = c.canonicalize();

        return contents.contains(cc);
    }

    @Override
    public boolean containsAll(Collection<?> elems) {
        ArrayList<Coordinate> ce = new ArrayList<Coordinate>(elems.size());
        for (Object e : elems) {
            if (!(e instanceof Coordinate)) {
                return false;
            }
            Coordinate c = (Coordinate) e;
            Coordinate cc = c.canonicalize();
            ce.add(cc);
        }

        return contents.containsAll(ce);
    }

    @Override
    public boolean isEmpty() {
        return contents.isEmpty();
    }

    @Override
    public Iterator<Coordinate> iterator() {
        return contents.iterator();
    }

    public CellIndexViewer set() {
        CellIndexViewer copy = new CellIndexViewer(this);
        return copy;
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof Coordinate)) {
            return false;
        }

        Coordinate c = (Coordinate) o;

        Coordinate cc = c.canonicalize();

        return contents.remove(cc);
    }

    @Override
    public boolean removeAll(Collection<?> elems) {
        ArrayList<Coordinate> ce = new ArrayList<Coordinate>(elems.size());
        for (Object e : elems) {
            if (!(e instanceof Coordinate)) {
                return false;
            }
            Coordinate c = (Coordinate) e;
            Coordinate cc = c.canonicalize();
            ce.add(cc);
        }

        return contents.removeAll(ce);
    }

    @Override
    public boolean retainAll(Collection<?> elems) {
        ArrayList<Coordinate> ce = new ArrayList<Coordinate>(elems.size());
        for (Object e : elems) {
            if (!(e instanceof Coordinate)) {
                return false;
            }
            Coordinate c = (Coordinate) e;
            Coordinate cc = c.canonicalize();
            ce.add(cc);
        }

        return contents.retainAll(ce);
    }

    @Override
    public int size() {
        return contents.size();
    }

    @Override
    public Object[] toArray() {
        return contents.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return contents.toArray(a);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellIndex that = (CellIndex) o;

        if (contents != null ? !contents.equals(that.contents) : that.contents != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return contents != null ? contents.hashCode() : 0;
    }
}
