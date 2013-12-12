package io.deserialize;

import java.util.HashMap;
import java.util.HashSet;

import structural.VectorViewer;
import structural.identifiers.Coordinate;

public class ConditionViewer {

	private VectorViewer f;
	private int[] states;
	private double gCurrent;
	private int frame;
	private HashSet<Coordinate> highlights;
	private CoordinateDeindexer deindexer;
	
	public ConditionViewer(VectorViewer f, int[] states, HashSet<Coordinate> highlights,
			int frame, double gCurrent, CoordinateDeindexer deindexer) {
		
		this.f = f;
		this.states = states;
		this.gCurrent = gCurrent;
		this.deindexer = deindexer;
		this.frame = frame;
		this.highlights = highlights;
	}

	public int getState(Coordinate c) {
		int i = deindexer.getIndex(c);
		
		return states[i];
	}
	
	public double getFitness(Coordinate c) {
		int i = deindexer.getIndex(c);
		
		return f.get(i);
	}
	
	public HashSet<Coordinate> getHighlights() {
		return highlights;
	}

	public boolean isVacant(Coordinate c) {
		int i = deindexer.getIndex(c);
		
		return (states[i] == 0);
	}
	
	public double getGillespie() {
		return gCurrent;
	}
	
	public int getFrame() {
		return frame;
	}

}
