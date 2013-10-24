package layers.cell;

import java.util.HashSet;

import cells.Cell;
import structural.identifiers.Coordinate;

/**
 * 
 * @tested CellLayerViewerTest
 * @author David Bruce Borenstein
 *
 */
public class CellLayerViewer {
	
	private CellLayer layer;
	private CellLayerContent content;
	private CellLayerIndices indices;

	public CellLayerViewer (CellLayer layer, CellLayerContent content, CellLayerIndices indices) {
		this.layer = layer;
		this.content = content;
		this.indices = indices;
	}
	
	/**
	 * Returns a vector containing the canonical coordinate of each
	 * active site on the lattice.
	 * @return
	 */
	public HashSet<Coordinate> getOccupiedSites() {
		// Construct a copy of internal state
		HashSet<Coordinate> res = new HashSet<Coordinate>(indices.getOccupiedSites());

		// Return it
		return res;
	}
	
	/**
	 * Returns a list of divisible sites on the lattice.
	 * 
	 * @return
	 */
	public HashSet<Coordinate> getDivisibleSites() {
		// Construct a copy of internal state
		HashSet<Coordinate> res = new HashSet<Coordinate>(indices.getDivisibleSites());

		// Return it
		return res;
	}

	public Cell getCell(Coordinate coord) {
		return content.get(coord);
	}
	
	public int[] getStateVector() {
		return content.getStateVector();
	}
	
	public double[] getFitnessVector() {
		return content.getFitnessVector();
	}
	
	public StateMapViewer getStateMapViewer() {
		return new StateMapViewer(indices.getStateMap());
	}

	public boolean isOccupied(Coordinate c) {
		return indices.isOccupied(c);
	}
	
	public boolean isDivisible(Coordinate c) {
		return indices.isDivisible(c);
	}
}
