package jeslime.mock;

import java.util.HashMap;

import structural.identifiers.Coordinate;
import geometries.Geometry;

public class MockGeometry extends Geometry {

	/* infinite */
	
	private boolean infinite;
	
	public void setInfinite(boolean infinite) {
		this.infinite = infinite;
	}

	@Override
	public boolean isInfinite() {
		return infinite;
	}
	
	/* canonicalSites */
	
	private Coordinate[] canonicalSites;
	
	public void setCanonicalSites(Coordinate[] canonicalSites) {
		this.canonicalSites = canonicalSites;
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
	public Coordinate[] getCellNeighbors(Coordinate coord) {
		return cellNeighbors.get(coord);
	}
	
	@Override
	public Coordinate wrap(int x0, int y0, int z0) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Coordinate[] getSoluteNeighbors(Coordinate coord) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Coordinate[] getAnnulus(Coordinate coord, int r, boolean circ) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public int[] getDisplacement(Coordinate pCoord, Coordinate qCoord) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Coordinate rel2abs(Coordinate coord, int[] displacement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSiteCount() {
		// TODO Auto-generated method stub
		return 0;
	}


}
