package io.serialize;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import geometries.Geometry;
import structural.GeneralParameters;
import structural.Lattice;
import structural.identifiers.Coordinate;

/**
 * Writes out a histogram of states by frame. Note that this
 * metric requires aggregation, and for this reason should
 * not be used to make a recording at every frame.
 * 
 * @author dbborens
 *
 */
public class StateHistogram extends TemporalMetricWriter {

	private static final String FILENAME = "histo.txt";
	private BufferedWriter bw;

	ArrayList<Integer> frames = new ArrayList<Integer>();
	
	// The keys to this map are FRAMES. The values are a mapping from STATE
	// number to count. If a state number does not appear, that means the
	// count was zero at that time.
	HashMap<Integer, HashMap<Integer, Integer>> histo;
	
	HashSet<Integer> observedStates = new HashSet<Integer>();
	
	public StateHistogram(GeneralParameters p, Lattice lattice, Geometry geometry) {
		super(p, lattice, geometry);
		
		histo = new HashMap<Integer, HashMap<Integer, Integer>>();
	}

	@Override
	public void init() {
		String filename = p.getInstancePath() + '/' + FILENAME;
		mkDir(p.getInstancePath(), true);
		bw = makeBufferedWriter(filename);
	}

	@Override
	public void push(Coordinate[] highlights, double gillespie, int frame) {
		frames.add(frame);
		
		Set<Coordinate> sites = lattice.getOccupiedSites();

		for (Coordinate site : sites) {

			Integer state = lattice.getState(site);
			
			// Add the state to the set of observed states, if needed
			observedStates.add(state);	
			
			// If we've never seen this FRAME, create a bucket for it.
			if (!histo.containsKey(frame)) {
				HashMap<Integer, Integer> observations = new HashMap<Integer, Integer>();
				histo.put(frame, observations);
			}
			
			// Retrieve the histogram bucket for this FRAME.
			HashMap<Integer, Integer> observations = histo.get(frame);
			
			// If this is the first observation for the current state in this frame, 
			// create the KVP.
			if (!observations.containsKey(state)) {
				observations.put(state, 0);
			}
			
			// Get current count of this state for this frame
			Integer current = observations.get(state);
			
			// Increment the count by one individual
			observations.put(state, current + 1);
			
		}
		
	}

	@Override
	public void close() {
	
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
		
		for (Integer frame : histo.keySet()) {
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

}
