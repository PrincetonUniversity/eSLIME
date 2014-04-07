/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package processes.continuum;

import geometry.Geometry;
import io.loader.ProcessLoader;
import layers.LayerManager;
import layers.cell.CellLayer;
import layers.solute.SoluteLayer;
import no.uib.cipr.matrix.DenseVector;
import processes.StepState;
import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

import java.util.Set;

/**
 * Created by David B Borenstein on 1/7/14.
 */
public class FieldUpdateProcess extends ContinuumProcess {

    private final CellLayer discreteLayer;
    private final SoluteLayer continuumLayer;
    private final Geometry geom;

    public FieldUpdateProcess(ProcessLoader loader, int processId, LayerManager layerManager, GeneralParameters p, String layerId) {
        super(loader, layerManager, p, processId);

        discreteLayer = layerManager.getCellLayer();
        continuumLayer = layerManager.getSoluteLayer(layerId);
        geom = layerManager.getCellLayer().getGeometry();
    }

    @Override
    protected String getProcessClass() {
        return this.getClass().getSimpleName();
    }


    @Override
    public void fire(StepState state) throws HaltCondition {
        // Check for production and apply it to the source vector.
        DenseVector source = checkForProduction();

        // Update source vector on solute field.
        continuumLayer.setSource(source);

        // Integrate solute field.
        continuumLayer.integrate();
    }

    protected DenseVector checkForProduction() {
        int n = geom.getCanonicalSites().length;
        DenseVector vec = new DenseVector(n);
        Set<Coordinate> sites = discreteLayer.getViewer().getOccupiedSites();

        String layerId = continuumLayer.getId();
        for (Coordinate c : sites) {
            double production = discreteLayer.getViewer().getCell(c).getProduction(layerId);
            int i = geom.coordToIndex(c);

            vec.set(i, production);
        }

        return vec;
    }

}
