package io.serialize;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import geometry.Geometry;
import io.project.GeometryManager;
import structural.GeneralParameters;
import layers.cell.CellLayer; import layers.cell.StateMapViewer;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

/**
 * Writes out a histogram of states by frame. Note that this
 * metric requires aggregation, and for this reason should
 * not be used to make a recording at every frame.
 * 
 * @author dbborens
 *
 */
public class FrequencyWriter extends AbstractCellWriter {

	private boolean closed = true;
	
	private static final String FILENAME = "histo.txt";
	private BufferedWriter bw;

	ArrayList<Integer> frames = new ArrayList<Integer>();
	
	// The keys to this map are FRAMES. The values are a mapping from STATE
	// number to count. If a state number does not appear, that means the
	// count was zero at that time.
	HashMap<Integer, HashMap<Integer, Integer>> histo;
	
	HashSet<Integer> observedStates = new HashSet<Integer>();
	
	public FrequencyWriter(GeneralParameters p, GeometryManager gm) {
		super(p, geometry);
		
		histo = new HashMap<Integer, HashMap<Integer, Integer>>();
	}

	@Override
	public void init(CellLayer l) {
		if (!closed) {
			throw new IllegalStateException("Attempted to initialize active writer.");
		}
		closed = false;
		layer = l;

		String filename = p.getInstancePath() + '/' + FILENAME;
		mkDir(p.getInstancePath(), true);
		bw = makeBufferedWriter(filename);
	}

	@Override
	public void step(Coordinate[] highlights, double gillespie, int frame) {
		
		if (p.isFrame(frame)) {
			frames.add(frame);
			
			// Create a bucket for this frame.
			HashMap<Integer, Integer> observations = new HashMap<Integer, Integer>();
			histo.put(frame, observations);
			
			// Iterate over all observed states for this frame.
			StateMapViewer smv = layer.getViewer().getStateMapViewer();
			for (Integer state : smv.getStates()) {
				Integer count = smv.getCount(state);
				observations.put(state, count);
				observedStates.add(state);
			}
		}
	}

	public void dispatchHalt(HaltCondition ex) {
		conclude();
		closed = true;
	}
	
	private void conclude() {
		// Sort the states numerically
		TreeSet<Integer> sortedStates = new TreeSet<Integer>(observedStates);
		
		// Write out the header
		StringBuilder line = new StringBuilder();
		line.append("frame");
		
		for (Integer state : sortedStates) {
			line.append("\t");
			line.append(state);
		}
		
		line.append("\n");
		
		hAppend(bw, line);
		
		TreeSet<Integer> sortedFrames = new TreeSet<Integer>(histo.keySet());
		for (Integer frame : sortedFrames) {
			HashMap<Integer, Integer> observations = histo.get(frame);
			
			line = new StringBuilder();
			line.append(frame);
			
			for (Integer state : sortedStates) {
				line.append("\t");
				
				if (observations.containsKey(state)) {
					line.append(observations.get(state));
				} else {
					line.append("0");
				}
			}
			
			line.append("\n");
			hAppend(bw, line);
			
		}
		hClose(bw);

	}

	public void close() {
		// Doesn't do anything.
	}
}
