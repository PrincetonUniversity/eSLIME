package io.serialize;

import geometry.Geometry;
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
public class ProgressReporter extends AbstractCellWriter {

	public ProgressReporter(GeneralParameters p, Geometry geometry) {
		super(p, geometry);
	}

	@Override
	public void init(CellLayer l) {
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
