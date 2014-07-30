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

package layers.cell;

import control.identifiers.Coordinate;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by dbborens on 5/2/14.
 */
public class CellIndexViewer extends CellIndex {
    private CellIndex contents;

    public CellIndexViewer(CellIndex contents) {
        this.contents = contents;
    }

    @Override
    public boolean add(Coordinate e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends Coordinate> toAdd) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> elems) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> elems) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        return contents.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> elems) {
        return contents.containsAll(elems);
    }

    @Override
    public boolean isEmpty() {
        return contents.isEmpty();
    }

    @Override
    public Iterator<Coordinate> iterator() {
        return contents.iterator();
    }

    @Override
    public CellIndexViewer set() {
        return this;
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
        return contents.equals(o);
    }

    @Override
    public int hashCode() {
        return contents.hashCode();
    }
}
