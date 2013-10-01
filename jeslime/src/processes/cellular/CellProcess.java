package processes.cellular;

import processes.Process;
import geometries.Geometry;
import io.project.ProcessLoader;
import structural.GeneralParameters;
import structural.Lattice;

public abstract class CellProcess extends Process {
	protected Lattice lattice;
	protected Geometry geom;
	protected GeneralParameters p;
	
	private final String elementName = "cell-process";
	
	public CellProcess(ProcessLoader loader, Lattice lattice, int id, 
			Geometry geom, GeneralParameters p) {
		super(loader, id);
		
		this.p = p;
		this.geom = geom;
		this.lattice = lattice;
	}
	
	protected String getElementName() {
		return elementName;
	}
	
	protected String getProcessClass() {
		return this.getClass().getSimpleName();
	}
}
