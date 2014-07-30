/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
