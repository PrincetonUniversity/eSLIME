package jeslime.mock;

import java.util.Map;

import cells.Cell;
import structural.identifiers.Coordinate;
import geometries.Geometry;
import layers.cell.CellLayerContent;
import layers.cell.CellLayerIndices;

public class MockCellLayerContent extends CellLayerContent {

	public MockCellLayerContent(Geometry geom, CellLayerIndices indices) {
		super(geom, indices);
	}

	public Cell get(Coordinate coord) {
		// Mock getter doesn't do any validation
		return map.get(coord);
	}
	
	/* stateVector */
	
	private int[] stateVector;
	
	public void setStateVector(int[] stateVector) {
		this.stateVector = stateVector; 
	}
	
	@Override
	public int[] getStateVector() {
		return stateVector;
	}
	
	/* fitnessVector */
	
	private double[] fitnessVector;
	
	public void setFitnessVector(double[] fitnessVector) {
		this.fitnessVector = fitnessVector; 
	}
	
	@Override
	public double[] getFitnessVector() {
		return fitnessVector;
	}
}
