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

package layers;

import geometry.Geometry;
import io.project.GeometryManager;
import layers.cell.CellLayer;
import layers.solute.SoluteLayer;
import layers.solute.SoluteLayerFactory;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;

/**
 * Created by David B Borenstein on 12/29/13.
 */
public class LayerManager {

    private static final int GEOMETRY_ID = 0;
    private CellLayer cellLayer;
    private HashMap<String, SoluteLayer> soluteLayers;

    public LayerManager() {
        // Null default constructor, used in mock testing
    }

    public LayerManager(Element layerRoot, GeometryManager factory) {
        // Build the Cell layer, if present
        if (hasCellElement(layerRoot)) {
            cellLayer = buildCellLayer(layerRoot, factory);
        } else {
            cellLayer = null;
        }

        // Build solute layers, if present
        List<Object> slElemObjs = layerRoot.elements("solute-layer");
        soluteLayers = new HashMap<>();
        for (Object o : slElemObjs) {
            Element e = (Element) o;
            initSoluteLayer(e, factory);
        }
    }

    private void initSoluteLayer(Element e, GeometryManager factory) {
        String id = e.element("id").getTextTrim();
        SoluteLayer layer = SoluteLayerFactory.instantiate(e, factory, this);
        soluteLayers.put(id, layer);
    }

    private boolean hasCellElement(Element layerRoot) {
        List<Object> elems = layerRoot.elements("cell-layer");
        return (elems.size() > 0);
    }

    private CellLayer buildCellLayer(Element layerRoot, GeometryManager factory) {
        Element e = layerRoot.element("cell-layer");
        Geometry geometry = factory.make(e);
        CellLayer layer = new CellLayer(geometry, GEOMETRY_ID);
        return layer;
    }

    public boolean hasCellLayer() {
        return (cellLayer != null);
    }

    public String[] getSoluteLayerIds() {
        return soluteLayers.keySet().toArray(new String[0]);
    }

    public SoluteLayer[] getSoluteLayers() {
        return soluteLayers.values().toArray(new SoluteLayer[0]);
    }

    public SoluteLayer getSoluteLayer(String id) {
        return soluteLayers.get(id);
    }

    public CellLayer getCellLayer() {
        return cellLayer;
    }
}
