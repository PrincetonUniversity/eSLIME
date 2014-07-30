/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.identifiers;

public class TemporalCoordinate extends Coordinate {

    private double t;

    public TemporalCoordinate(int x, int y, double t, int flags) {
        super(x, y, flags);
        this.t = t;
    }

    public TemporalCoordinate(int x, int y, int z, double t, int flags) {
        super(x, y, z, flags);
        this.t = t;
    }

    public TemporalCoordinate(Coordinate c, double t) {
        super(c);
        this.t = t;
    }

    public double t() {
        return t;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(t);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        TemporalCoordinate other = (TemporalCoordinate) obj;
        if (Double.doubleToLongBits(t) != Double.doubleToLongBits(other.t))
            return false;
        return true;
    }

    @Override
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

        ss.append(" | ");
        ss.append(t);

        ss.append(close);

        String s = ss.toString();

        return s;
    }
}
