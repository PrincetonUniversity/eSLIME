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
public class RectangleSet extends CoordinateSet {

    public RectangleSet() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

      // OLD CODE: DO NOT USE
//    protected Coordinate[] coordinateRectangle(Element siteElement) {
//        ArrayList<Coordinate> coordinates = new ArrayList<>();
//
//        // Specifies one corner of the rectangle/prism.
//        Element originElem = siteElement.element("origin");
//
//        // Specifies the number of sites to skip as the rectangle is built in
//        // each direction. 1 means a solid rectangle.  May be negative.
//        Element displacementElem = siteElement.element("displacement");
//
//        // Specifies the total offset in each direction.
//        Element dimensionsElem = siteElement.element("extension");
//
//        boolean xyCorrection = siteElement.element("xy-correction") != null;
//
//        // Use floating point so that w can build in offset corrections for
//        // a particular layerManager.getCellLayer().getGeometry().try.
//        double x0 = Double.valueOf(originElem.attribute("x").getValue());
//        double y0 = Double.valueOf(originElem.attribute("y").getValue());
//
//        double dx = Double.valueOf(displacementElem.attribute("dx").getValue());
//        double dy = Double.valueOf(displacementElem.attribute("dy").getValue());
//
//        double w = Double.valueOf(dimensionsElem.attribute("x").getValue());
//        double l = Double.valueOf(dimensionsElem.attribute("y").getValue());
//
//        // 3D case
//        if (originElem.attribute("z") != null) {
//            double z0 = Double.valueOf(originElem.attribute("z").getValue());
//            double h = Double.valueOf(dimensionsElem.attribute("z").getValue());
//            double dz = Double.valueOf(displacementElem.attribute("dz").getValue());
//
//            for (double x = x0; x < x0 + w; x += dx) {
//                for (double y = y0; y < y0 + l; y += dy) {
//                    for (double z = z0; z < z0 + h; z += dz) {
//
//                        int xInt = (int) Math.round(Math.floor(x));
//                        int yInt = (int) Math.round(Math.floor(y));
//                        int zInt = (int) Math.round(Math.floor(z));
//
//                        if (xyCorrection) {
//                            yInt += xInt / 2;
//                        }
//
//                        Coordinate c = new Coordinate(xInt, yInt, zInt, 0);
//                        coordinates.add(c);
//                    }
//                }
//            }
//
//            // 2D case
//        } else {
//            for (double x = x0; x < x0 + w; x += dx) {
//                for (double y = y0; y < y0 + l; y += dy) {
//                    int xInt = (int) Math.round(Math.floor(x));
//                    int yInt = (int) Math.round(Math.floor(y));
//
//                    if (xyCorrection) {
//                        yInt += xInt / 2;
//                    }
//
//                    Coordinate c = new Coordinate(xInt, yInt, 0);
//                    coordinates.add(c);
//                }
//            }
//        }
//
//        return coordinates.toArray(new Coordinate[0]);
//
//    }

}
