package processes.continuum;

import org.dom4j.Element;

import geometry.Geometry;
import io.project.ProcessLoader;
import processes.Process;
import processes.StepState;
import processes.gillespie.GillespieState;
import structural.halt.HaltCondition;

public abstract class ContinuumProcess extends Process {

	// Constructor needs to load in a list of matrix operations
	// The particular subclass specifies what kind of solver will be used,
	// what information will be provided, etc
	
	public ContinuumProcess(ProcessLoader loader, int id, Geometry geom) {
		super(loader, id, geom);
		loadOperations(loader);
	}

	@Override
	public void target(GillespieState gs) throws HaltCondition {
		// Continuum processes either happen or they don't.
		if (gs != null) {
			gs.add(getID(), 1, 1D);
		}
	}

	private void loadOperations(ProcessLoader loader) {
		Element root = e.element("operations");
		
		for (Object o : root.elements()) {
			Element op = (Element) o;
			
			// Load each of the children into the operator matrix/source vector
			load(op);
		}
	}
			
	protected abstract void solve(); 
	protected abstract void load(Element operation);
}
