package geometry.boundaries;

import geometry.lattice.Lattice;
import geometry.shape.Shape;
import structural.Flags;
import structural.identifiers.Coordinate;

/**
 * All boundaries are treated as infinite. However, going beyond
 * a specified limit results in the END_OF_WORLD flag being set.
 * This flag can then be handled within a specific process. In
 * practice, this amounts to a kind of absorbing boundary.
 * 
 * @test ArenaTest.java
 * @author David Bruce Borenstein
 *
 */
public class Arena extends Boundary {

	public Arena(Shape shape, Lattice lattice) {
		super(shape, lattice);
	}

	@Override
	public Coordinate apply(Coordinate c) {
		Coordinate ob = shape.getOverbounds(c);
		
		Coordinate ret;
		// Is any component overbound?
		if (ob.x() != 0 || ob.y() != 0 || ob.z() != 0) {
			// If yes, set the appropriate flags
			ret = c.addFlags(Flags.END_OF_WORLD | Flags.BOUNDARY_APPLIED);
		} else {
			ret = c;
		}

		return ret;
	}

	@Override
	public boolean isInfinite() {
		return true;
	}

	@Override
	protected void verify(Shape shape, Lattice lattice) {
		// Arena is compatible with all lattice geometries and 
		// shapes, so no verification is needed.
	}

}
