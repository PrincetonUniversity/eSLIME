/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

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

package geometry.shape;

import control.identifiers.Coordinate;
import control.identifiers.Flags;
import geometry.lattice.Lattice;
import org.dom4j.Element;

import java.util.ArrayList;

public class Line extends Shape {

    private int length;

    public Line(Lattice lattice, int length) {
        super(lattice);
        this.length = length;
        
        init();
    }

    public Line(Lattice lattice, Element descriptor) {
        super(lattice);

        length = Integer.valueOf(descriptor.element("length").getTextTrim());

        init();
    }

    @Override
    protected void verify(Lattice lattice) {
        if (lattice.getDimensionality() != 1) {
            throw new IllegalArgumentException("Line shape requires a linear" +
                    " lattice connectivity.");
        }
    }

    @Override
    public Coordinate getCenter() {
        // 0-indexed coordinates
        int y = (length - 1) / 2;

        Coordinate center = new Coordinate(0, y, 0);

        Coordinate adjusted = lattice.adjust(center);

        return adjusted;
    }

    @Override
    public Coordinate[] getBoundaries() {

        return new Coordinate[] {
                new Coordinate(0, 0, 0),
                new Coordinate(0, length - 1, 0)
        };
    }

    @Override
    protected Coordinate[] calcSites() {
        Coordinate[] sites = new Coordinate[length];

        for (int y = 0; y < length; y++) {
            sites[y] = new Coordinate(0, y, 0);
        }

        return sites;
    }

    @Override
    public Coordinate getOverbounds(Coordinate coord) {
        // Get orthogonal distance from (0, 0) to this point.
        Coordinate origin = new Coordinate(0, 0, 0);
        Coordinate d = lattice.getOrthoDisplacement(origin, coord);

        int ob;
        if (d.y() > length) {
            ob = (length - d.y());
        } else if (d.y() < 0) {
            ob = -1 * d.y();
        } else {
            ob = 0;
        }

        return new Coordinate(0, ob, 0);
    }

    @Override
    public int[] getDimensions() {
        return new int[]{length};
    }

    @Override
    public boolean equals(Object obj) {
        // Is it a Line?
        if (!(obj instanceof Line)) {
            return false;
        }

        Line other = (Line) obj;

        if (other.length != this.length) {
            return false;
        }

        // If these things are OK, return true
        return true;
    }

    @Override
    public Shape cloneAtScale(Lattice clonedLattice, double rangeScale) {
        int scaledWidth, scaledLength;
        scaledLength = (int) Math.round(length * rangeScale);
        return new Line(clonedLattice, scaledLength);
    }
}
