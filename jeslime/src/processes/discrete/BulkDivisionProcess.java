package processes.discrete;

import cells.Cell;
import geometry.Geometry;
import io.project.ProcessLoader;
import layers.LayerManager;
import processes.StepState;
import structural.Flags;
import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.halt.LatticeFullEvent;
import structural.identifiers.Coordinate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public abstract class BulkDivisionProcess extends CellProcess{

    // This class is in need of a refactor.

    protected Random random;
    private Geometry geom;
    private int maxTargets;

    public BulkDivisionProcess(ProcessLoader loader, LayerManager layerManager, int id,
                               GeneralParameters p, int maxTargets) {
        super(loader, layerManager, id, p);
		random = p.getRandom();
        geom = layer.getGeometry();
        this.maxTargets = maxTargets;
    }

    protected void execute(StepState state, Coordinate[] candidates) throws HaltCondition {
        Coordinate[] chosen = respectMaxTargets(candidates);
        Cell[] chosenCells = toCellArray(chosen);
        for (Cell cell : chosenCells) {
            Coordinate currentLocation = layer.getLookupManager().getCellLocation(cell);
            doDivision(state, currentLocation);
        }
    }

    private Cell[] toCellArray(Coordinate[] chosen) {
        int n = chosen.length;
        Cell[] cells = new Cell[n];
        for (int i = 0; i < n; i++) {
            Coordinate coord = chosen[i];
            Cell cell = layer.getViewer().getCell(coord);
            cells[i] = cell;
        }

        return cells;
    }


    protected void doDivision(StepState state, Coordinate origin) throws HaltCondition {
        HashSet<Coordinate> preliminary = new HashSet<Coordinate>();

        Coordinate target;

        // Get nearest vacancies to the cell
		Coordinate[] targets = layer.getLookupManager().getNearestVacancies(origin, -1);

		if (targets.length == 0) {
			throw new LatticeFullEvent(state.getTime());
		} else {
			int i = random.nextInt(targets.length);
			target = targets[i];
		}

        // Get child cell
		Cell child = layer.getUpdateManager().divide(origin);

		// Move parent cell to the chosen vacancy, if necessary by shoving the
		// parent and a line of interior cells toward the surface
		Coordinate displacement = geom.getDisplacement(origin, target, Geometry.APPLY_BOUNDARIES);

        if (!layer.getViewer().isOccupied(origin)) {
			throw new IllegalStateException();
		}


        shove(origin, displacement, preliminary);
				
		if (layer.getViewer().isOccupied(origin)) {
			throw new IllegalStateException();
		}

		// Look for sites that have been pushed out of the world
		for (Coordinate toCheck : preliminary) {

			if (toCheck.hasFlag(Flags.END_OF_WORLD)) {
				layer.getUpdateManager().banish(toCheck);
			}

		}
		
		// Divide the child cell into the vacancy left by the parent
		layer.getUpdateManager().place(child, origin);
	}
	
	
	/**
	 * 
	 * @param curLoc: starting location. the child will be placed in this
	 *    position after the parent is shoved.
	 * @param d: displacement vector to target, in natural basis of lattice.
	 * @param sites: list of affected sites (for highlighting)
	 * 
	 * TODO: This is so cloodgy and terrible.
	 */
	private void shove(Coordinate curLoc, Coordinate d, HashSet<Coordinate> sites) {
		// Base case 0: we've reached the target
		if (d.norm() == 0) {
			return;
		}

		// Choose whether to go horizontally or vertically, weighted
		// by the number of steps remaining in each direction
		int nv = d.norm();
		
		// Take a step in the chosen direction.
		int[] dNext;				// Displacement vector, one step closer
		Coordinate nextLoc;

		int[] rel = new int[3];			// Will contain a unit vector specifying
											// step direction

		// Loop if the move is illegal.
		do {
			int n = random.nextInt(nv);

			dNext = new int[] {d.x(), d.y(), d.z()};
			
			// Initialize rel vector
			for (int i = 0; i < 3; i++) {rel[i] = 0;}
			
			// Decrement the displacement vector by one unit in a randomly chosen
			// direction, weighted so that the path is, on average, straight.
			if (n < Math.abs(d.x())) {
				dNext[0] -= (int) Math.signum(d.x());
				rel[0] += (int) Math.signum(d.x());
			} else if (n < (Math.abs(d.x()) + Math.abs(d.y()))) {
				dNext[1] -= (int) Math.signum(d.y());
				rel[1] += (int) Math.signum(d.y());
			} else {
				dNext[2] -= (int) Math.signum(d.z());
				rel[2] += (int) Math.signum(d.z());
			}

			Coordinate disp = new Coordinate(rel, d.flags());
			
				nextLoc = geom.rel2abs(curLoc, disp, Geometry.APPLY_BOUNDARIES);

			if (nextLoc == null) {
				continue;
			} else if (nextLoc.hasFlag(Flags.BEYOND_BOUNDS) && nv == 1) {
				throw new IllegalStateException("There's only one place to push cells and it's illegal!");
			} else if (!nextLoc.hasFlag(Flags.BEYOND_BOUNDS)) {
				break;
			}
		} while(true);

		Coordinate du = new Coordinate(dNext, d.flags());
		shove(nextLoc, du, sites);

		layer.getUpdateManager().f_swap(curLoc, nextLoc);

		sites.add(nextLoc);
	}

    private Coordinate[] respectMaxTargets(Coordinate[] candidates) {
        // If maxTargets is < 0, it means that there is no maxTargets; return all.
        if (maxTargets < 0) {
            return candidates;
        }
        // If there the number of candidates does not exceed the max, return.
        if (candidates.length <= maxTargets) {
            return candidates;
        }

        // Otherwise, permute and choose the first n, where n = maxTargets.
        permute(candidates);

        Coordinate[] reduced = Arrays.copyOfRange(candidates, 0, maxTargets);

        return reduced;
    }

    /**
     * Fischer-Yates shuffling algorithm for permuting the contents of
     * a coordinate array.
     */
    private void permute(Coordinate[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            swap(arr, i, j);
        }
    }

    private void swap(Coordinate[] arr, int i, int j) {
        Coordinate temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
    }
}
