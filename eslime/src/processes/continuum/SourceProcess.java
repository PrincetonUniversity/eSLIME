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
import processes.StepState;
import structural.GeneralParameters;
import structural.halt.HaltCondition;

/**
 * Created by David B Borenstein on 1/7/14.
 */
@SuppressWarnings("unused")
public class SourceProcess extends ContinuumProcess {

    @Override
    public void fire(StepState state) throws HaltCondition {
        throw new UnsupportedOperationException("This class won't work until after you implement source/sink active management");
    }

    public SourceProcess(ProcessLoader loader, LayerManager layerManager, GeneralParameters p, int id) {
        super(loader, layerManager, p, id);
    }
}
