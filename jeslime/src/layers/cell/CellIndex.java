/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package layers.cell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import structural.identifiers.Coordinate;

/**
 * A coordinate set, backed by a hash set, that canonicalizes
 * coordinates before using them.
 * 
 * @author dbborens
 *
 */
public class CellIndex implements Set<Coordinate> {

	private HashSet<Coordinate> contents = new HashSet<Coordinate>();
	
	@Override
	public boolean add(Coordinate e) {
		Coordinate c = e.canonicalize();
		return contents.add(c);
	}

	@Override
	public boolean addAll(Collection<? extends Coordinate> toAdd) {
		ArrayList<Coordinate> canonical = new ArrayList<Coordinate>(toAdd.size());
		
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

}
