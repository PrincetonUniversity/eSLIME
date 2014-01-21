package geometry.boundaries;

import geometry.lattice.Lattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import structural.Flags;
import structural.identifiers.Coordinate;

public class PlaneRingReflecting extends Boundary {

	private PlaneRingHelper helper;
	
	public PlaneRingReflecting(Shape shape, Lattice lattice) {
		super(shape, lattice);
		helper = new PlaneRingHelper(lattice, shape.getDimensions());
	}

	@Override
	public Coordinate apply(Coordinate c) {
		Coordinate ob = shape.getOverbounds(c);

		// First, fix x if needed.
		Coordinate wrapped, reflected;
		
		if (ob.x() != 0) {
			wrapped = helper.wrap(c);
		} else {
			wrapped = c;
		}
		
		// Next, fix y if needed.
		if (ob.y() != 0) {
			reflected = helper.reflect(wrapped);
		} else {
			reflected = wrapped;
		}

		return reflected;
	}

	@Override
	public boolean isInfinite() {
		return false;
	}

	@Override
	protected void verify(Shape shape, Lattice lattice) {
		// PlaneRing is compatible only with Rectangle shapes.
		if (!(shape instanceof Rectangle)) {
			throw new IllegalArgumentException("PlaneRingReflecting boundary requires a Rectangle shape.");
		}
	}

    @Override
    public Boundary clone(Shape scaledShape, Lattice clonedLattice) {
        return new PlaneRingReflecting(scaledShape, clonedLattice);
    }
}
