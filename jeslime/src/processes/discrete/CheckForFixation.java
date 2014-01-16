package processes.discrete;

import geometry.Geometry;
import io.project.ProcessLoader;
import layers.LayerManager;
import layers.cell.CellLayer;
import layers.cell.StateMapViewer;
import processes.StepState;
import processes.gillespie.GillespieState;
import structural.GeneralParameters;
import structural.halt.FixationEvent;
import structural.halt.HaltCondition;

/**
 * Created by dbborens on 1/13/14.
 */
public class CheckForFixation extends CellProcess {
    public CheckForFixation(ProcessLoader loader, LayerManager layerManager, int id, GeneralParameters p) {
        super(loader, layerManager, id, p);
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {
        // There's only one event that can happen--we update.
        if (gs != null) {
            gs.add(this.getID(), 1, 0.0D);
        }
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
		StateMapViewer smv = layer.getViewer().getStateMapViewer();

		for (Integer s : smv.getStates()) {
			if (smv.getCount(s) == layer.getGeometry().getCanonicalSites().length) {
				throw new FixationEvent(state.getTime(), s);
			}
		}
    }
}
