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

package geometry.set;

/**
 * Created by dbborens on 7/28/14.
 */
public class LineSet extends CoordinateSet {
    public LineSet() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // OLD CODE: DO NOT USE
//    protected Coordinate[] coordinateLine(Element siteElement) {
//        ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
//
//        Element originElem = siteElement.element("origin");
//        Element displacementElem = siteElement.element("displacement");
//
//        int x0 = Integer.valueOf(originElem.attribute("x").getValue());
//        int y0 = Integer.valueOf(originElem.attribute("y").getValue());
//
//        int du = Integer.valueOf(displacementElem.attribute("u").getValue());
//        int dv = Integer.valueOf(displacementElem.attribute("v").getValue());
//
//        // Remember that 2D triangular layerManager.getCellLayer().getGeometry().tries have a third, non-orthogonal
//        // basis vector.
//        int dw = 0;
//        if (displacementElem.attribute("w") != null) {
//            dw = Integer.valueOf(displacementElem.attribute("w").getValue());
//        }
//
//
//        int length = Integer.valueOf(siteElement.element("length").getText());
//
//        Coordinate c;
//        if (siteElement.attribute("z") != null) {
//            int z0 = Integer.valueOf(originElem.element("z").getText());
//            c = new Coordinate(x0, y0, z0, 0);
//        } else {
//            c = new Coordinate(x0, y0, 0);
//        }
//
//        coordinates.add(c);
//        int[] dArr = new int[]{du, dv, dw};
//
//        Coordinate d = new Coordinate(dArr, c.flags() | Flags.VECTOR);
//
//        for (int i = 0; i < length; i++) {
//            Coordinate cNext = layerManager.getCellLayer().getGeometry().rel2abs(c, d, Geometry.APPLY_BOUNDARIES);
//            c = cNext;
//            coordinates.add(c);
//        }
//
//        return coordinates.toArray(new Coordinate[0]);
//    }
}
