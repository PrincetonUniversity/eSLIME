package processes.cellular;

import java.util.HashSet;

import org.dom4j.Element;

import geometries.Geometry;
import io.project.ProcessLoader;
import processes.StepState;
import structural.GeneralParameters;
import structural.Lattice;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

/**
 * Adds a fixed amount of biomass to every cell.
 * 
 * @author dbborens
 *
 */
public class UniformBiomassGrowth extends CellProcess {

	// How much biomass to accumulate per time step
	private double delta;
	
	// If false, apply() will be called on cells after their
	// biomass is updated. If true, you must call apply() on
	// a cell before the new biomass accumulates.
	private boolean defer;
	
	public UniformBiomassGrowth(ProcessLoader loader, Lattice lattice, int id,
			Geometry geom, GeneralParameters p) {
		super(loader, lattice, id, geom, p);
		
		delta = Double.valueOf(get("delta"));
		
		defer = Boolean.valueOf(get("defer"));
	}

	public UniformBiomassGrowth(Lattice lattice, Geometry geom, 
			double delta, boolean defer) {
		super(null, lattice, 0, geom, null);
		
		this.delta = delta;
		this.defer = defer;
	}
	public void iterate(StepState state) throws HaltCondition {
		// Feed the cells.
		for (Coordinate site : activeSites) {
			if (lattice.isOccupied(site)) {
				lattice.feed(site, delta);
			}
		}
		
		// If we're not defering updates, tell the cells to use the
		// new data right away. Note that we want to do this after
		// every cell has been "fed," in case there are non-local
		// interactions.
		if (!defer) {
			for (Coordinate site : activeSites) {
				if (lattice.isOccupied(site)) {
					lattice.apply(site);
				}
			}
		}
		
		// Since we update every cell, we don't bother highlighting
		// any of them for display.
	}

}
