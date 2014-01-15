package layers.cell;

import geometry.Geometry;

import java.util.HashMap;

import structural.Flags;
import structural.identifiers.Coordinate;
import cells.Cell;

/**
 * 
 * @test CellLayerContentTest
 * @author David Bruce Borenstein
 *
 */
public class CellLayerContent {
	
	// Mapping of sites to cell pointers -- the lattice itself.		
	protected HashMap<Coordinate, Cell> map;
	
	// All sites (basically the keyset for the lattice map). 
	protected Coordinate[] canonicalSites;
	
	protected Geometry geom;
	protected CellLayerIndices indices;
	
	public CellLayerContent(Geometry geom, CellLayerIndices indices) {
		// Assign geometry.
		this.geom = geom;
		this.indices = indices;
		// Get canonical site list.
		canonicalSites = geom.getCanonicalSites();
		
		// Initialize map.
		map = new HashMap<>();
	    for (int i = 0; i < canonicalSites.length; i++) {
			Coordinate coord = canonicalSites[i];

			// Initialize each site to null (empty)
			map.put(coord, null);	
		}
	}

	public Coordinate checkExists(Coordinate coord) {
		// First check to see if this cell is supposed to be retained even though
		// it is not a "real" coordinate.
		if (geom.isInfinite() && coord.hasFlag(Flags.END_OF_WORLD)) {
			return coord;
		}
		
		Coordinate canonical = coord.canonicalize();

		// Otherwise, it had better be in the coordinate system.
		if (!map.containsKey(canonical)) {
			StringBuilder ss = new StringBuilder();
			ss.append("Consistency failure: coordinate ");
			ss.append(coord.stringForm());
			ss.append(" does not exist in this geometry.\n");
			String str = ss.toString();
			throw new IllegalStateException(str);
		}
		return canonical;
	}
	
	public Cell get(Coordinate coord) {
		checkExists(coord);

		// Validate input
		if (!indices.isOccupied(coord))
			throw new IllegalStateException("Attempted to access an empty cell.");

	    // Get pointer to cell and return it
		Cell res = map.get(coord.canonicalize());

	    return res;
	}

	/**
	 * Returns a vector containing the canonical coordinate of each
	 * site on the lattice.
	 * @return
	 */
	public Coordinate[] getCanonicalSites() {
		// Construct a copy of internal state
		Coordinate[] res = canonicalSites.clone();
		
		// Return it
		return res;
	}

	public void put(Coordinate coord, Cell cell) {
		checkExists(coord);
		map.put(coord, cell);
	}
	
	public int[] getStateVector() {
		Coordinate[] cArr = getCanonicalSites();
		
		int[] sArr = new int[cArr.length];
		
		for (int i = 0; i < cArr.length; i++) {
			Cell c = map.get(cArr[i]);
			if (c == null) { 
				sArr[i] = 0;
			} else {
				sArr[i] = map.get(cArr[i]).getState();
			}
		}
		
		return sArr;
	}

    /**
     * Returns the fitness vector, in canonical site order.
     */
    public double[] getFitnessVector() {
		Coordinate[] cArr = getCanonicalSites();
		
		double[] fArr = new double[cArr.length];
		
		for (int i = 0; i < cArr.length; i++) {
			Cell c = map.get(cArr[i]);
			if (c == null) { 
				fArr[i] = 0D;
			} else {
				fArr[i] = map.get(cArr[i]).getFitness();
			}
		}
		
		return fArr;
	}
}
