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

package factory.layers;

import factory.geometry.GeometryFactory;
import factory.layers.cell.CellLayerFactory;
import factory.layers.solute.SoluteLayerFactory;
import layers.LayerManager;
import layers.cell.CellLayer;
import layers.solute.SoluteLayer;
import org.dom4j.Element;

import java.util.List;

/**
 * Created by dbborens on 7/25/14.
 */
public abstract class LayerManagerFactory {

    public static LayerManager instantiate(Element layerRoot, GeometryFactory factory) {
        LayerManager ret = new LayerManager();
        // Build the Cell layer, if present
        if (hasCellElement(layerRoot)) {
            CellLayer cellLayer = buildCellLayer(layerRoot, factory);
            ret.setCellLayer(cellLayer);
        }

        // Build solute layers, if present
        List<Object> slElemObjs = layerRoot.elements("solute-layer");
        for (Object o : slElemObjs) {
            Element e = (Element) o;
            initSoluteLayer(e, factory, ret);
        }

        return ret;
    }

    private static CellLayer buildCellLayer(Element layerRoot, GeometryFactory factory) {
        Element e = layerRoot.element("cell-layer");
        return CellLayerFactory.instantiate(e, factory);
    }

    private static void initSoluteLayer(Element e, GeometryFactory factory, LayerManager ret) {
        String id = e.element("id").getTextTrim();
        SoluteLayer layer = SoluteLayerFactory.instantiate(e, factory, ret);
        ret.addSoluteLayer(id, layer);
    }

    private static boolean hasCellElement(Element layerRoot) {
        List<Object> elems = layerRoot.elements("cell-layer");
        return (elems.size() > 0);
    }

}
