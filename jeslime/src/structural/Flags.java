package structural;

public class Flags {
	
	/********************/
	/* COORDINATE FLAGS */
	/********************/

	/* Type flags */

	// If "planar" is set, geometry is 2D; otherwise it is 3D.
	public static final int PLANAR        = 1;




	// Indicates that this is a vector. Affects text representation.
	public static final int VECTOR     = 1 << 1;
	
	// Indicates that boundary conditions have been applied.
	public static final int BOUNDARY_APPLIED     = 1 << 2;
	public static final int BOUNDARY_IGNORED     = 1 << 3;

	/* Undefined state flag */
	public static final int UNDEFINED     = 1 << 29;
	
	/* Boundary flags */
	public static final int END_OF_WORLD  = 1 << 30;
	public static final int BEYOND_BOUNDS = 1 << 31;

	// If "triangular" is set, geometry is triangular; otherwise it is rectangular.
	public static final int RESERVED4     = 1 << 4;
	public static final int RESERVED5     = 1 << 5;
	public static final int RESERVED6     = 1 << 6;
	// ...
	public static final int RESERVED29     = 1 << 28;

}
