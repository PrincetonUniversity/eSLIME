package jeslime.mock;

import java.util.HashSet;

import cells.Cell;
import layers.cell.CellLayerIndices;
import structural.identifiers.Coordinate;

public class MockCellLayerIndices extends CellLayerIndices {

	private HashSet<Coordinate> occupied = new HashSet<Coordinate>();
	private HashSet<Coordinate> divisible = new HashSet<Coordinate>();
	
	public void setOccupied(Coordinate k, Boolean v) {
		if (v)
			occupied.add(k);
		else
			occupied.remove(k);
	}
	
	public void setDivisible(Coordinate k, Boolean v) {
		if (v)
			divisible.add(k);
		else
			divisible.remove(k);	}
	
	public void setOccupiedSites(HashSet<Coordinate> occupied) {
		this.occupied = occupied;
	}
	
	public void setDivisibleSites(HashSet<Coordinate> divisible) {
		this.divisible = divisible;
	}
	
	public boolean isOccupied(Coordinate k) {
		return occupied.contains(k);
	}
	
	public boolean isDivisible(Coordinate k) {
		return divisible.contains(k);
	}
	
	public HashSet<Coordinate> getOccupiedSites() {
		return occupied;
	}
	
	public HashSet<Coordinate> getDivisibleSites() {
		return divisible;
	}
	
	public void decrStateCount(Cell cell) {
		if (!stateMap.containsKey(cell.getState())) {
			stateMap.put(cell.getState(), 0);
		}
		
		super.decrStateCount(cell);
	}
}
