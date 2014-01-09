package processes.continuum;
import geometry.Geometry;
import io.project.ProcessLoader;
import processes.Process;
import processes.gillespie.GillespieState;
import structural.halt.HaltCondition;

/**
 * Created by David B Borenstein on 1/7/14.
 */
public abstract class ContinuumProcess extends Process {

    public ContinuumProcess(ProcessLoader loader, int id, Geometry geom) {
        super(loader, id, geom);
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
