package processes;

import org.dom4j.Element;

import structural.Lattice;
import structural.halt.HaltCondition;

import io.project.CellFactory;
import io.project.ProcessLoader;
import io.project.ProjectLoader;

public abstract class Process {

	int id;
	int period;
	
	// XML element associated with this process
	Element e;
	
	/* Constructors */
	
	public Process(ProcessLoader loader, int id) {
		
		if (loader == null) {
			System.err.println("WARNING: Mock behavior for process loader invoked. Use only for testing!");
			id = 0;
			period = 1;
			return;
		}
		
		this.id = id;
		e = loader.getProcess(id);
		period = Integer.valueOf(get("period"));
	}

	/* Process identifiers */
	
	protected abstract String getElementName();

	protected abstract String getProcessClass();

	public int getID() {
		return id;
	}
	
	/* Process events */
	
	public abstract void iterate(StepState state) throws HaltCondition;
	
	/**
	 *  Pull in a single-datum element
	 * @param g
	 * @param key
	 * @return
	 */
	private String get(Element g, String key) {
		Element vElem = g.element(key);
		if (vElem == null) {
			throw new IllegalArgumentException("Required parameter '" + 
					key + "' not defined for process " +
					getProcessClass() + " (id=" + getID() +").");
		}
		
		Object value = vElem.getData();
		
		return value.toString();
	}

	protected String get(String key) {
		return get(e, key);
	}
	
	public int getPeriod() {
		return period;
	}
	
	protected CellFactory getCellFactory(Lattice lattice) {
		return new CellFactory(lattice, e.element("cell-descriptor"));
	}
}
