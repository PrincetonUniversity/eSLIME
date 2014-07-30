/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers;

import layers.cell.CellLayer;
import layers.solute.SoluteLayer;

import java.util.HashMap;

/**
 * Created by dbborens on 12/31/13.
 */
public class MockLayerManager extends LayerManager {
    public MockLayerManager() {
        super();
        soluteLayers = new HashMap<>();
    }

    public CellLayer getCellLayer() {
        return cellLayer;
    }

    public void setCellLayer(CellLayer cellLayer) {
        this.cellLayer = cellLayer;
    }

    public void addSoluteLayer(String s, SoluteLayer layer) {
        soluteLayers.put(s, layer);
    }

    public SoluteLayer getSoluteLayer(String s) {
        return soluteLayers.get(s);
    }

    public SoluteLayer[] getSoluteLayers() {
        return soluteLayers.values().toArray(new SoluteLayer[0]);
    }
}
