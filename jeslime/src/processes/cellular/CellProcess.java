package processes.cellular;

import processes.Process;
import geometries.Geometry;
import structural.identifiers.Coordinate;
import io.project.ProcessLoader;
import structural.GeneralParameters;
import structural.Lattice;

public abstract class CellProcess extends Process {
	protected Lattice lattice;
	protected GeneralParameters p;
	
	// This set represents the sites where the process
	// is allowed to take place. By default, it is set
	// to all canonical sites.
	protected Coordinate[] activeSites;
	
	public CellProcess(ProcessLoader loader, Lattice lattice, int id, 
			Geometry geom, GeneralParameters p) {
		
		
		super(loader, id, geom);
		
		this.p = p;
		this.lattice = lattice;
		
		activeSites = this.loadSiteList("active-sites");
	}

	protected String getProcessClass() {
		return this.getClass().getSimpleName();
	}
}
