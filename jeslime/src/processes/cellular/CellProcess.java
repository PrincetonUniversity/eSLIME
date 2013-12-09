package processes.cellular;

import processes.Process;
import geometry.Geometry;
import structural.identifiers.Coordinate;
import jeslime.io.project.ProcessLoader;
import structural.GeneralParameters;
import layers.cell.CellLayer; 
public abstract class CellProcess extends Process {
	protected CellLayer layer;
	protected GeneralParameters p;
	
	// This set represents the sites where the process
	// is allowed to take place. By default, it is set
	// to all canonical sites.
	protected Coordinate[] activeSites;
	
	public CellProcess(ProcessLoader loader, CellLayer layer, int id, 
			Geometry geom, GeneralParameters p) {
		
		
		super(loader, id, geom);
		
		this.p = p;
		this.layer = layer;
		
		activeSites = this.loadSiteList("active-sites");
	}

	protected String getProcessClass() {
		return this.getClass().getSimpleName();
	}
}
