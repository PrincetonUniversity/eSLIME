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

import java.io.BufferedWriter;

import geometry.Geometry;
import io.project.GeometryManager;
import layers.LayerManager;
import structural.GeneralParameters;
import layers.cell.CellLayer; 
import structural.halt.FixationEvent;
import structural.halt.HaltCondition;
import structural.halt.StepMaxReachedEvent;
import structural.identifiers.Coordinate;

public class FixationTimeWriter extends Serializer {

	private boolean closed = true;
	private static final String FILENAME = "ttf.txt";
	private BufferedWriter bw;

	public FixationTimeWriter(GeneralParameters p) {
		super(p);

		// We use the non-instance path because this metric aggregates over
		// all instances.
		String filename = p.getPath() + '/' + FILENAME;
		mkDir(p.getInstancePath(), true);
		bw = makeBufferedWriter(filename);

		hAppend(bw, new StringBuilder("gillespie\tfix_state\n"));
	}

	@Override
	public void step(Coordinate[] highlights, double gillespie, int frame) {
		
	}

	@Override
	public void close() {
		closed = true;
		hClose(bw);
	}

	@Override
	public void dispatchHalt(HaltCondition ex) {
		StringBuilder line;
		if (ex.getClass().equals(FixationEvent.class)) {
			FixationEvent fix = (FixationEvent) ex;
			line = fixationLine(fix);
		} else if (ex.getClass().equals(StepMaxReachedEvent.class)) {
			line = maxStepLine(ex);
		} else {
			line = errorLine(ex);
		}
		
		hAppend(bw, line);
		
		closed = true;
	}

	private StringBuilder errorLine(HaltCondition ex) {
		StringBuilder sb = new StringBuilder();
		sb.append(ex.getGillespie());
		sb.append("\tERR\n");
		return sb;
	}

	private StringBuilder maxStepLine(HaltCondition ex) {
		StringBuilder sb = new StringBuilder();
		sb.append(ex.getGillespie());
		sb.append("\tMAX\n");
		return sb;
	}

	private StringBuilder fixationLine(FixationEvent fix) {
		StringBuilder sb = new StringBuilder();
		sb.append(fix.getGillespie());
		sb.append("\t");
		sb.append(fix.getFixationState());
		sb.append("\n");
		return sb;
	}

}
