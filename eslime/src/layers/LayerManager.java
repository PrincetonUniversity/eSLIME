/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers;

import layers.cell.CellLayer;
import layers.solute.SoluteLayer;
import processes.StepState;

import java.util.HashMap;

/**
 * Created by David B Borenstein on 12/29/13.
 */
public class LayerManager {

//    private static final int GEOMETRY_ID = 0;
    protected CellLayer cellLayer;
    protected HashMap<String, SoluteLayer> soluteLayers;
    private StepState stepState;

    public LayerManager() {
        soluteLayers = new HashMap<>();
    }

    public void addSoluteLayer(String id, SoluteLayer soluteLayer) {
        soluteLayers.put(id, soluteLayer);
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

    public void setCellLayer(CellLayer cellLayer) {
        this.cellLayer = cellLayer;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LayerManager)) {
            return false;
        }

        LayerManager that = (LayerManager) o;

        if (cellLayer != null ? !cellLayer.equals(that.cellLayer) : that.cellLayer != null)
            return false;

        if (soluteLayers.size() != that.soluteLayers.size()) {
            return false;
        }

        for (String s : soluteLayers.keySet()) {
            if (!that.soluteLayers.containsKey(s)) {
                return false;
            }

            SoluteLayer p = soluteLayers.get(s);
            SoluteLayer q = that.soluteLayers.get(s);
            if (!p.equals(q)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = cellLayer != null ? cellLayer.hashCode() : 0;
        result = 31 * result + (soluteLayers != null ? soluteLayers.hashCode() : 0);
        return result;
    }

    public StepState getStepState() {
        return stepState;
    }

    public void setStepState(StepState stepState) {
        this.stepState = stepState;
    }

    public void reset() {
        cellLayer.reset();
    }
}
