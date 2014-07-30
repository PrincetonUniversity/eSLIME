/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import control.GeneralParameters;
import geometry.set.CoordinateSet;
import io.loader.ProcessLoader;
import layers.LayerManager;
import layers.cell.CellLayer;
import processes.Process;

public abstract class CellProcess extends Process {
    // These are easily accessed from the layer manager, but there
    // are very many calls to them thanks to some legacy code.
    protected CellLayer layer;
    protected CoordinateSet activeSites;

    public CellProcess(ProcessLoader loader, LayerManager layerManager, CoordinateSet activeSites, int id,
                       GeneralParameters p) {


        super(loader, layerManager, p, id);
        this.activeSites = activeSites;
        layer = layerManager.getCellLayer();
    }

    protected String getProcessClass() {
        return this.getClass().getSimpleName();
    }
}
