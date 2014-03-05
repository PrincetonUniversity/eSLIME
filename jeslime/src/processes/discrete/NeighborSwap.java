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

import io.project.ProcessLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import layers.LayerManager;
import processes.StepState;
import processes.gillespie.GillespieState;
import geometry.Geometry;
import structural.GeneralParameters;
import layers.cell.CellLayer; import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

/**
 * Neighbor swap -- switches a randomly chosen cell with one of
 * its neighbors.
 * 
 * @author dbborens
 *
 */
public class NeighborSwap extends CellProcess {

	private List<SwapTuple> candidates = null;
    private Geometry geom;

	public NeighborSwap(ProcessLoader loader, LayerManager layerManager, int id,
			GeneralParameters p) {
		
		super(loader, layerManager, id, p);
	    geom = layer.getGeometry();
	}

	@Override
	public void fire(StepState state) throws HaltCondition {
		
		if (candidates == null) {
			throw new IllegalStateException("Attempted to call fire() before calling target().");
		}
		
		if (candidates.size() == 0) {
			return;
		}		
		
		//System.out.println("In NeighborSwap::iterate().");

		SwapTuple target = selectTarget();
		
		if (target == null) {
			return;
		}
		
		layer.getUpdateManager().swap(target.p, target.q);
		
		state.highlight(target.p);
		state.highlight(target.q);
		
		candidates = null;
	}

	private SwapTuple selectTarget() {

		
		// Choose a candidate
		int index = p.getRandom().nextInt(candidates.size());
		SwapTuple target = candidates.get(index);
		return target;
	}

	/**
	 * Convenience class for pairs of coordinates to swap.
	 * 
	 */
	private class SwapTuple {
		public Coordinate p;
		public Coordinate q;
		
		public SwapTuple(Coordinate p, Coordinate q) {
			this.p = p;
			this.q = q;
		}
	}

	@Override
	public void target(GillespieState gs) throws HaltCondition {
		
		// Create an ArrayList of SwapTuples
		candidates = new ArrayList<SwapTuple>();
		
		// Get a list of occupied sites
		Set<Coordinate> coords = layer.getViewer().getOccupiedSites();
		
		// For each occupied site...
		for(Coordinate coord : coords) {
		
			// See how many occupied neighbors it has
			Coordinate[] neighbors = geom.getNeighbors(coord, Geometry.APPLY_BOUNDARIES);
			
			// Add each possible swap as a candidate
			for (Coordinate neighbor : neighbors) {
				if (layer.getViewer().isOccupied(neighbor)) {
					SwapTuple sw = new SwapTuple(coord, neighbor);
					candidates.add(sw);
				}
			}
			
		}
		
		// Weight is defined in terms of number of swappable cells,
		// whereas event count is defined in terms of number of possible
		// swaps.
		if (gs != null) {
			gs.add(getID(), candidates.size(), coords.size() * 1.0D);
		}
	}
}
