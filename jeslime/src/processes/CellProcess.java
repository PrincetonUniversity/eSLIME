package processes;

import structural.Coordinate;
import structural.HaltException;
import structural.Lattice;

public abstract class CellProcess {
	protected Lattice lattice;
	
	public CellProcess (Lattice lattice) {
		this.lattice = lattice;
	}
	
	public abstract Coordinate[] iterate() throws HaltException;
}
