/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers;

import layers.cell.CellLayer;
import layers.continuum.ContinuumAgentLinker;
import layers.continuum.ContinuumLayer;
import processes.StepState;

import java.util.HashMap;

/**
 * Created by David B Borenstein on 12/29/13.
 */
public class LayerManager {

    protected CellLayer cellLayer;

    protected HashMap<String, ContinuumLayer> continuumLayers;
    private StepState stepState;

    public LayerManager() {
        continuumLayers = new HashMap<>();
    }

    public void addContinuumLayer(String id, ContinuumLayer continuumLayer) {
        continuumLayers.put(id, continuumLayer);
    }

    public boolean hasCellLayer() {
        return (cellLayer != null);
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

        if (continuumLayers.size() != that.continuumLayers.size()) {
            return false;
        }

        for (String s : continuumLayers.keySet()) {
            if (!that.continuumLayers.containsKey(s)) {
                return false;
            }

            ContinuumLayer p = continuumLayers.get(s);
            ContinuumLayer q = that.continuumLayers.get(s);
            if (!p.equals(q)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = cellLayer != null ? cellLayer.hashCode() : 0;
        result = 31 * result + (continuumLayers != null ? continuumLayers.hashCode() : 0);
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

    /**
     * Returns a linker through which agents can retrieve the state of a
     * continuum at their location, or notify the field of their birth or
     * death.
     *
     * @param id
     * @return
     */
    public ContinuumAgentLinker getLinker(String id) {
        ContinuumLayer layer = continuumLayers.get(id);
        return layer.getLinker();
    }
}
