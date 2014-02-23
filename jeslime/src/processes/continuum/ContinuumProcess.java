package processes.continuum;
import geometry.Geometry;
import io.project.ProcessLoader;
import layers.LayerManager;
import processes.Process;
import processes.gillespie.GillespieState;
import structural.GeneralParameters;
import structural.halt.HaltCondition;

/**
 * Created by David B Borenstein on 1/7/14.
 */
public abstract class ContinuumProcess extends Process {

    public ContinuumProcess(ProcessLoader loader, LayerManager layerManager, GeneralParameters p,
                            int id) {
        super(loader, layerManager, p, id);
    }

    protected String getProcessClass() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {
        // There's only one event that can happen--we update.
        if (gs != null) {
            gs.add(this.getID(), 1, 0.0D);
        }
    }
}
