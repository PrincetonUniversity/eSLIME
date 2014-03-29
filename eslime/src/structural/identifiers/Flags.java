/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package structural.identifiers;

public class Flags {

    /********************/
    /* COORDINATE FLAGS */
    /**
     * ****************
     */

	/* Type flags */

    // If "planar" is set, geometry is 2D; otherwise it is 3D.
    public static final int PLANAR = 1;


    // Indicates that this is a vector. Affects text representation.
    public static final int VECTOR = 1 << 1;

    // Indicates that boundary conditions have been applied.
    public static final int BOUNDARY_APPLIED = 1 << 2;
    public static final int BOUNDARY_IGNORED = 1 << 3;

    /* Undefined state flag */
    public static final int UNDEFINED = 1 << 29;

    /* Boundary flags */
    public static final int END_OF_WORLD = 1 << 30;
    public static final int BEYOND_BOUNDS = 1 << 31;

    // If "triangular" is set, geometry is triangular; otherwise it is rectangular.
    public static final int RESERVED4 = 1 << 4;
    public static final int RESERVED5 = 1 << 5;
    public static final int RESERVED6 = 1 << 6;
    // ...
    public static final int RESERVED29 = 1 << 28;

}
