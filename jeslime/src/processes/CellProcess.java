package processes;

import structural.Lattice;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

public abstract class CellProcess {
	protected Lattice lattice;
	
	public CellProcess (Lattice lattice) {
		this.lattice = lattice;
	}
	
	public abstract Coordinate[] iterate() throws HaltCondition;
}
