/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package processes.temporal;

import layers.LayerManager;
import processes.Process;
import processes.gillespie.GillespieState;
import geometry.Geometry;
import io.project.ProcessLoader;
import structural.GeneralParameters;
import layers.cell.CellLayer;

/**
 * Convenience superclass for time-specific processes,
 * including some methods for calculating elapsed time.
 * Probably not necessary to have a separate superclass
 * for this.
 * 
 * At the moment, TimeProcess is specific to a particular
 * cell layer.
 * 
 * @untested
 * @author David Bruce Borenstein
 *
 */
public abstract class TimeProcess extends Process {
	protected GeneralParameters p;
	
	public TimeProcess(ProcessLoader loader, LayerManager layerManager, int id,
			GeneralParameters p) {
		
		super(loader, layerManager, p, id);
		
		this.p = p;
	}
	
	protected String getProcessClass() {
		return this.getClass().getSimpleName();
	}

	public void target(GillespieState gs) {
		// There's only one event that can happen--we update.
		if (gs != null) {
			gs.add(this.getID(), 1, 0.0D);	
		}
	}
	
	/**
	 * Returns an exponentially distributed random number.
	 */
	protected double expRandom(double lambda) {
		// Get a random number between 0 (inc) and 1 (exc)
		double u = p.getRandom().nextDouble();
		
		// Inverse of exponential CDF
		return Math.log(1 - u) / (-1 * lambda);
	}
}
