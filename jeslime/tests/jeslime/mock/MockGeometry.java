package jeslime.mock;

import java.util.ArrayList;
import java.util.HashMap;

import structural.Flags;
import structural.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.shape.Shape;

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
		this.canonicalSites = canonicalSites;
		rebuildIndex();
	}
	
	@Override
	public Coordinate[] getCanonicalSites() {
		return canonicalSites;
	}
	
	/* cell neighbors */
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
}
