package processes.temporal;

import processes.Process;
import geometries.Geometry;
import io.parameters.ProcessLoader;
import structural.GeneralParameters;
import structural.Lattice;

public abstract class TimeProcess extends Process {
	protected Lattice lattice;

	
	private final String elementName = "time-process";
	
	public TimeProcess(ProcessLoader loader, Lattice lattice, int id, 
			Geometry geom, GeneralParameters p) {
		super(loader, id);
		
		this.lattice = lattice;
	}
	
	protected String getElementName() {
		return elementName;
	}
	
	protected String getProcessClass() {
		return this.getClass().getSimpleName();
	}
}
