package jeslime.continuum.operations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import jeslime.mock.MockGeometry;
import structural.Flags;
import structural.identifiers.Coordinate;
import junit.framework.TestCase;

public abstract class AbstractMatrixTest extends TestCase {
	
	/*protected class LinearMockGeometry extends MockGeometry {
		
		public LinearMockGeometry() {
			super();
			setConnectivity(1);
			setDimensionality(1);
		}
		
		@Override
		public Coordinate[] getSoluteNeighbors(Coordinate coord) {

			if (!coord.hasFlag(Flags.PLANAR))
				throw new IllegalStateException();
			
			ArrayList<Coordinate> neighbors = new ArrayList<Coordinate>();
			
			int x = coord.x();
			int y = coord.y();
			
			consider(neighbors, x+1, y);
			consider(neighbors, x-1, y);
			
			return neighbors.toArray(new Coordinate[0]);
		}
	}
	
	protected class SquareMockGeometry extends MockGeometry {
		
		public SquareMockGeometry() {
			super();
			setConnectivity(2);
			setDimensionality(2);
		}
		
		@Override
		public Coordinate[] getSoluteNeighbors(Coordinate coord) {
			if (!coord.hasFlag(Flags.PLANAR))
				throw new IllegalStateException();
			
			ArrayList<Coordinate> neighbors = new ArrayList<Coordinate>();
			
			int x = coord.x();
			int y = coord.y();
			
			consider(neighbors, x+1, y);
			consider(neighbors, x-1, y);
			consider(neighbors, x, y+1);
			consider(neighbors, x, y-1);		
			
			return neighbors.toArray(new Coordinate[0]);
		}
	}
	
	protected class TriangularMockGeometry extends MockGeometry {
		
		public TriangularMockGeometry() {
			super();
			setConnectivity(3);
			setDimensionality(2);
		}
		
		@Override
		public Coordinate[] getSoluteNeighbors(Coordinate coord) {
			if (!coord.hasFlag(Flags.PLANAR))
				throw new IllegalStateException();
			
			ArrayList<Coordinate> neighbors = new ArrayList<Coordinate>();
			
			int x = coord.x();
			int y = coord.y();
			
			consider(neighbors, x, y+1);
			consider(neighbors, x+1, y+1);
			consider(neighbors, x+1, y);
			consider(neighbors, x, y-1);		
			consider(neighbors, x-1, y-1);		
			consider(neighbors, x-1, y);		

			return neighbors.toArray(new Coordinate[0]);
		}
	}
	
	protected class CubicMockGeometry extends MockGeometry {
		
		public CubicMockGeometry() {
			super();
			setConnectivity(3);
			setDimensionality(3);
		}
		
		@Override
		public Coordinate[] getSoluteNeighbors(Coordinate coord) {
			if (coord.hasFlag(Flags.PLANAR))
				throw new IllegalStateException(coord.toString());
			
			ArrayList<Coordinate> neighbors = new ArrayList<Coordinate>();
			
			int x = coord.x();
			int y = coord.y();
			int z = coord.z();

			consider(neighbors, x+1, y, z);
			consider(neighbors, x-1, y, z);
			consider(neighbors, x, y+1, z);
			consider(neighbors, x, y-1, z);
			consider(neighbors, x, y, z+1);
			consider(neighbors, x, y, z-1);

			return neighbors.toArray(new Coordinate[0]);
		}
	}*/
	
	public void testNothing() {
		fail();
	}
}
