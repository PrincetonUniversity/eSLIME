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

package processes.continuum;

import io.project.ProcessLoader;
import layers.LayerManager;
import processes.Process;
import processes.gillespie.GillespieState;
import structural.GeneralParameters;
import structural.halt.HaltCondition;

/**
 * Created by David B Borenstein on 1/7/14.
 */
public abstract class ContinuumProcess extends Process {

    public ContinuumProcess(ProcessLoader loader, LayerManager layerManager, GeneralParameters p,
                            int id) {
        super(loader, layerManager, p, id);
    }

    protected String getProcessClass() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {
        // There's only one event that can happen--we update.
        if (gs != null) {
            gs.add(this.getID(), 1, 0.0D);
        }
    }
}
