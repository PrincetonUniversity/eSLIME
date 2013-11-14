package geometry.lattice;

import java.util.HashSet;

import structural.Flags;
import structural.identifiers.Coordinate;

public class CubicLattice extends Lattice {

	protected void defineBasis() {
		Coordinate i = new Coordinate(1, 0, 0, 0);
		Coordinate j = new Coordinate(0, 1, 0, 0);
		Coordinate k = new Coordinate(0, 0, 1, 0);

		basis = new Coordinate[] {i, j, k};
	}
	
	@Override
	public int getConnectivity() {
		return 3;
	}

	@Override
	public int getDimensionality() {
		return 3;
	}

	@Override
	public Coordinate adjust(Coordinate i) {
		if (i.hasFlag(Flags.PLANAR)) {
			throw new IllegalArgumentException("Cubic lattice is a 3D geometry.");
		}
				
		return i;
	}

	@Override
	public Coordinate[] getAnnulus(Coordinate coord, int r) {
		// De-index coordinate.
		int x0 = coord.x();
		int y0 = coord.y();
		int z0 = coord.z();
		
		// r=0 case (a point)
		if (r == 0) {
			return new Coordinate[] {new Coordinate(x0, y0, 0)};
		}
		
		// All other cases
		HashSet<Coordinate> ring = new HashSet<Coordinate>();
		
		for (int dx = 0; dx <= r; dx++) {
			for (int dy = 0; dy <= r - dx; dy++) {
				int dz = r - dx - dy;
				//System.out.println("r=" + r + " dx=" + dx + " dy=" + dy + " dz=" + dz);
				
				// TODO: This is redundant and may be inefficient
				ring.add(new Coordinate(x0 + dx, y0 + dy, z0 + dz, 0));
				ring.add(new Coordinate(x0 + dx, y0 + dy, z0 - dz, 0));
				ring.add(new Coordinate(x0 + dx, y0 - dy, z0 + dz, 0));
				ring.add(new Coordinate(x0 + dx, y0 - dy, z0 - dz, 0));
				ring.add(new Coordinate(x0 - dx, y0 + dy, z0 + dz, 0));
				ring.add(new Coordinate(x0 - dx, y0 + dy, z0 - dz, 0));
				ring.add(new Coordinate(x0 - dx, y0 - dy, z0 + dz, 0));
				ring.add(new Coordinate(x0 - dx, y0 - dy, z0 - dz, 0));

			}

		}
		
		return ring.toArray(new Coordinate[0]);
	}

	@Override
	public Coordinate getDisplacement(Coordinate pCoord, Coordinate qCoord) {
		if (pCoord.hasFlag(Flags.PLANAR) || qCoord.hasFlag(Flags.PLANAR)) {
			throw new IllegalArgumentException("Expect 3D coordinates in rectangular lattice.");
		}
		
		int dx = qCoord.x() - pCoord.x();
		int dy = qCoord.y() - pCoord.y();
		int dz = qCoord.z() - pCoord.z();

		return new Coordinate(dx, dy, dz, Flags.VECTOR);
	}

	@Override
	public Coordinate rel2abs(Coordinate coord, int[] displacement) {
		if (displacement.length != 3) {
			throw new IllegalArgumentException("Expected three arguments to rectangular lattice rel2abs.");
		}

		int x = coord.x();
		int y = coord.y();
		int z = coord.z();
		
		// Apply x component
		x += displacement[0];

		// Apply y component
		y += displacement[1];
		
		// Apply z component
		z += displacement[2];

		Coordinate target = new Coordinate(x, y, z, 0);
		
		return target;
	}

	@Override
	public Coordinate getOrthoDisplacement(Coordinate pCoord, Coordinate qCoord) {
		return getDisplacement(pCoord, qCoord);
	}

	@Override
	public Coordinate invAdjust(Coordinate toAdjust) {
		return toAdjust;
	}
	
}
