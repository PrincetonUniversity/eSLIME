package io;

import java.util.HashMap;

import structural.VectorViewer;
import structural.identifiers.Coordinate;
import control.Parameters;
import geometries.*;

public class ConditionViewer {

	private VectorViewer f;
	private int[] states;
	private double gCurrent;
	private HashMap<Coordinate, Integer> coordMap;
	
	public ConditionViewer(VectorViewer f, int[] states, double gCurrent,
			HashMap<Coordinate, Integer> coordMap) {
		
		this.f = f;
		this.states = states;
		this.gCurrent = gCurrent;
		this.coordMap = coordMap;
	}

	public int getState(Coordinate c) {
		int i = coordMap.get(c);
		
		return states[i];
	}
	
	public double getFitness(Coordinate c) {
		int i = coordMap.get(c);
		
		return f.get(i);
	}
	
	public Coordinate[] getHighlight() {
		throw new UnsupportedOperationException();
	}

	public boolean isVacant(Coordinate c) {
		int i = coordMap.get(c);
		
		return (states[i] == 0);
	}
	
	public double getGillespie() {
		return gCurrent;
	}

}
