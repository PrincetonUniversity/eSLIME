/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import cells.Cell;
import control.GeneralParameters;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import factory.cell.CellFactory;
import geometry.set.CoordinateSet;
import io.loader.ProcessLoader;
import layers.LayerManager;
import org.dom4j.Element;
import processes.StepState;
import processes.gillespie.GillespieState;
import structural.utilities.XmlUtil;

/**
 * Fills in all sites in the active site set
 * with cells of the type specified by the
 * process' cell descriptor. Does not throw
 * LatticeFullExceptions.
 *
 * @author dbborens
 */
public class Fill extends CellProcess {

    // If true, the process will skip over any already-filled sites. If
    // false, it will blow up if it encounters an already-filled site
    // that it expected to fill.
    boolean skipFilled;

    public Fill(ProcessLoader loader, LayerManager layerManager, CoordinateSet activeSites, int id,
                GeneralParameters p) {
        super(loader, layerManager, activeSites, id, p);

        Element e = loader.getProcess(id);
        skipFilled = XmlUtil.getBoolean(e, "skip-filled-sites");
    }

    public void target(GillespieState gs) throws HaltCondition {
        // This process only has one event: it affects all relevant cells.
        if (gs != null) {
            gs.add(getID(), 1, 1D);
        }
    }

    public void fire(StepState state) throws HaltCondition {
        CellFactory factory = getCellFactory(layerManager);

        for (Coordinate c : activeSites) {
            boolean filled = layer.getViewer().isOccupied(c);

            if (filled && !skipFilled) {
                String msg = "Attempted to fill coordinate " + c.toString() +
                        " but it was already filled. This is illegal unless" +
                        " the <skip-filled-sites> flag is set to true. Did you" +
                        " mean to set it? (id=" + getID() + ")";

                throw new IllegalStateException(msg);
            } else if (!filled) {
                Cell cell = factory.instantiate();
                layer.getUpdateManager().place(cell, c);
            } else {
                // Do nothing if site is filled and skipFilled is true.
            }
        }
    }
}
