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

package control.identifiers;


public class Coordinate implements Comparable<Coordinate> {
    protected final int x, y, z;
    protected final int flags;

    public Coordinate(int x, int y, int flags) {
        this.x = x;
        this.y = y;

        z = 0;

        this.flags = flags | Flags.PLANAR;
    }

    public Coordinate(int x, int y, int z, int flags) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.flags = flags;
    }

    /**
     * Copy constructor.
     *
     * @param c
     */
    public Coordinate(Coordinate c) {
        this.x = c.x;
        this.y = c.y;
        this.z = c.z;
        this.flags = c.flags;
    }

    public Coordinate(int[] vec, int flags) {
        x = vec[0];
        y = vec[1];
        this.flags = flags;
        if (!hasFlag(Flags.PLANAR)) {
            z = vec[2];
        } else z = Integer.MIN_VALUE;
    }

    @Override
    public Coordinate clone() {
        if (hasFlag(Flags.PLANAR)) {
            return new Coordinate(x, y, flags);
        } else {
            return new Coordinate(x, y, z, flags);
        }
    }

    public int norm() {
        int total = 0;
        total += Math.abs(x);
        total += Math.abs(y);

        if (!hasFlag(Flags.PLANAR)) {
            total += Math.abs(z);
        }

        return total;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int z() {
        return z;
    }

    public int flags() {
        return flags;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + flags;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Coordinate other = (Coordinate) obj;
        if (flags != other.flags)
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        if (z != other.z)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return stringForm();
    }

    public String stringForm() {
        if (hasFlag(Flags.VECTOR)) {
            return canonical("<", ">", false);
        } else {
            return canonical("(", ")", true);
        }
    }

    protected String canonical(String open, String close, boolean useFlags) {
        StringBuilder ss = new StringBuilder();

        ss.append(open);
        ss.append(x);
        ss.append(", ");
        ss.append(y);

        if ((flags & Flags.PLANAR) == 0) {
            ss.append(", ");
            ss.append(z);
        }

        if (useFlags) {
            ss.append(" | ");
            ss.append(flags);
        }

        ss.append(close);

        String s = ss.toString();

        return s;
    }

    public boolean hasFlag(int flag) {
        return ((flags & flag) != 0);
    }

    /**
     * This (arbitrary) comparator is used to make ordered
     * arrays, eg for tests.
     */
    @Override
    public int compareTo(Coordinate o) {
        if (x > o.x) {
            return 1;
        } else if (x < o.x) {
            return -1;
        } else if (y > o.y) {
            return 1;
        } else if (y < o.y) {
            return -1;
        } else if (z > o.z) {
            return 1;
        } else if (z < o.z) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Clone the coordinate with additional flags.
     */
    public Coordinate addFlags(int f) {
        Coordinate ret;
        int rf = flags() | f;

        if (hasFlag(Flags.PLANAR)) {
            ret = new Coordinate(x, y, rf);
        } else {
            ret = new Coordinate(x, y, z, rf);
        }

        return ret;
    }

    /**
     * Return a coordinate with same location as this one, and all
     * flags cleared except for the PLANAR flag.
     *
     * @return
     */
    public Coordinate canonicalize() {
        Coordinate ret;

        if (hasFlag(Flags.PLANAR)) {
            ret = new Coordinate(x, y, 0);
        } else {
            ret = new Coordinate(x, y, z, 0);
        }

        return ret;
    }

}
