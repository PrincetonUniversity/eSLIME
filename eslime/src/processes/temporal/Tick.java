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

package processes.temporal;

import io.project.ProcessLoader;
import layers.LayerManager;
import processes.StepState;
import structural.GeneralParameters;
import structural.halt.HaltCondition;

/**
 * Advances the clock by a constant dt.
 *
 * @author dbborens
 */
public class Tick extends TimeProcess {

    private double dt;

    public Tick(ProcessLoader loader, LayerManager layerManager, int id,
                GeneralParameters p) {

        super(loader, layerManager, id, p);

        dt = Double.valueOf(get("dt"));
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        state.advanceClock(dt);
    }

}
