/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.layers.cell;

import factory.geometry.GeometryFactory;
import geometry.Geometry;
import layers.cell.CellLayer;
import org.dom4j.Element;

/**
 * Created by dbborens on 7/25/14.
 */
public class CellLayerFactory {
    public static CellLayer instantiate(Element e, GeometryFactory factory) {
        Geometry geometry = factory.make(e);
        CellLayer layer = new CellLayer(geometry);
        return layer;
    }
}
