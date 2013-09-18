package io.deserialize;

import java.util.HashMap;
import java.util.HashSet;

import structural.GeneralParameters;
import structural.VectorViewer;
import structural.identifiers.Coordinate;
import geometries.*;

public class ConditionViewer {

	private VectorViewer f;
	private int[] states;
	private double gCurrent;
	private int frame;
	private HashSet<Coordinate> highlights;
	private HashMap<Coordinate, Integer> coordMap;
	
	public ConditionViewer(VectorViewer f, int[] states, HashSet<Coordinate> highlights,
			int frame, double gCurrent, HashMap<Coordinate, Integer> coordMap) {
		
		this.f = f;
		this.states = states;
		this.gCurrent = gCurrent;
		this.coordMap = coordMap;
		this.frame = frame;
		this.highlights = highlights;
	}

	public int getState(Coordinate c) {
		int i = coordMap.get(c);
		
		return states[i];
	}
	
	public double getFitness(Coordinate c) {
		int i = coordMap.get(c);
		
		return f.get(i);
	}
	
	public HashSet<Coordinate> getHighlights() {
		return highlights;
	}

	public boolean isVacant(Coordinate c) {
		int i = coordMap.get(c);
		
		return (states[i] == 0);
	}
	
	public double getGillespie() {
		return gCurrent;
	}
	
	public int getFrame() {
		return frame;
	}

}
