package geometry.boundaries;

import geometry.lattice.Lattice;
import geometry.shape.Shape;
import structural.identifiers.Coordinate;

public abstract class Boundary {

	protected Shape shape;
	protected Lattice lattice;
	
	public Boundary(Shape shape, Lattice lattice) {
		verify(shape, lattice);
		this.shape = shape;
		this.lattice = lattice;
	}
	
	protected abstract void verify(Shape shape, Lattice lattice);
	
	public abstract Coordinate apply(Coordinate c);
	
	/**
	 * If false, we are allowed to check against the size of the canonical site
	 * array to determine the number of legal lattice sites. If false, we should
	 * treat the number of legal lattice sites as infinite. 
	 * 
	 * Note that, if an infinite number of sites is promised, this promise must
	 * be kept (for annulus, etc.) or else jeSLIME is likely to enter infinite
	 * loop conditions and hang.
	 * 
	 * @return
	 */
	public abstract boolean isInfinite();

    /**
     * Boundaries are considered equal if and only if they are the
     * same class. Equality does NOT require that boundaries are being
     * applied to the same shape or lattice!
     *
     * If a boundary requires additional arguments (such as a constant
     * flux rate or some other parameter), then the child class should
     * override the equality operator, first invoking this equality
     * operator.
     */
    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }

        return true;
    }

    public abstract Boundary clone(Shape scaledShape, Lattice clonedLattice);
}
