/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.layers.cell;

import control.arguments.GeometryDescriptor;
import factory.geometry.boundaries.BoundaryFactory;
import geometry.Geometry;
import geometry.boundaries.Boundary;
import layers.cell.CellLayer;
import org.dom4j.Element;

/**
 * Created by dbborens on 7/25/14.
 */
public class CellLayerFactory {
    public static CellLayer instantiate(Element root, GeometryDescriptor geometryDescriptor) {
        Boundary boundary = makeBoundary(root, geometryDescriptor);
        Geometry geometry = geometryDescriptor.make(boundary);
        CellLayer layer = new CellLayer(geometry);
        return layer;
    }

    private static Boundary makeBoundary(Element root, GeometryDescriptor geometryDescriptor) {
        Element boundaryElem = root.element("boundary");
        Boundary boundary = BoundaryFactory.instantiate(boundaryElem, geometryDescriptor);
        return boundary;
    }
}
