/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

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
