package processes.discrete;

import layers.LayerManager;
import processes.Process;
import geometry.Geometry;
import structural.identifiers.Coordinate;
import io.project.ProcessLoader;
import structural.GeneralParameters;
import layers.cell.CellLayer; 
public abstract class CellProcess extends Process {
	protected GeneralParameters p;
	
	// This set represents the sites where the process
	// is allowed to take place. By default, it is set
	// to all canonical sites.
	protected Coordinate[] activeSites;

    // These are easily accessed from the layer manager, but there
    // are very many calls to them thanks to some legacy code.
    protected CellLayer layer;
    protected Geometry geom;

	public CellProcess(ProcessLoader loader, LayerManager layerManager, int id,
			GeneralParameters p) {
		
		
		super(loader, layerManager, p, id);
		
		this.p = p;
        layer = layerManager.getCellLayer();
		activeSites = this.loadSiteList("active-sites");
	}

	protected String getProcessClass() {
		return this.getClass().getSimpleName();
	}

    public void setActiveSites(Coordinate[] activeSites) {
        this.activeSites = activeSites;
    }
}
