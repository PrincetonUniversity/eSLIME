package processes.cellular;

import java.util.HashSet;
import java.util.Random;

import cells.Cell;
import processes.StepState;
import geometry.Geometry;
import jeslime.io.project.ProcessLoader;
import structural.Flags;
import structural.GeneralParameters;
import layers.cell.CellLayer; 
import structural.halt.HaltCondition;
import structural.halt.LatticeFullEvent;
import structural.identifiers.Coordinate;

public abstract class BulkDivisionProcess extends CellProcess{

	protected Random random;

	public BulkDivisionProcess(ProcessLoader loader, CellLayer layer, int id,
			Geometry geom, GeneralParameters p) {
		super(loader, layer, id, geom, p);
		random = p.getRandom();

	}

	protected void execute(StepState state, Coordinate[] candidates) throws HaltCondition {
		System.out.println("Executing bulk division process.");
		HashSet<Coordinate> preliminary = new HashSet<Coordinate>();

		Coordinate origin, target;

		if (candidates.length == 0) {
			return;
		} else {

			int i = random.nextInt(candidates.length);
			origin = candidates[i];
		}
		
		System.out.println("   Origin: " + origin);
		
		// UNCOMMENT ME FOR HIGHLIGHTING
		//state.highlight(origin);
		
		// Get nearest vacancies to the cell
		Coordinate[] targets = layer.getLookupManager().getNearestVacancies(origin, -1);
		System.out.println("   Nearest vacancies: ");
		for (Coordinate t : targets) {
			System.out.println("      " + t + " (d=" + geom.getL1Distance(origin, t, Geometry.APPLY_BOUNDARIES) +", ba=" + (t.hasFlag(Flags.BOUNDARY_APPLIED)) +")");
		}
		
		// CONTINUE ADDING DIAGNOSTICS HERE
		if (targets.length == 0) {
			throw new LatticeFullEvent(state.getTime());
		} else {
			int i = random.nextInt(targets.length);
			target = targets[i];
		}
		System.out.println("   Chose " + target + ".");
		// Get child cell
		Cell child = layer.getUpdateManager().divide(origin);

		// Move parent cell to the chosen vacancy, if necessary by shoving the
		// parent and a line of interior cells toward the surface
		Coordinate displacement = geom.getDisplacement(origin, target, Geometry.APPLY_BOUNDARIES);
		System.out.println("   Displacement vector to target: " + displacement);
		
		if (!layer.getViewer().isOccupied(origin)) {
			throw new IllegalStateException();
		}
		
		System.out.print("   Shoving...");
		
		shove(origin, displacement, preliminary);
				
		if (layer.getViewer().isOccupied(origin)) {
			throw new IllegalStateException();
		}

		// Look for sites that have been pushed out of the world
		for (Coordinate toCheck : preliminary) {

			if (toCheck.hasFlag(Flags.END_OF_WORLD)) {
				layer.getUpdateManager().banish(toCheck);
			} else {
				// UNCOMMENT ME AFTER GENERATING IMAGES FOR NED!
				//state.highlight(toCheck);
			}

		}
		
		System.out.println("   Placing child cell.");
		// Divide the child cell into the vacancy left by the parent
		layer.getUpdateManager().place(child, origin);
		System.out.println(" Division complete.");
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
		System.out.println("      Inside shove: curLoc=" + curLoc + "; d=" + d + ".");
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
}
