/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import control.arguments.Argument;
import geometry.set.CoordinateSet;
import layers.cell.CellLayer;
import processes.BaseProcessArguments;
import processes.EcoProcess;

public abstract class CellProcess extends EcoProcess {
    // These are easily accessed from the layer manager, but there
    // are very many calls to them thanks to some legacy code.
    protected CellLayer layer;
    protected CoordinateSet activeSites;
    protected Argument<Integer> maxTargets;

    public CellProcess(BaseProcessArguments arguments, CellProcessArguments cpArguments) {
        super(arguments);
        layer = layerManager.getCellLayer();
        activeSites = cpArguments.getActiveSites();
        maxTargets = cpArguments.getMaxTargets();
    }
}
