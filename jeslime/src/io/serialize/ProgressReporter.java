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

package io.serialize;

import geometry.Geometry;
import io.project.GeometryManager;
import layers.LayerManager;
import structural.GeneralParameters;
import layers.cell.CellLayer; import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

/**
 * Outputs some basic information about simulation progress.
 * Without this (or similar), eSLIME does not write to stdout.
 * 
 * @untested
 * @author David Bruce Borenstein
 *
 */
public class ProgressReporter extends Serializer {

	public ProgressReporter(GeneralParameters p) {
		super(p);
	}

	public void init(LayerManager lm) {
		System.out.println("Instance " + p.getInstance() + ": " + p.getInstancePath());
	}

	@Override
	public void step(Coordinate[] highlights, double gillespie, int frame) {
		if (p.isFrame(frame)) {
			System.out.println("   Frame " + frame + ".");
		}
	}

	@Override
	public void dispatchHalt(HaltCondition ex) {
		System.out.println("  Instance halted. Cause type: " + ex.getClass().getSimpleName());

	}

	@Override
	public void close() {
		System.out.println(" " + p.getBasePath());
		System.out.println("Project concluded.");
	}

}
