package io.serialize;

import java.io.BufferedWriter;

import geometry.Geometry;
import structural.GeneralParameters;
import layers.cell.CellLayer; 
import structural.halt.FixationEvent;
import structural.halt.HaltCondition;
import structural.halt.StepMaxReachedEvent;
import structural.identifiers.Coordinate;

public class FixationTimeWriter extends AbstractCellWriter {

	private boolean closed = true;
	private static final String FILENAME = "ttf.txt";
	private BufferedWriter bw;

	public FixationTimeWriter(GeneralParameters p, Geometry geometry) {
		super(p, geometry);

		// We use the non-instance path because this metric aggregates over
		// all instances.
		String filename = p.getPath() + '/' + FILENAME;
		mkDir(p.getInstancePath(), true);
		bw = makeBufferedWriter(filename);
		
	
		hAppend(bw, new StringBuilder("gillespie\tfix_state\n"));
	}

	@Override
	public void init(CellLayer l) {
		if (!closed) {
			throw new IllegalStateException("Attempted to initialize active writer.");
		}
		closed = false;
		layer = l;


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
