package processes.cellular;

import io.project.ProcessLoader;

import java.util.HashSet;
import java.util.Random;

import processes.StepState;


import cells.Cell;

import structural.Flags;
import structural.GeneralParameters;
import structural.Lattice;
import structural.halt.FixationEvent;
import structural.halt.HaltCondition;
import structural.halt.LatticeFullEvent;
import structural.identifiers.Coordinate;

import geometries.Geometry;

public class DivideAnywhere extends CellProcess {
	
	private Random random;
	
	public DivideAnywhere(ProcessLoader loader, Lattice lattice, int id,
			Geometry geom, GeneralParameters p) {
		
		super(loader, lattice, id, geom, p);

		random = p.getRandom();
	}

	public void iterate(StepState state) throws HaltCondition {
		HashSet<Coordinate> preliminary = new HashSet<Coordinate>();

		// Choose a random active cell.
		HashSet<Coordinate> candSet = lattice.getDivisibleSites();
		Coordinate[] candidates = candSet.toArray(new Coordinate[0]);
		
		Coordinate origin, target;

		if (candidates.length == 0) {
			return;
		} else {

			int i = random.nextInt(candidates.length);
			origin = candidates[i];
		}
		
		state.highlight(origin);
		
		// Get nearest vacancies to the cell
		Coordinate[] targets = lattice.getNearestVacancies(origin, -1);
		if (targets.length == 0) {
			throw new LatticeFullEvent(lattice.getGillespie());
		} else {
			int i = random.nextInt(targets.length);
			target = targets[i];
		}
		
		// Get child cell
		Cell child = lattice.divide(origin);

		// Move parent cell to the chosen vacancy, if necessary by shoving the
		// parent and a line of interior cells toward the surface
		int[] displacement = geom.getDisplacement(origin, target);

		shove(origin, displacement, preliminary);


		// Look for sites that have been pushed out of the world
		for (Coordinate toCheck : preliminary) {

			if (toCheck.hasFlag(Flags.END_OF_WORLD)) {
				lattice.banish(toCheck);
			} else {
				state.highlight(toCheck);
			}

		}
		
		// Divide the child cell into the vacancy left by the parent
		lattice.place(child, origin);
	}

	private int norm(int[] vec) {
		int agg = 0;

		for (int i = 0; i < vec.length; i++) {
			agg += Math.abs(vec[i]);
		}

		return agg;
	}
	
	void shove(Coordinate curLoc, int[] d, HashSet<Coordinate> sites) {

		// Base case 0: we've reached the target
		if (norm(d) == 0) {
			return;
		}

		// Choose whether to go horizontally or vertically, weighted
		// by the number of steps remaining in each direction
		int nv = norm(d);
		
		// Take a step in the chosen direction.
		int[] dNext;				// Displacement vector, one step closer
		Coordinate nextLoc;

		int[] rel = new int[3];			// Will contain a unit vector specifying
											// step direction

		// Loop if the move is illegal.
		do {
			int n = random.nextInt(nv);

			dNext = d.clone();
			for (int i = 0; i < 3; i++) {rel[i] = 0;}
			// Decrement the displacement vector by one unit in a randomly chosen
			// direction, weighted so that the path is, on average, straight.
			if (n < Math.abs(d[0])) {
				dNext[0] -= (int) Math.signum(d[0]);
				rel[0] += (int) Math.signum(d[0]);
			} else if (n < (Math.abs(d[0]) + Math.abs(d[1]))) {
				dNext[1] -= (int) Math.signum(d[1]);
				rel[1] += (int) Math.signum(d[1]);
			} else {
				dNext[2] -= (int) Math.signum(d[2]);
				rel[2] += (int) Math.signum(d[2]);
			}

			nextLoc = geom.rel2abs(curLoc, rel);

			
			if (nextLoc.hasFlag(Flags.BEYOND_BOUNDS) && nv == 1) {
				throw new IllegalStateException("There's only one place to push cells and it's illegal!");
			} else if (!nextLoc.hasFlag(Flags.BEYOND_BOUNDS)) {
				break;
			}
		} while(true);

		shove(nextLoc, dNext, sites);

		lattice.f_swap(curLoc, nextLoc);

		sites.add(nextLoc);
	}
}
