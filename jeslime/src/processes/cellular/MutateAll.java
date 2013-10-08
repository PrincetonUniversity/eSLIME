package processes.cellular;

import java.util.ArrayList;
import java.util.HashSet;

import org.dom4j.Element;

import cells.Cell;

import geometries.Geometry;
import io.project.ProcessLoader;
import processes.StepState;
import structural.GeneralParameters;
import structural.Lattice;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

/**
 * Adds a fixed amount of biomass to every cell with a cell
 * state matching the target.
 * 
 * @author dbborens
 *
 */
public class MutateAll extends CellProcess {

	// How much biomass to accumulate per time step
	private int ancestral;
	private int mutant;
	
	public MutateAll(ProcessLoader loader, Lattice lattice, int id,
			Geometry geom, GeneralParameters p) {
		super(loader, lattice, id, geom, p);
		
		ancestral = Integer.valueOf(e.element("mutation").attribute("ancestral").getText());
		mutant = Integer.valueOf(e.element("mutation").attribute("mutant").getText());
		
	}

	public MutateAll(Lattice lattice, Geometry geom, 
			int ancestral, int mutant) {
		super(null, lattice, 0, geom, null);
		
		this.ancestral = ancestral;
		this.mutant = mutant;
	}
	
	public void iterate(StepState state) throws HaltCondition {
		// If the target state doesn't exist, don't waste any time
		// checking cells.
		int targetCount = lattice.getStateMapViewer().getCount(ancestral);
		if (targetCount == 0) {
			return;
		}
		
		ArrayList<Coordinate> targetSites = new ArrayList<Coordinate>(targetCount);
		// Feed the cells.
		for (Coordinate site : activeSites) {
			if (lattice.isOccupied(site) && lattice.getCell(site).getState() == ancestral) {
				Cell child = lattice.getCell(site).clone(mutant);
				lattice.banish(site);
				lattice.place(child, site);
			}
		}
	}

}
