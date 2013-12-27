package layers.cell;

import cells.Cell;
import layers.cell.CellIndex;
import layers.cell.CellLayerIndices;
import structural.identifiers.Coordinate;

public class MockCellLayerIndices extends CellLayerIndices {

	private CellIndex occupied = new CellIndex();
	private CellIndex divisible = new CellIndex();
	
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
	
	public void setOccupiedSites(CellIndex occupied) {
		this.occupied = occupied;
	}
	
	public void setDivisibleSites(CellIndex divisible) {
		this.divisible = divisible;
	}
	
	public boolean isOccupied(Coordinate k) {
		return occupied.contains(k);
	}
	
	public boolean isDivisible(Coordinate k) {
		return divisible.contains(k);
	}
	
	public CellIndex getOccupiedSites() {
		return occupied;
	}
	
	public CellIndex getDivisibleSites() {
		return divisible;
	}
	
	public void decrStateCount(Cell cell) {
		if (!stateMap.containsKey(cell.getState())) {
			stateMap.put(cell.getState(), 0);
		}
		
		super.decrStateCount(cell);
	}
}
