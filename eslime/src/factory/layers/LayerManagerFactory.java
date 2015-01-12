/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.layers;

import control.arguments.GeometryDescriptor;
import factory.layers.cell.CellLayerFactory;
import layers.LayerManager;
import layers.cell.CellLayer;
import org.dom4j.Element;

import java.util.List;

/**
 * Created by dbborens on 11/26/14.
 */
public abstract class LayerManagerFactory {

    public static LayerManager instantiate(Element root, GeometryDescriptor geometryDescriptor) {
        LayerManager ret = new LayerManager();
        // Build the Cell layer, if present
        if (hasCellElement(root)) {
            CellLayer cellLayer = buildCellLayer(root, geometryDescriptor);
            ret.setCellLayer(cellLayer);
        }
        return ret;

    }

    private static CellLayer buildCellLayer(Element layerRoot, GeometryDescriptor geometryDescriptor) {
        Element e = layerRoot.element("cell-layer");
        return CellLayerFactory.instantiate(e, geometryDescriptor);
    }

    private static boolean hasCellElement(Element layerRoot) {
        List<Object> elems = layerRoot.elements("cell-layer");
        return (elems.size() > 0);
    }
}
