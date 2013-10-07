package processes.cellular;

import geometries.Geometry;
import io.project.CellFactory;
import io.project.ProcessLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.dom4j.Element;

import processes.StepState;


import cells.Cell;
import cells.SimpleCell;

import structural.GeneralParameters;
import structural.Lattice;
import structural.halt.HaltCondition;
import structural.halt.LatticeFullEvent;
import structural.identifiers.Coordinate;

/**
 * Fills in all sites in the active site set
 * with cells of the type specified by the
 * process' cell descriptor. Does not throw
 * LatticeFullExceptions.
 * 
 * @author dbborens
 *
 */
public class Fill extends CellProcess {
	
	// If true, the process will skip over any already-filled sites. If
	// false, it will blow up if it encounters an already-filled site 
	// that it expected to fill.
	boolean skipFilled;
	
	public Fill(ProcessLoader loader, Lattice lattice, int id,
			Geometry geom, GeneralParameters p) {
		
		super(loader, lattice, id, geom, p);
		
		skipFilled = Boolean.valueOf(get("skip-filled-sites"));
		
		
	}

	public void iterate(StepState state) throws HaltCondition {
		CellFactory factory = getCellFactory(lattice);
		
		for (Coordinate c : activeSites) {
			boolean filled = lattice.getOccupiedSites().contains(c);
			
			if (filled && !skipFilled) {
				String msg = "Attempted to fill coordinate " + c.toString() + 
						" but it was already filled. This is illegal unless" + 
						" the <skip-filled-sites> flag is set to true. Did you" +
						" mean to set it? (id=" + getID() + ")";
				
				throw new IllegalStateException(msg);
			} else if (!filled) {
				Cell cell = factory.instantiate();
				lattice.place(cell, c);
			} else {
				// Do nothing if site is filled and skipFilled is true.
			}
		}
	}
}
