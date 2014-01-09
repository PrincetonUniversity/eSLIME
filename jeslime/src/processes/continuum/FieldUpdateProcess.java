package processes.continuum;

import geometry.Geometry;
import io.project.ProcessLoader;
import layers.LayerManager;
import layers.cell.CellLayer;
import layers.solute.SoluteLayer;
import no.uib.cipr.matrix.DenseVector;
import processes.Process;
import processes.StepState;
import processes.gillespie.GillespieState;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

import java.util.Set;

/**
 * Created by David B Borenstein on 1/7/14.
 */
public class FieldUpdateProcess extends ContinuumProcess {

    private final CellLayer discreteLayer;
    private final SoluteLayer continuumLayer;
    
    public FieldUpdateProcess(ProcessLoader loader, int processId, LayerManager layerManager, String layerId) {
       super(loader, processId, layerManager.getSoluteLayer(layerId).getGeometry());

       discreteLayer = layerManager.getCellLayer();
       continuumLayer = layerManager.getSoluteLayer(layerId);
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
            // CHECK API AND REMEMBER HOW TO GET THIS INFORMATION -- is it just getCell?
            double production = discreteLayer.getViewer().getCell(c).getProduction(layerId);
            int i = geom.coordToIndex(c);

            vec.set(i, production);
        }

        return vec;
    }

}
