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

package geometry;

import java.util.ArrayList;
import java.util.HashMap;

import structural.Flags;
import structural.identifiers.Coordinate;

public class MockGeometry extends Geometry {

	/* infinite */
	
	public MockGeometry() {
		super(null, null, null);
	}

	private boolean infinite;
	
	public void setInfinite(boolean infinite) {
		this.infinite = infinite;
	}
	
	@Override
	public boolean isInfinite() {
		return infinite;
	}
	
	/* canonicalSites */
	
	protected Coordinate[] canonicalSites;
	
	public void setCanonicalSites(Coordinate[] canonicalSites) {
        // Note that the MockGeometry override of setCanonicalSites
        // does not canonicalize coordinates -- in particular, 2D
        // coordinates will not receive the PLANAR flag.
		this.canonicalSites = canonicalSites;
		rebuildIndex();
	}
	
	@Override
	public Coordinate[] getCanonicalSites() {
		return canonicalSites;
	}
	
	private HashMap<Coordinate, Coordinate[]> cellNeighbors = new HashMap<Coordinate, Coordinate[]>();

	public void setCellNeighbors(Coordinate coord, Coordinate[] neighbors) {
		cellNeighbors.put(coord, neighbors);
	}

	@Override
	public Coordinate[] getNeighbors(Coordinate coord, int mode) {
		return cellNeighbors.get(coord);
	}
	
	protected void consider(ArrayList<Coordinate> neighbors, int x, int y) {
		Coordinate candidate = new Coordinate(x, y, 0);
		
		for (int i = 0; i < canonicalSites.length; i++) {
			if (canonicalSites[i].equals(candidate)) {
				neighbors.add(candidate);
				return;
			}
		}

	}

	protected void consider(ArrayList<Coordinate> neighbors, int x, int y, int z) {
		Coordinate candidate = new Coordinate(x, y, z, 0);
		
		for (int i = 0; i < canonicalSites.length; i++) {
			if (canonicalSites[i].equals(candidate)) {
				neighbors.add(candidate);
				return;
			}
		}
	}

	private int connectivity;
	
	public int getConnectivity() {
		return connectivity;
	}

	public void setConnectivity(int connectivity) {
		this.connectivity = connectivity;
	}
	
	private int dimensionality;

	public int getDimensionality() {
		return dimensionality;
	}

	public void setDimensionality(int dimensionality) {
		this.dimensionality = dimensionality;
	}
	
	/* rel2abs -- overriden as needed in subclass mocks */
	public Coordinate rel2abs(Coordinate origin, Coordinate displacement, int mode) {
		return origin.addFlags(Flags.UNDEFINED);
	}

    private Coordinate center;

    public void setCenter(Coordinate center) {
        this.center = center;
    }

    public Coordinate getCenter() {
        return center;
    }

    private boolean reportEquals;

    /**
     * Causes the equality operator of this mock object to return
     * the specified value in all cases.
     */
    public void setEquals(boolean reportEquals) {
        this.reportEquals = reportEquals;
    }

    @Override
    public boolean equals(Object obj) {
        return reportEquals;
    }

    public double getLastRequestedScale() {
        return lastRequestedScale;
    }

    private double lastRequestedScale = 0;

    @Override
    public Geometry cloneAtScale(double rangeScale) {
        lastRequestedScale = rangeScale;
        return null;
    }

}
